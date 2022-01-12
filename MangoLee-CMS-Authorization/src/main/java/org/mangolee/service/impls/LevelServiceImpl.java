package org.mangolee.service.impls;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mangolee.entity.Level;
import org.mangolee.mapper.LevelMapper;
import org.mangolee.service.LevelService;
import org.springframework.stereotype.Service;

@Service("levelService")
public class LevelServiceImpl extends ServiceImpl<LevelMapper, Level> implements LevelService {
}

