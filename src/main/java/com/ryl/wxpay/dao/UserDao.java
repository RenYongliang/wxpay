package com.ryl.wxpay.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void add(String username,String password){
        String sql = "insert into user(username,password)values(?,?)";
        int updateResult = jdbcTemplate.update(sql,username,password);
        System.out.println("updateResult:" + updateResult);
    }
}
