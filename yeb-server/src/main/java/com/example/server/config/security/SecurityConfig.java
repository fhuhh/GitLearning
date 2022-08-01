package com.example.server.config.security;

import com.example.server.filters.CustomFilter;
import com.example.server.filters.CustomUrlDecisionManager;
import com.example.server.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomFilter customFilter;
    private final CustomUrlDecisionManager decisionManager;

    //    配置加密器
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //    配置AuthenticationManagerBuilder---新版会自动识别encoder和userDetailService
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Autowired
    public SecurityConfig(AuthenticationEntryPoint authenticationEntryPoint
            ,AccessDeniedHandler accessDeniedHandler
            ,JwtTokenFilter jwtTokenFilter
            ,CustomFilter customFilter
            ,CustomUrlDecisionManager decisionManager){
        this.authenticationEntryPoint=authenticationEntryPoint;
        this.accessDeniedHandler=accessDeniedHandler;
        this.jwtTokenFilter=jwtTokenFilter;
        this.customFilter=customFilter;
        this.decisionManager=decisionManager;
    }



//    配置httpSecurity
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/system/admin/login","/system/admin/logout","/captcha/get")
                .permitAll()
                .anyRequest()
                .authenticated()
//                动态权限拦截器,泛型必须是FilterSecurityInterceptor
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(decisionManager);
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })
                .and()
//                关闭默认的头部缓存策略
                .headers()
                .cacheControl()
                .disable()
                .and()
//                登录jwt过滤器,由于标注为了Component,所以不需要在这个类注入了,构造方法AutoWire就好了
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                未授权未登录处理
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().build();

    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()
                .antMatchers(
                        "/images/**"
                        , "/js/**"
                        , "/webjars/**"
                        ,"/doc.html",
                        "/swagger-resources/**",
                        "/v2/api-docs/**"
                        ,"/ws/**"
                        ,"chat/**");
    }

}
