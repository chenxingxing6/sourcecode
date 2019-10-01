package com;

/**
 * @Author: cxx
 * @Date: 2019/10/1 22:40
 */
public class UserService implements IUserService {
    public void delete(String id) {
        System.out.println("删除用户 id:" + id);
    }
}
