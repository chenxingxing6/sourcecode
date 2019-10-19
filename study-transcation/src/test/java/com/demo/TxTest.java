package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.entity.Log;
import com.demo.entity.User;
import com.demo.service.ILogService;
import com.demo.service.IUserService;
import com.demo.txtest.ITxService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/10/19 14:30
 * Desc: 事务测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TranscationApplication.class)
public class TxTest {
    @Resource
    private ITxService txService;

    @Test
    public void test01(){
        txService.notxException_required_required();
    }

    @Test
    public void test02(){
        txService.notx_required_requiredException();
    }

    @Test
    public void test03(){
        txService.txException_required_required();
    }

    @Test
    public void test04(){
        txService.txException_requiredNew_requiredNew();
    }

    @Test
    public void test05(){
        txService.txException_requiredNew_requiredNewException();
    }


    @Test
    public void test06(){
        txService.txException_required_requiredExceptionTry();
    }
}
