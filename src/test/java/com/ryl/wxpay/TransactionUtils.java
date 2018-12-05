package com.ryl.wxpay;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

public class TransactionUtils {

    //获取事物源
    private DataSourceTransactionManager manager;

    //开启事物
    public TransactionStatus begin(){
        TransactionStatus status = manager.getTransaction(new DefaultTransactionAttribute());
        return null;
    }

}
