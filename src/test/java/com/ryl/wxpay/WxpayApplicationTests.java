package com.ryl.wxpay;

import com.ryl.wxpay.service.UserService;
import com.ryl.wxpay.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WxpayApplicationTests {

    @Autowired
    UserServiceImpl service;

    @Test
    public void contextLoads() {
        service.add();
    }

}
