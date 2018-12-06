package com.ryl.wxpay.dao.bean;

public class Base_info {

    //商户logo,建议300*300
    private String logo_url;
    //码型
    private String code_type;
    //商户名字
    private String brand_name;
    //卡券名称
    private String title;
    //券颜色
    private String color;
    //卡券使用提醒
    private String notice;
    //卡券使用说明
    private String discription;
    //商品信息
    private Sku sku;
    //卡券库存数量
    private int quantity;
    //使用有效日期
    private Date_info date_info;
    //使用时间的类型
    private String type;
    //

}
