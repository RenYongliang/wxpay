package com.ryl.wxpay.service.impl;

import com.google.gson.Gson;
import com.ryl.wxpay.common.miniprogram.HttpUtil;
import com.ryl.wxpay.common.miniprogram.WxPayConfig;
import com.ryl.wxpay.service.CardService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@Service
public class CardServiceImpl implements CardService {


    @Override
    public String getAccessToken(String appID, String secret) {
        String param = "grant_type=client_credential"+"&appID="+appID+"&secret="+secret;
        String accessTokenJson = HttpUtil.sendGet(WxPayConfig.access_token_url, param);//获取access_token
        Gson gson = new Gson();
        Map<String,String> map = gson.fromJson(accessTokenJson, Map.class);
        return map.get("access_token");
    }

    @Override
    public String getLogoUrl(String access_token, String filePath) throws Exception {
        String reqUrl = WxPayConfig.upload_img_url + "?access_token=" + access_token + "&type=image";
        URL url = new URL(reqUrl);
        String result = null;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new IOException("上传的文件不存在");
        }
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
                + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());

        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件以流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;

        // 定义BufferedReader输入流来读取URL的响应
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        if (result == null) {
            result = buffer.toString();
        }
        return result;
    }

    @Override
    public String getCardID(String access_token, String data) throws IOException {
        String reqUrl = WxPayConfig.card_info_url + "?access_token=" + access_token;
        String result;
        try(
            BufferedReader reader = new BufferedReader(new FileReader("card.txt"))
        ){
            String line;
            StringBuffer sb = new StringBuffer();
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            result = sb.toString();
        }
        String res = HttpUtil.postData(reqUrl, result,"application/json");
        return res;
    }

    @Override
    public String getTicket(String access_token, String data) throws IOException {
        String reqUrl = WxPayConfig.ticket_url + "?access_token=" + access_token;
        String result;
        try(
                BufferedReader reader = new BufferedReader(new FileReader("qrcode.txt"))
        ){
            String line;
            StringBuffer sb = new StringBuffer();
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            result = sb.toString();
        }
        String res = HttpUtil.postData(reqUrl, result,"application/json");
        return res;
    }

    @Override
    public InputStream getQRCode(String ticket) throws Exception {
        String reqUrl = WxPayConfig.qrcode_url + "?ticket=" + ticket;
        URL realUrl = new URL(reqUrl);
        URLConnection connection = realUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
        InputStream in = connection.getInputStream();
        return in;
    }

    @Override
    public String submitVipInfo(String access_token, String data) throws IOException {
        String reqUrl = WxPayConfig.vip_info_url + "?access_token=" + access_token;
        String result;
        try(
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream("vipinfo.txt"));
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ){
            int b;
            while((b=bis.read())!=-1){
                baos.write(b);
            }
            result = baos.toString();
        }
        String res = HttpUtil.postData(reqUrl, result,"application/json");
        return res;
    }


}
