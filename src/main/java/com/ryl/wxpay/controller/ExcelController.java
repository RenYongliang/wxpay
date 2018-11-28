package com.ryl.wxpay.controller;

import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.OutputStream;

@Controller
public class ExcelController {

    public void importExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition","attachment;filename=temp.xls");
        response.setContentType("application/msexcel");

        //创建可写入的Excel工作薄，且内容将写入到输出流，并通过输出流输出给客户端浏览
        WritableWorkbook wk = Workbook.createWorkbook(os);
    }
}
