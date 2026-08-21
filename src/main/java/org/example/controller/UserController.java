package org.example.controller;

import org.example.common.Result;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * 处理用户管理相关接口
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询所有用户
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 创建用户
     */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 查询所有用户（POST）
     */
    @PostMapping("/list")
    public List<User> listUsers() {
        return userService.getAllUsers();
    }

    /**
     * 获取待审批用户列表
     *
     * @param authorization 请求头中的 Authorization（Bearer Token）
     * @return 待审批用户列表
     */
    @GetMapping("/pending")
    public Result<List<User>> getPendingUsers(@RequestHeader(value = "Authorization", required = false) String authorization) {
        // 验证 Token
        if (!isValidAdmin(authorization)) {
            return Result.unauthorized();
        }

        List<User> pendingUsers = userService.getUsersByStatus("PENDING");
        return Result.success(pendingUsers);
    }

    /**
     * 审批账号
     *
     * @param userId 用户唯一标识
     * @param request 审批请求，包含 action（APPROVE/REJECT）
     * @param authorization 请求头中的 Authorization（Bearer Token）
     * @return 审批结果
     */
    @PostMapping("/approve/{userId}")
    public Result<Map<String, Object>> approveAccount(@PathVariable String userId,
                                                      @RequestBody Map<String, String> request,
                                                      @RequestHeader(value = "Authorization", required = false) String authorization) {
        // 验证 Token 和管理员权限
        String adminId = getAdminUserId(authorization);
        if (adminId == null) {
            return Result.unauthorized();
        }

        String action = request.get("action");
        if (action == null || action.isEmpty()) {
            return Result.error("审批动作不能为空");
        }

        try {
            Map<String, Object> result = userService.approveAccount(userId, action, adminId);
            return Result.success("审批成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 验证是否为有效管理员
     */
    private boolean isValidAdmin(String authorization) {
        String userId = getAdminUserId(authorization);
        return userId != null;
    }

    /**
     * 从 Token 中获取管理员用户 ID
     */
    private String getAdminUserId(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }

        String token = authorization.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return null;
        }

        return jwtUtil.getUserIdFromToken(token);
    }
}
