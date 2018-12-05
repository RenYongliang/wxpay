package com.ryl.wxpay.common.miniprogram;

public class WxPayConfig {
    //支付成功后的服务器回调url  
    public static final String notify_url = "https://ff.hbg168.com/wxpay/wxNotify";  
    //签名方式，固定值  
    public static final String SIGNTYPE = "MD5";  
    //交易类型，小程序支付的固定值为JSAPI  
    public static final String TRADETYPE = "JSAPI";  
    //微信统一下单接口地址  
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信获取access_token接口
    public static final String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
    
}  

