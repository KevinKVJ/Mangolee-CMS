package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.Permission;
import org.mangolee.mapper.PermissionMapper;
import org.mangolee.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    PermissionMapper permissionMapper;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionMapper.getAllPermissions();
    }

    @Override
    public Permission getPermissionByIdIgnoreLogicalDeletion(Long id) {
        return permissionMapper.getPermissionByIdIgnoreLogicalDeletion(id);
    }

    @Override
    public void physicalDeleteById(Long id) {
        permissionMapper.physicalDeleteById(id);
    }
}
