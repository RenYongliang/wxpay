package com.ryl.wxpay.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public interface CardService {

    String getAccessToken(String appID,String secret);
    String getLogoUrl(String access_token,String filepath) throws Exception;
    String getCardID(String access_token,String data) throws IOException;
    String getTicket(String access_token,String data) throws IOException;
    InputStream getQRCode(String ticket) throws Exception;






}
