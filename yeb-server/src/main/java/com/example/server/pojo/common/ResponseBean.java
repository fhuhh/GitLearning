package com.example.server.pojo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResponseBean {
    private int code;
    private String message;
    private Object obj;
//    本身的obj就是给前端发送的东西
    public static ResponseBean success(String message){
        return new ResponseBean(200,message,null);
    }

    public static ResponseBean success(String message,Object obj){
        return new ResponseBean(200,message,obj);
    }

    public static ResponseBean error(String message){
        return new ResponseBean(500,message,null);
    }

    public static ResponseBean error(String message,Object obj){
        return new ResponseBean(500,message,obj);
    }

}
