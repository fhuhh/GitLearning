package com.example.server.config.security;

import com.example.server.config.constants.WebResponseConstants;
import com.example.server.pojo.common.ResponseBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//        设置编码
        response.setCharacterEncoding("utf-8");
//        设置json
        response.setContentType("application/json");
//        获得printWriter
        PrintWriter out=response.getWriter();
        ResponseBean bean=ResponseBean.error("未登录！").setCode(WebResponseConstants.ACCESS_DENIED.getCode());
//        利用ObjectMapper把对象转化为String
        out.write(new ObjectMapper().writeValueAsString(bean));
        out.flush();
        out.close();
    }
}
