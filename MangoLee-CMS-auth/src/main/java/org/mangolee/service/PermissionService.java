package org.mangolee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    // 获取包括被逻辑删除的所有权限
    List<Permission> getAllPermissions();
}
