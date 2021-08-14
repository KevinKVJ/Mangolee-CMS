package org.mangolee.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.mangolee.entity.Log;

public interface LogService extends IService<Log> {
    Log getLogByIdIgnoreLogicalDeletion(Long id);
    void physicalDeleteById(Long id);
}
