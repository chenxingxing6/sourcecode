package com.demo.service;

import com.demo.entity.Log;

/**
 * User: lanxinghua
 * Date: 2019/10/19 14:48
 * Desc:
 */
public interface ILogService {
    int deleteByPrimaryKey(String id);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKey(Log record);
}
