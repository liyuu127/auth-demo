package cn.liyu.log.service;

import cn.liyu.log.entity.SysLog;
import cn.liyu.log.mapper.SysLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysLogService {

    @Autowired
    SysLogMapper logMapper;

    public void save(SysLog sysLog) {
        logMapper.insert(sysLog);
    }
}
