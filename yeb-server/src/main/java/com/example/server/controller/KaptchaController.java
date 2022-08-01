package com.example.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/captcha")
@Api("/captcha")
public class KaptchaController {
    private final DefaultKaptcha defaultKaptcha;
    @Autowired
    public KaptchaController(DefaultKaptcha defaultKaptcha){
        this.defaultKaptcha=defaultKaptcha;
    }
    @ApiOperation(value = "获得验证码",produces = "image/jpeg")
    @GetMapping("/get")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
//        首先因为传给前端的是图片，所以response的内容要稍加修改
        /*
          1.设置类型为jpeg
          2.设置no-cache策略
          3.设置Expires为0*/
        response.setDateHeader("Expires",0);
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control","no-store, no-cache, must-revalidate");

        /*
        * 生成验证码*/
//        获取验证码的文本内容
        String text=defaultKaptcha.createText();
//        在控制台输出一下
        System.out.println("验证码:"+text);
//        验证码放到Session中去
        request.getSession().setAttribute("captcha",text);
//        根据文本内容创建验证码图片
        BufferedImage image=defaultKaptcha.createImage(text);
        ServletOutputStream outputStream=null;
        try {
            outputStream=response.getOutputStream();
//            输出图片到前端去
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
//            关闭输出流
            if (outputStream!=null){
                try {
                    outputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
