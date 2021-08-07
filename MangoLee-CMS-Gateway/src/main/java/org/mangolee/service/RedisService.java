package org.mangolee.service;

import org.mangolee.entity.Result;
import org.mangolee.entity.UserInfo;

public interface RedisService {
    Result<UserInfo> verify(String token);
}
