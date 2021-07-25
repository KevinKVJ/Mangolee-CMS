package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.Userpermission;
import org.mangolee.mapper.UserpermissionMapper;
import org.mangolee.service.UserpermissionService;
import org.springframework.stereotype.Service;

@Service("userpermissionService")
public class UserpermissionServiceImpl extends ServiceImpl<UserpermissionMapper, Userpermission> implements UserpermissionService {
    
}
