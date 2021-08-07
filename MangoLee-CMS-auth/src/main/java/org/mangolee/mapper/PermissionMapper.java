package org.mangolee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.mangolee.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    // 获取所有权限 忽略逻辑删除
    @Select("select * from mangolee.permission;")
    List<Permission> getAllPermissions();

    // 按主键ID获取role 忽略逻辑删除
    @Select("select * from mangolee.permission where id = #{id} limit 1;")
    Permission getPermissionByIdIgnoreLogicalDeletion(Long id);

    // 按主键ID进行物理删除
    @Delete("delete from mangolee.permission where id = #{id};")
    void physicalDeleteById(Long id);
}
