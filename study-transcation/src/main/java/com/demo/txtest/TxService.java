package com.demo.txtest;

import com.demo.entity.User;
import com.demo.service.IUserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/10/19 15:10
 * Desc:
 */
@Component
public class TxService implements ITxService {
    @Resource
    private IUserService userService;
    private static User user1 = new User();
    private static User user2 = new User();

    static {
        user1.setId("1");
        user1.setName("用户1");
        user1.setIsValid(Short.valueOf("1"));

        user2.setId("2");
        user2.setName("用户2");
        user2.setIsValid(Short.valueOf("1"));
    }

    // 添加成功：user1,user2
    public void notxException_required_required() {
        userService.addRequired(user1);
        userService.addRequired(user2);
        throw new RuntimeException();
    }

    // 添加成功：user1
    public void notx_required_requiredException() {
        userService.addRequired(user1);
        userService.addRequiredException(user2);
    }

    // 全回滚
    @Transactional
    public void txException_required_required() {
        userService.addRequired(user1);
        userService.addRequired(user2);
        throw new RuntimeException();
    }

    // 添加成功：user1，user2
    @Transactional
    public void txException_requiredNew_requiredNew() {
        userService.addRequiredNew(user1);
        userService.addRequiredNew(user2);
        throw new RuntimeException();
    }

    // 添加成功：user1
    @Transactional
    public void txException_requiredNew_requiredNewException() {
        userService.addRequiredNew(user1);
        userService.addRequiredNewException(user2);
        throw new RuntimeException();
    }

    // 添加成功：user1
    @Transactional
    public void txException_required_requiredExceptionTry() {
        userService.addRequired(user1);
        try {
            userService.addRequiredNewException(user2);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
