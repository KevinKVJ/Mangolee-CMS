package org.mangolee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.mangolee.entity.Log;
import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper extends BaseMapper<Log> {
    // 按主键ID获取Log 忽略逻辑删除
    @Select("select * from mangolee.log where id = #{id} limit 1;")
    Log getLogByIdIgnoreLogicalDeletion(Long id);
    // 按主键ID进行物理删除
    @Delete("delete from mangolee.log where id = #{id};")
    void physicalDeleteById(Long id);
}
