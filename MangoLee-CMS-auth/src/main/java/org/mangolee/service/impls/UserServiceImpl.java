package org.mangolee.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.User;
import org.mangolee.mapper.UserMapper;
import org.mangolee.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    UserMapper userMapper;
    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
}
