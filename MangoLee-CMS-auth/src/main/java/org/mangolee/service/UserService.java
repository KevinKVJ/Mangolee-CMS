package org.mangolee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    // 获取包括被逻辑删除的所有用户
    List<User> getAllUsers();

    // 按主键ID查找条目 忽略逻辑删除
    User getUserByIdIgnoreLogicalDeletion(Long id);

    // 按主键ID进行物理删除
    void physicalDeleteById(Long id);

    // 更新所有指定role为null
    void updateRoleBatchWithNull(String role);
}
