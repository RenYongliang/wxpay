package com.ryl.wxpay.controller;

import com.google.gson.Gson;
import com.ryl.wxpay.common.HttpUtil;
import com.ryl.wxpay.common.PayCommonUtil;
import com.ryl.wxpay.common.WxPayConfig;
import com.ryl.wxpay.common.XMLUtil;
import org.jdom.JDOMException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


@RestController
public class WxPayController {

    @RequestMapping("/orderPay")
    public void doOrder(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String title = "充值"; //订单标题
        String times = System.currentTimeMillis()+""; //时间戳

        SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
        packageParams.put("appid", "wxb84c40fe7c409322");//服务商ID
        packageParams.put("sub_appid","wx148a11668cd7115e");//小程序appid
        packageParams.put("mch_id", "1501519031");//商户号
        packageParams.put("sub_mch_id","1517155971");//子服务商户id
        packageParams.put("nonce_str", times);//时间戳
        packageParams.put("body", title);//支付主体
        packageParams.put("out_trade_no", "ryl201811231555");//订单号
        packageParams.put("total_fee", 1);//价格
        packageParams.put("notify_url","https://api.cangbaotuapp.com");//支付回调
        packageParams.put("sub_openid", "os3EQ5aLkuW5phGmnSPFxWKsifFs");//openid
        packageParams.put("trade_type", "JSAPI");//这个api有，固定的

        //获取sign
        String sign = PayCommonUtil.createSign("UTF-8", packageParams,"rcxpy2u00vshhbgaxgxuiqi5ao34eap2");//微信支付的商户密钥 appid.getMch_key()
        packageParams.put("sign", sign);

        String requestXML = PayCommonUtil.getRequestXml(packageParams); //转成XML
        System.out.println(requestXML);

        String resXml = HttpUtil.postData(WxPayConfig.pay_url, requestXML);//得到含有prepay_id的XML
        System.out.println(resXml);

        Map map = XMLUtil.doXMLParse(resXml);//解析XML存入Map
        System.out.println(map);

        String prepay_id = (String) map.get("prepay_id");//得到prepay_id
        SortedMap<Object, Object> packageP = new TreeMap<Object, Object>();
        packageP.put("appId","wx148a11668cd7115e");//appId,上面是appid   appid.getAppid()
        packageP.put("nonceStr", times);//时间戳
        packageP.put("package", "prepay_id="+prepay_id);//必须把package写成 "prepay_id="+prepay_id这种形式
        packageP.put("signType", "MD5");//paySign加密
        packageP.put("timeStamp", (System.currentTimeMillis() / 1000) + "");
        String paySign = PayCommonUtil.createSign("UTF-8", packageP, "rcxpy2u00vshhbgaxgxuiqi5ao34eap2");//得到paySign
        packageP.put("paySign", paySign);
        //将packageP数据返回给小程序
        Gson gson = new Gson();
        String json = gson.toJson(packageP);
        PrintWriter pw = response.getWriter();
        System.out.println(json);
        pw.write(json);
        pw.close();

    }

    /**
     * 回调
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/wxNotify")
    @ResponseBody
    public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while((line = br.readLine()) != null){
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        System.out.println("接收到的报文：" + notityXml);
        Map map = XMLUtil.doXMLParse(notityXml);
        String returnCode = (String) map.get("return_code");
        if("SUCCESS".equals(returnCode)){
            //验证签名是否正确
            Map<String, String> validParams = XMLUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
            //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
            if(XMLUtil.verify(validParams, (String)map.get("sign"),"utf-8", returnCode)){
                /**此处添加自己的业务逻辑代码start**/
                String orderCode = request.getParameter("orderCode");
                /**此处添加自己的业务逻辑代码end**/
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        }else{
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"  + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        System.out.println(resXml);
        System.out.println("微信支付回调数据结束");
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }
}
