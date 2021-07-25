package org.mangolee.service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mangolee.entity.User;
import org.mangolee.mapper.UserMapper;
import org.mangolee.model.MyUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;

public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        //根据用户名去数据库查询 where username = ?
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        //判断
        if (user == null) {//数据库没有该用户名，认证失败
            throw new UsernameNotFoundException("用户名不存在");
        }
        return new MyUserDetails(user.getUsername(), user.getPassword(), user.getDeleted() != 1);
    }
}
