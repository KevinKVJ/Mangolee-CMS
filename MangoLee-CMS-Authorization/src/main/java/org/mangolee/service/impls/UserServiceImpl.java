package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.User;
import org.mangolee.mapper.UserMapper;
import org.mangolee.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public void updateRoleBatchWithNull(String role) {
        userMapper.updateRoleBatchWithNull(role);
    }
}
