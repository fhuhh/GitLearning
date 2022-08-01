package com.example.server.config.constants;

public enum RedisKeyConstants {
    MENU_KEY_PRE("menu_");
    private final String key;
    RedisKeyConstants(String key){
        this.key=key;
    }

    public String getKey() {
        return key;
    }
}
