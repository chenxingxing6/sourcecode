package com.demo.service.impl;

import com.demo.entity.Log;
import com.demo.mapper.LogMapper;
import com.demo.service.ILogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/10/19 14:48
 * Desc:
 */
@Service
public class LogService implements ILogService {
    @Resource
    private LogMapper logMapper;

    public int deleteByPrimaryKey(String id) {
        return logMapper.deleteByPrimaryKey(id);
    }

    public int insert(Log record) {
        return logMapper.insert(record);
    }

    public int insertSelective(Log record) {
        return logMapper.insertSelective(record);
    }

    public Log selectByPrimaryKey(String id) {
        return logMapper.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(Log record) {
        return logMapper.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(Log record) {
        return logMapper.updateByPrimaryKey(record);
    }
}
