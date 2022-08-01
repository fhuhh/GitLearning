package com.example.server.config.websocket;

import com.example.server.config.jwt.JwtTokenUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
//配置WebSocket需要加这个
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService userDetailsService;
    @Autowired
    public WebSocketConfig(JwtTokenUtils jwtTokenUtils
            ,UserDetailsService userDetailsService){
        this.jwtTokenUtils=jwtTokenUtils;
        this.userDetailsService=userDetailsService;
    }


    @Override
//    添加Endpoint，让前端链接到后端WebSocket服务
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
//                端点固定/ws/ep
                .addEndpoint("/ws/ep")
//                允许跨域
                .setAllowedOrigins("*")
//                支持socketJS
                .withSockJS();
    }

    @Override
//    当有JWT令牌时需要配置
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
//                获取头存取器
                StompHeaderAccessor accessor= MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
//                获取到token
                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())){
//                    headerName实际上是前端规定的
                    String token= accessor.getFirstNativeHeader("Auth-Token");
                    if (StringUtils.hasText(token)){
                        String authToken = token.substring(tokenHead.length());
                        String username=jwtTokenUtils.getUsernameFromToken(authToken);
                        if (StringUtils.hasText(username)){
//                            有这个用户的话
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                            需要看一下token有没有过期
                            if (jwtTokenUtils.validateToken(authToken,userDetails)){
                                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                                头存取器需要设置登录用户
                                accessor.setUser(authenticationToken);
                            }
                        }
                    }
                }
                return message;
            }
        });
    }

    @Override
//    配置消息代理
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        说白了就是往哪发消息,代理前缀就是参数
        registry.enableSimpleBroker("/queue");
    }
}
