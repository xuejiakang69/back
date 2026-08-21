package org.example.controller;

import org.example.common.Result;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理登录、修改密码、账号申请等认证相关接口
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户登录
     *
     * @param request 登录请求，包含 username 和 password
     * @return 登录结果，包含 token 和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        // 参数校验
        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (password == null || password.isEmpty()) {
            return Result.error("密码不能为空");
        }

        try {
            // 调用登录服务
            Map<String, Object> loginResult = userService.login(username, password);

            // 生成 JWT Token
            String userId = (String) loginResult.get("userId");
            String token = jwtUtil.generateToken(userId, username);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", loginResult);
            result.put("needChangePassword", loginResult.get("needChangePassword"));

            return Result.success("登录成功", result);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 修改密码
     *
     * @param request 修改密码请求，包含 oldPassword 和 newPassword
     * @param authorization 请求头中的 Authorization（Bearer Token）
     * @return 修改结果
     */
    @PostMapping("/change-password")
    public Result<Void> changePassword(@RequestBody Map<String, String> request,
                                       @RequestHeader(value = "Authorization", required = false) String authorization) {
        // 验证 Token
        String userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return Result.unauthorized();
        }

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        // 参数校验
        if (oldPassword == null || oldPassword.isEmpty()) {
            return Result.error("旧密码不能为空");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return Result.error("新密码不能为空");
        }
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            return Result.error("新密码长度应在6-20位之间");
        }

        try {
            userService.changePassword(userId, oldPassword, newPassword);
            return Result.success("密码修改成功", null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 账号申请
     *
     * @param request 申请请求，包含 username、realName、phone、email、department
     * @return 申请结果
     */
    @PostMapping("/apply")
    public Result<Void> applyAccount(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String realName = request.get("realName");
        String phone = request.get("phone");
        String email = request.get("email");
        String department = request.get("department");

        // 参数校验
        if (username == null || username.isEmpty()) {
            return Result.error("用户名不能为空");
        }
        if (realName == null || realName.isEmpty()) {
            return Result.error("真实姓名不能为空");
        }

        try {
            userService.applyAccount(username, realName, phone, email, department);
            return Result.success("申请已提交，请等待管理员审批", null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     *
     * @param authorization 请求头中的 Authorization（Bearer Token）
     * @return 用户信息
     */
    @GetMapping("/userinfo")
    public Result<Map<String, Object>> getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization) {
        // 验证 Token
        String userId = getUserIdFromToken(authorization);
        if (userId == null) {
            return Result.unauthorized();
        }

        try {
            User user = userService.getUserByUserId(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 构建用户信息（排除敏感字段）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", user.getUserId());
            userInfo.put("username", user.getUsername());
            userInfo.put("realName", user.getRealName());
            userInfo.put("phone", user.getPhone());
            userInfo.put("email", user.getEmail());
            userInfo.put("department", user.getDepartment());
            userInfo.put("status", user.getStatus());
            userInfo.put("needChangePassword", userService.isPasswordExpired(user));

            return Result.success(userInfo);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param authorization Authorization 请求头
     * @return 用户 ID，如果 Token 无效则返回 null
     */
    private String getUserIdFromToken(String authorization) {
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
