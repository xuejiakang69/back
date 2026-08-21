package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据 ID 查询用户
     */
    User selectById(@Param("id") Long id);

    /**
     * 根据用户唯一标识查询用户
     */
    User selectByUserId(@Param("userId") String userId);

    /**
     * 根据用户名查询用户
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 查询所有用户
     */
    List<User> selectAll();

    /**
     * 根据状态查询用户列表
     */
    List<User> selectByStatus(@Param("status") String status);

    /**
     * 插入用户
     */
    int insert(User user);

    /**
     * 更新用户
     */
    int update(User user);

    /**
     * 更新用户密码
     */
    int updatePassword(@Param("userId") String userId, @Param("password") String password, @Param("passwordUpdatedAt") LocalDateTime passwordUpdatedAt);

    /**
     * 更新用户状态
     */
    int updateStatus(@Param("userId") String userId, @Param("status") String status);

    /**
     * 根据 ID 删除用户
     */
    int deleteById(@Param("id") Long id);
}
