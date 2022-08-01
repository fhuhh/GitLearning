package com.example.server.config.constants;

public enum WebResponseConstants {
    NEED_LOGIN(401),ACCESS_DENIED(403);
    private final int code;
    WebResponseConstants(int code){
        this.code=code;
    }
    public int getCode() {
        return code;
    }
}
