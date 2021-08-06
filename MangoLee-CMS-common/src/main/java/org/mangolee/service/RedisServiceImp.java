package org.mangolee.service;

import org.mangolee.utils.Result;

public class RedisServiceImp implements RedisService{
    @Override
    public Result verify(String token) {
        return new Result(200,"Success","abc");
    }
}
