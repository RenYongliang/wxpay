package com.ryl.wxpay.controller;

import com.google.gson.Gson;
import com.ryl.wxpay.common.miniprogram.HttpUtil;
import com.ryl.wxpay.common.miniprogram.WxPayConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {

    @RequestMapping("/create")
    public String createCard() throws IOException {
        String appID = "wxe0dfc47d71fce43b";
        String secret = "afc2f9ff14f49583dd421d2c3b77bed6";
        String param = "grant_type=client_credential"+"&appID="+appID+"&secret="+secret;
        String accessTokenJson = HttpUtil.sendGet(WxPayConfig.access_token_url, param);//获取access_token
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(accessTokenJson, Map.class);
        String access_token = map.get("access_token");
        Map<String,Object> map2 = new HashMap<>();
        File buffer = new File("ipad.png");
        map2.put("access_token",access_token);
        map2.put("buffer",buffer);
        String json = gson.toJson(map2);
        String url = HttpUtil.postData(WxPayConfig.upload_img_url,json,"application/json");

        return url;

    }
}
