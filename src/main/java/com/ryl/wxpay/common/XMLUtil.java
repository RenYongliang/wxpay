package com.ryl.wxpay.common;

import org.apache.commons.codec.digest.DigestUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLUtil {
	private static String text;

	public static Map doXMLParse(String strxml) throws JDOMException, IOException {  
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");  

        if(null == strxml || "".equals(strxml)) {  
            return null;  
        }  
        Map m = new HashMap();  
        InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));  
        SAXBuilder builder = new SAXBuilder();  
        Document doc = builder.build(in);  
        Element root = doc.getRootElement();  
        List list = root.getChildren();  
        Iterator it = list.iterator();  
        while(it.hasNext()) {  
            Element e = (Element) it.next();  
            String k = e.getName();  
            String v = "";  
            List children = e.getChildren();  
            if(children.isEmpty()) {  
                v = e.getTextNormalize();  
            } else {  
                v = XMLUtil.getChildrenText(children);  
            }  
            m.put(k, v);  
        }  
        //关闭流  
        in.close();  
        return m;  
    }  

    /** 
     * 获取子结点的xml 
     * @param children 
     * @return String 
     */  
    public static String getChildrenText(List children) {  
        StringBuffer sb = new StringBuffer();  
        if(!children.isEmpty()) {  
            Iterator it = children.iterator();  
            while(it.hasNext()) {  
                Element e = (Element) it.next();  
                String name = e.getName();  
                String value = e.getTextNormalize();  
                List list = e.getChildren();  
                sb.append("<" + name + ">");  
                if(!list.isEmpty()) {  
                    sb.append(XMLUtil.getChildrenText(list));  
                }  
                sb.append(value);  
                sb.append("</" + name + ">");  
            }  
        }  

        return sb.toString();  
    }  
    
    public static Map<String, String> paraFilter(Map<String, String> sArray) {     
        Map<String, String> result = new HashMap<String, String>();     
        if (sArray == null || sArray.size() <= 0) {     
            return result;     
        }     
        for (String key : sArray.keySet()) {     
            String value = sArray.get(key);     
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")     
                    || key.equalsIgnoreCase("sign_type")) {     
                continue;     
            }     
            result.put(key, value);     
        }     
        return result;     
    }    
    /**   
     * 签名字符串   
     * @param text 需要签名的字符串
     * @param key 密钥   
     * @param input_charset 编码格式
     * @return 签名结果   
     */     
    public static String sign(String text, String key, String input_charset) {     
        text = text + "&key=" + key;     
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));     
    }     
    /**   
     * 签名字符串   
     *  @param text 需要签名的字符串
     * @param sign 签名结果   
     * @param key 密钥
     * @param input_charset 编码格式   
     * @return 签名结果   
     */
    public static boolean verify(Map<String, String> validParams, String sign, String key, String input_charset) {
		text = text + key;     
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));     
        if (mysign.equals(sign)) {     
            return true;     
        } else {     
            return false;     
        }     
	}     
    
   /* public static boolean verifys(String text, String sign, String key, String input_charset) {     
        text = text + key;     
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));     
        if (mysign.equals(sign)) {     
            return true;     
        } else {     
            return false;     
        }     
    }     */
    public static byte[] getContentBytes(String content, String charset) {     
        if (charset == null || "".equals(charset)) {     
            return content.getBytes();     
        }     
        try {     
            return content.getBytes(charset);     
        } catch (UnsupportedEncodingException e) {     
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);     
        }     
    }

	
}
