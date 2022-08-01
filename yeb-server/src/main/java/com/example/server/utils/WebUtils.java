package com.example.server.utils;

import com.example.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

public class WebUtils {
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
