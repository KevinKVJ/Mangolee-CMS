package org.mangolee.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import org.mangolee.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
    // 更新所有指定role为null
    @Update("update mangolee.user set role = null where role = #{role};")
    void updateRoleBatchWithNull(String role);
}
