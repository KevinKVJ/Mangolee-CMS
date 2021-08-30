package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.Log;
import org.mangolee.mapper.LogMapper;
import org.mangolee.service.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("logService")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Resource
    LogMapper logMapper;

    @Override
    public Log getLogByIdIgnoreLogicalDeletion(Long id) {
        return logMapper.getLogByIdIgnoreLogicalDeletion(id);
    }

    @Override
    public void physicalDeleteById(Long id) {
        logMapper.physicalDeleteById(id);
    }
}
