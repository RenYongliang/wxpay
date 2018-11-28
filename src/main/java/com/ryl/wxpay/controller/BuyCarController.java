package com.ryl.wxpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class BuyCarController {

    @RequestMapping("/buy")
    public void test(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("username","ryl");
        HttpSession session = request.getSession();
        session.setAttribute("password",123);
        response.addCookie(cookie);
        response.getWriter().write("HttpServlet");

    }
}
