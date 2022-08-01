package com.example.server.filters;

import com.example.server.config.jwt.JwtTokenUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;
    @Autowired
    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils,
                          UserDetailsService userDetailsService){
        this.jwtTokenUtils=jwtTokenUtils;
        this.userDetailsService=userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader(this.tokenHeader);
//        检查token的合法性
        if (null!=tokenHeader&&tokenHeader.startsWith(this.tokenHead)){
            String token=tokenHeader.substring(tokenHead.length()+1);
            String username=jwtTokenUtils.getUsernameFromToken(token);
//            用户名要能拿到，而且securityContextHolder必须也存在着
            if (null!=username&&null== SecurityContextHolder.getContext().getAuthentication()){
//                未登录状态,使用loadUserByUsername直接登录
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
//                看一下token是不是有效的
                if (jwtTokenUtils.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                    下面的这句话是为了Session搞得，但是基于token的server又不需要Session，所以没有啥用
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
