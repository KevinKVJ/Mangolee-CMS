package org.mangolee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.mangolee.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends BaseMapper<User> {
    // 获取所有条目 忽略逻辑删除
    @Select("select * from mangolee.user;")
    List<User> getAllUsers();
    // 按主键ID进行物理删除
    @Delete("delete from mangolee.user where id = #{id};")
    void physicalDeleteById(Long id);
    // 按主键ID查询用户 忽略逻辑删除
    @Select("select * from mangolee.user where id = #{id};")
    User getUserByIdIgnoreLogicalDeletion(Long id);
}
