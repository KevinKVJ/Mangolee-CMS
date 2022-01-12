package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.Permission;
import org.mangolee.mapper.PermissionMapper;
import org.mangolee.service.PermissionService;
import org.springframework.stereotype.Service;

@Service("permissionService")
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
}
