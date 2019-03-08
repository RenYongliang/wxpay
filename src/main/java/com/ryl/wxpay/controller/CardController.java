package com.ryl.wxpay.controller;

import com.google.gson.Gson;
import com.ryl.wxpay.service.impl.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping("/card")
public class CardController {

//    final String appID = "wxe0dfc47d71fce43b";
//    final String secret = "afc2f9ff14f49583dd421d2c3b77bed6";

    final String appID = "wx3235fc1930f3781a";
    final String secret = "2421a04bbe12094e398ce02cc1a73054";

    @Autowired
    CardServiceImpl cardService;

    @RequestMapping("/create")
    public String createCard(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String access_token = cardService.getAccessToken(appID,secret);
        //String url = cardService.getLogoUrl(access_token,"ipad.png");
        //String res = cardService.getCardID(access_token,"abc");
        String ticketJson = cardService.getTicket(access_token,"abc");
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(ticketJson,Map.class);
        String ticket = map.get("ticket");
        try(
            InputStream in = cardService.getQRCode(ticket);
            OutputStream out = response.getOutputStream()
        ){
            byte[] b = new byte[1024];
            int len;
            while((len=in.read(b))!=-1){
                out.write(b,0,len);
            }
        }
        String cardID = cardService.getCardID(access_token,"aaa");
        return null;
    }

    @RequestMapping("/submit")
    public String submit() throws IOException {
        String access_token = cardService.getAccessToken(appID,secret);

        String res = cardService.submitVipInfo(access_token,"aaa");
        return res;
    }


}
