package com.ryl.wxpay.service.impl;

import com.ryl.wxpay.dao.UserDao;
import com.ryl.wxpay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;


    @Override
    public void add() {
        userDao.add("test01","123456");
//        int i = 1/0;
        System.out.println("###############");
        userDao.add("test02","456789");
    }
}
