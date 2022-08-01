package com.example.server.filters;

import com.example.server.pojo.Menu;
import com.example.server.pojo.Role;
import com.example.server.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

//根据url请求判断哪些角色可以通过,加入SecurityConfig
@Component
public class CustomFilter implements FilterInvocationSecurityMetadataSource {
    private final MenuService menuService;
    private final AntPathMatcher antPathMatcher;
    @Autowired
    public CustomFilter(MenuService menuService){
        this.menuService=menuService;
        this.antPathMatcher=new AntPathMatcher();
    }
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
//        获取url
        String requestUrl=((FilterInvocation) object).getRequestUrl();
        if (antPathMatcher.match("/admin/log**",requestUrl)||antPathMatcher.match("/captcha/**",requestUrl)){
            return SecurityConfig.createList("NOTHING");
        }
        List<Menu> menuList=menuService.getMenusByRoles();
        for (Menu menu:menuList){
            if (antPathMatcher.match(menu.getUrl(),requestUrl)){
                String[] strings=menu.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new);
                return SecurityConfig.createList(strings);
            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
