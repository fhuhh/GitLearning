package com.example.server.config.constants;

public enum MailConstants {
    DELIVERING(0,null)
    ,SUCCESS(1,null)
    ,FAILURE(2,null)
    ,MAX_TRY_COUNT(3,null)
    ,MSG_TIMEOUT(1,null)
    ,MAIL_QUEUE_NAME(null,"mail.queue")
    ,MAIL_EXCHANGE_NAME(null,"mail.exchange")
    ,MAIL_ROUTING_KEY_NAME(null,"mail.routing.key")
    ,REDIS_MAIL_KEY(null,"mail_log");
    private final Integer code;
    private final String name;
    public final static String QUEUE_NAME= "mail.queue";
    MailConstants(Integer code,String name){
        this.code=code;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
