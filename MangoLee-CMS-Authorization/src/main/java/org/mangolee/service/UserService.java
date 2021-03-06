package org.mangolee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.User;

public interface UserService extends IService<User> {
    // 更新所有指定role为null
    void updateRoleBatchWithNull(String role);
}
