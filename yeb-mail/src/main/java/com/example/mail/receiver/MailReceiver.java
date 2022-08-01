package com.example.mail.receiver;

import com.example.server.config.constants.MailConstants;
import com.example.server.pojo.Employee;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Component
public class MailReceiver {
//    打印日志
    public static final Logger logger= LoggerFactory.getLogger(MailReceiver.class);
//    注入几个接受邮件必要的东西
    private final JavaMailSender mailSender;
    private final MailProperties mailProperties;
    private final TemplateEngine templateEngine;
    private final RedisTemplate<String,Object> redisTemplate;
    @Autowired(required = false)
    public MailReceiver(JavaMailSender mailSender
            ,MailProperties mailProperties
            ,TemplateEngine templateEngine
            ,RedisTemplate<String,Object> redisTemplate){
        this.mailSender=mailSender;
        this.mailProperties=mailProperties;
        this.templateEngine=templateEngine;
        this.redisTemplate=redisTemplate;
    }
    @RabbitListener(queues =MailConstants.QUEUE_NAME)
//    手动确认rabbitmq需要接收的是Message
    public void handler(Message<Employee> msg, Channel channel) throws IOException {
//        获取传过来的员工
        Employee employee=msg.getPayload();
//        获取消息头部
        MessageHeaders headers=msg.getHeaders();
//        从头部获取消息序号
        Long tag=headers.get(AmqpHeaders.DELIVERY_TAG,Long.class);
//        获取MsgId
        String msgId=headers.get("spring_returned_message_correlation",String.class);
//        Redis中存入唯一的信息标识，以免重复处理同个消息
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);
        try {
            if (hashOperations.entries(MailConstants.REDIS_MAIL_KEY.getName()).containsKey(msgId)){
                logger.error("消息已经被消费----{}",msgId);
//                手动确认消息
                assert tag!=null;
                channel.basicAck(tag,false);
                return;
            }
//            发件人
            helper.setFrom(mailProperties.getUsername());
//            收件人
            helper.setTo(employee.getEmail());
            helper.setSubject("入职邮件");
            helper.setSentDate(new Date());

            Context context=new Context();
//            为哪个Template填数据
            context.setVariable("employeeName",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
//            生成邮件内容
            String mail=templateEngine.process("mail",context);
//            邮件内容写进sender
            helper.setText(mail,true);
            mailSender.send(message);
//            邮件发送成功
            logger.info("邮件发送成功");
            assert msgId != null;
//            存入redis，采用HashMap的形式
            hashOperations.put(MailConstants.REDIS_MAIL_KEY.getName(),msgId,"OK");
//            手动确认消息
            assert tag!=null;
            channel.basicAck(tag,false);

        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("邮件发送失败:"+e.getMessage());
        } catch (IOException e) {
//            第三个参数表示要不要把消息退回到队列中去
            channel.basicNack(tag,false,true);
            e.printStackTrace();
        }
    }

}
