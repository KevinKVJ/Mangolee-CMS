package org.mangolee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.mangolee.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    // 获取所有条目 忽略逻辑删除
    @Select("select * from mangolee.user;")
    List<User> getAllUsers();
}