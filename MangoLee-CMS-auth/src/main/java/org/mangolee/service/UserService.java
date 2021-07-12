package org.mangolee.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> getAllUsers();
}
