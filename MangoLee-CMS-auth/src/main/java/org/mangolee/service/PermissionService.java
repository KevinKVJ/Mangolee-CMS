package org.mangolee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    // 获取包括被逻辑删除的所有用户
    List<Permission> getAllPermissions();
    // 按主键ID进行物理删除
    void physicalDeleteById(Long id);
    // 按权限名称进行物理删除
    void physicalDeleteByRole(String role);
}
