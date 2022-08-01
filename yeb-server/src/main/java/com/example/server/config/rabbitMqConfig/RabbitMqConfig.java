package com.example.server.config.rabbitMqConfig;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.server.config.constants.MailConstants;
import com.example.server.pojo.MailLog;
import com.example.server.service.MailLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value("${spring.rabbitmq.username}")
    private String username;
    @Value("${spring.rabbitmq.password}")
    private String password;
    @Value("${spring.rabbitmq.host}")
    private String host;
    @Value("${spring.rabbitmq.port}")
    private Integer port;
    private static final Logger LOGGER= LoggerFactory.getLogger(RabbitMqConfig.class);
    private final MailLogService mailLogService;
    @Autowired
    public RabbitMqConfig(MailLogService mailLogService){
        this.mailLogService=mailLogService;
    }
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory=new CachingConnectionFactory(host,port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
    @Bean
    public RabbitTemplate rabbitTemplate(){
        RabbitTemplate template=new RabbitTemplate(connectionFactory());
        /*
        配置确认回调
        * 第一个参数是msgId
        * 第二个参数是ack
        * 第三个参数是原因*/
        template.setConfirmCallback(((correlationData, b, s) -> {
            assert correlationData != null;
            String msgId=correlationData.getId();
            if (b){
                LOGGER.info("{}---消息发送成功",msgId);
//                更新数据库中消息的状态
                mailLogService.update(new LambdaUpdateWrapper<MailLog>().set(MailLog::getStatus,1).eq(MailLog::getMsgId,msgId));

            }else {
                LOGGER.info("{}---消息发送失败",msgId);
            }
        }));
        /*
        * 配置失败回调
        * 1.message:消息主题
        * 2.replyCode:响应码
        * 3.replyText:响应描述
        * 4.exchange:交换机
        * 5.routingKey:路由键*/
        template.setReturnsCallback(returnedMessage -> LOGGER.info("{}---消息发送失败",returnedMessage.getMessage().getBody()));
        return template;
    }



    @Bean
//    设置消息队列
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME.getName());

    }

    @Bean
//    设置交换机
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME.getName());
    }

    @Bean
//    这个的设置相当有技术，需要前面的两个东西，设置路由
    public Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with(MailConstants.MAIL_ROUTING_KEY_NAME.getName());
    }

}
