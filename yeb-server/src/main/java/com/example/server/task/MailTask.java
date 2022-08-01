package com.example.server.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.server.config.constants.MailConstants;
import com.example.server.pojo.Employee;
import com.example.server.pojo.MailLog;
import com.example.server.service.EmployeeService;
import com.example.server.service.MailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MailTask {
    private final MailLogService mailLogService;
    private final EmployeeService employeeService;
    private final RabbitTemplate rabbitTemplate;
    public MailTask(MailLogService mailLogService
            ,EmployeeService employeeService
            ,RabbitTemplate rabbitTemplate){
        this.mailLogService=mailLogService;
        this.employeeService=employeeService;
        this.rabbitTemplate=rabbitTemplate;
    }

//    邮件发送10秒一次
    @Scheduled(cron = "0/10 * * * * ?")
    public void mailTask(){
//        状态为0，并且超过尝试时间,需要重新发送一下
        List<MailLog> list = mailLogService.list(new LambdaQueryWrapper<MailLog>().eq(MailLog::getStatus, 0).lt(MailLog::getTryTime, LocalDateTime.now()));
        list.forEach(mailLog -> {
//            尝试次数必须小于3,大于等于三直接判定失败
            if (MailConstants.MAX_TRY_COUNT.getCode()<=mailLog.getCount()){
                mailLogService.update(new LambdaUpdateWrapper<MailLog>()
                        .set(MailLog::getStatus,2)
                        .eq(MailLog::getMsgId,mailLog.getMsgId()));
            }else {
                mailLogService.update(new LambdaUpdateWrapper<MailLog>()
                        .set(MailLog::getCount,mailLog.getCount()+1)
                        .set(MailLog::getUpdateTime,LocalDateTime.now())
                        .set(MailLog::getTryTime,LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT.getCode())));

                Employee employee = employeeService.getEmployeeById(mailLog.getEid()).get(0);
//            发送到队列去
                rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME.getName()
                        ,MailConstants.MAIL_ROUTING_KEY_NAME.getName()
                        ,employee
                        ,new CorrelationData(mailLog.getMsgId()));
            }
        });
    }
}
