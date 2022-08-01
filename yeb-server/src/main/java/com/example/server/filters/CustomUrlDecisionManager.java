package com.example.server.filters;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
//判断用户角色，实现权限控制
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
//        首先可以从configAttribute里面获得角色
        for (ConfigAttribute configAttribute:configAttributes){
            String needRole=configAttribute.getAttribute();
//            看看角色是不是ROLE_LOGIN
            if ("ROLE_LOGIN".equals(needRole)){
//                看一下有没有登陆
                if (authentication instanceof AnonymousAuthenticationToken){
//                    是这个Anonymous的话，一定没有登陆呢
                    throw new AccessDeniedException("请登录");
                }else {
                    return;
                }
            }else if ("NOTHING".equals(needRole)){
                return;
            }
//            判断现在的用户角色是否符合url请求
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority:authorities){
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
//        一个合适的都没有证明没有权限
        throw new AccessDeniedException("权限不足");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
