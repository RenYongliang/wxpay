package com.ryl.wxpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
public class BuyCarController {

    @RequestMapping("/buy")
    public String addToCar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        String name = request.getParameter("name");
        HttpSession session = request.getSession();
        Map<String, Integer> map = (Map<String, Integer>) session.getAttribute("map");
        if(map==null) {
            map = new HashMap<>();
        }
        map.put(name,map.get(name)==null?1:map.get(name)+1);
        session.setAttribute("map",map);
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>商品名称</th>\n" +
                "                <th>数量</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n");
        for(String key:map.keySet()){
            sb.append("            <tr>\n" +
                    "                <td>"+key+"</td>\n" +
                    "                <td>"+map.get(key)+"</td>\n" +
                    "            </tr>\n");
        }
        sb.append("        </tbody>\n" +
                "    </table>\n" +
                "    <a href='/goods.html'>继续购物</a>");
        PrintWriter pw = response.getWriter();
        pw.write(sb.toString());
        pw.close();
        return "success";

    }

}
