package com.demo.service;

import com.demo.entity.User;

/**
 * User: lanxinghua
 * Date: 2019/10/19 14:24
 * Desc:
 */
public interface IUserService {
    public void addRequired(User user);
    public void addRequiredException(User user);
    public void addRequiredNew(User user);
    public void addRequiredNewException(User user);
}
