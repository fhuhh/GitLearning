package com.example.server.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtils {
//    首先通过配置文件获取到过期时间和密钥字符串
    @Value("${jwt.secret}")
    private String JWT_SECRET_STRING;
    @Value("${jwt.expiration}")
    private Long JWT_EXPIRATION_TIME;

//    指定Claims中的key是什么
//    sub是固定的
   private final String SUB_KEY="sub";
   private final String CREATED_KEY="created";


//   生成token：建立Claims,然后通过jwt使用Claims生成token
    public String generateToken(UserDetails userDetails){
//        通过用户名生成Token
        Map<String,Object> claims=new HashMap<>();
        claims.put(SUB_KEY,userDetails.getUsername());
        claims.put(CREATED_KEY,new Date());
        return generateToken(claims);
    }
//    根据claims，使用jwt的builder来生成Token
    private String generateToken(Map<String,Object> claims){
        /*
        * 设置claims
        * 设置过期时间
        * 设置签名*/
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getExpiration())
                .signWith(SignatureAlgorithm.HS512,JWT_SECRET_STRING)
                .compact();
    }
//    获取过期时间
    private Date getExpiration(){
        return new Date(JWT_EXPIRATION_TIME*1000+System.currentTimeMillis());
    }

//    根据token来获取用户
    public String getUsernameFromToken(String token){
        /*
        * 首先parser获取Claims
        * Claims中存在用户名*/
        String username=null;
        Claims claims=getClaimsFromToken(token);
        try {
            username= claims.get(SUB_KEY,String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return username;
    }

//    获取jwt的Claims
    private Claims getClaimsFromToken(String token){
        /*
        * 设置密钥
        * 传入需要解析的token*/
        Claims claims=null;
        try {
            claims=Jwts.parser()
                    .setSigningKey(JWT_SECRET_STRING)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return claims;
    }
//   确定是否可以刷新
    public boolean canRefresh(String token){
//        主要就是看有没有过期
//        通过claims来判断就行了
        return tokenNotExpired(token);
    }

    public boolean tokenNotExpired(String token){
        Date expireDate=getExpiredDateFromToken(token);
        return !expireDate.before(new Date());
    }
//    获取过期Date
    private Date getExpiredDateFromToken(String token){
        Claims claims=getClaimsFromToken(token);
        return claims.getExpiration();
    }
//    刷新Token
    public String refreshToken(String token){
        Claims claims=getClaimsFromToken(token);
        claims.put(CREATED_KEY,new Date());
        return generateToken(claims);
    }
//    判断token是不是有效
    public boolean validateToken(String token,UserDetails userDetails){
        /*
        * 用户名要一样
        * token不能过期
        * */
        String username=userDetails.getUsername();
        return username.equals(getUsernameFromToken(token))&& tokenNotExpired(token);
    }
}
