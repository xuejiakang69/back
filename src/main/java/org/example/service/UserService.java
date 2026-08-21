package org.example.service;

import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务类
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * BCrypt 密码加密器
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 密码过期天数
     */
    private static final int PASSWORD_EXPIRE_DAYS = 30;

    /**
     * 根据 ID 查询用户
     */
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据用户唯一标识查询用户
     */
    public User getUserByUserId(String userId) {
        return userMapper.selectByUserId(userId);
    }

    /**
     * 根据用户名查询用户
     */
    public User getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 查询所有用户
     */
    public List<User> getAllUsers() {
        return userMapper.selectAll();
    }

    /**
     * 根据状态查询用户列表
     */
    public List<User> getUsersByStatus(String status) {
        return userMapper.selectByStatus(status);
    }

    /**
     * 创建用户
     */
    public User createUser(User user) {
        userMapper.insert(user);
        return user;
    }

    /**
     * 更新用户
     */
    public User updateUser(User user) {
        userMapper.update(user);
        return user;
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Long id) {
        return userMapper.deleteById(id) > 0;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含 token、userInfo、needChangePassword
     */
    public Map<String, Object> login(String username, String password) {
        // 查询用户
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 检查账号状态
        if (!"APPROVED".equals(user.getStatus())) {
            if ("PENDING".equals(user.getStatus())) {
                throw new RuntimeException("账号待审批，请等待管理员审核");
            } else if ("REJECTED".equals(user.getStatus())) {
                throw new RuntimeException("账号审批已被拒绝");
            } else if ("DISABLED".equals(user.getStatus())) {
                throw new RuntimeException("账号已被禁用");
            }
            throw new RuntimeException("账号状态异常");
        }

        // 检查密码是否过期
        boolean needChangePassword = isPasswordExpired(user);

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getUserId());
        result.put("username", user.getUsername());
        result.put("realName", user.getRealName());
        result.put("needChangePassword", needChangePassword);

        return result;
    }

    /**
     * 修改密码
     *
     * @param userId      用户唯一标识
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void changePassword(String userId, String oldPassword, String newPassword) {
        // 查询用户
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 检查新旧密码是否相同
        if (oldPassword.equals(newPassword)) {
            throw new RuntimeException("新密码不能与旧密码相同");
        }

        // 加密新密码并更新
        String encodedPassword = passwordEncoder.encode(newPassword);
        userMapper.updatePassword(userId, encodedPassword, LocalDateTime.now());
    }

    /**
     * 账号申请
     *
     * @param username   用户名
     * @param realName   真实姓名
     * @param phone      手机号
     * @param email      邮箱
     * @param department 部门
     * @return 申请结果
     */
    public User applyAccount(String username, String realName, String phone, String email, String department) {
        // 检查用户名是否已存在
        User existingUser = userMapper.selectByUsername(username);
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户记录
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setUsername(username);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setDepartment(department);
        user.setStatus("PENDING");
        user.setCreatedAt(LocalDateTime.now());

        userMapper.insert(user);
        return user;
    }

    /**
     * 审批账号
     *
     * @param userId 用户唯一标识
     * @param action 审批动作：APPROVE-通过，REJECT-拒绝
     * @param adminId 审批管理员 ID（可选，用于记录日志）
     * @return 审批结果，如果通过则包含初始密码
     */
    public Map<String, Object> approveAccount(String userId, String action, String adminId) {
        // 查询用户
        User user = userMapper.selectByUserId(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 检查当前状态
        if (!"PENDING".equals(user.getStatus())) {
            throw new RuntimeException("该账号不在待审批状态");
        }

        Map<String, Object> result = new HashMap<>();

        if ("APPROVE".equals(action)) {
            // 生成初始密码
            String initialPassword = generateInitialPassword();
            String encodedPassword = passwordEncoder.encode(initialPassword);

            // 更新用户状态和密码
            userMapper.updateStatus(userId, "APPROVED");
            userMapper.updatePassword(userId, encodedPassword, LocalDateTime.now());

            result.put("initialPassword", initialPassword);
            result.put("message", "审批通过，初始密码已生成");
        } else if ("REJECT".equals(action)) {
            userMapper.updateStatus(userId, "REJECTED");
            result.put("message", "已拒绝该账号申请");
        } else {
            throw new RuntimeException("无效的审批动作");
        }

        return result;
    }

    /**
     * 检查密码是否过期
     *
     * @param user 用户对象
     * @return true-已过期，false-未过期
     */
    public boolean isPasswordExpired(User user) {
        if (user.getPasswordUpdatedAt() == null) {
            return true;
        }
        LocalDateTime expireTime = user.getPasswordUpdatedAt().plusDays(PASSWORD_EXPIRE_DAYS);
        return LocalDateTime.now().isAfter(expireTime);
    }

    /**
     * 生成初始密码（8位随机字符）
     */
    private String generateInitialPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }
        return password.toString();
    }
}
