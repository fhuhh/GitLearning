package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.config.constants.MailConstants;
import com.example.server.pojo.Employee;
import com.example.server.mapper.EmployeeMapper;
import com.example.server.pojo.MailLog;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.common.ResponsePageBean;
import com.example.server.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.service.MailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final RabbitTemplate rabbitTemplate;
    private final MailLogService mailLogService;
    public EmployeeServiceImpl(EmployeeMapper employeeMapper
            ,RabbitTemplate rabbitTemplate
            ,MailLogService mailLogService){
        this.employeeMapper=employeeMapper;
        this.rabbitTemplate=rabbitTemplate;
        this.mailLogService=mailLogService;
    }

    @Override
    public ResponsePageBean getEmployees(Integer pageNum, Integer pageSize, Employee employee, LocalDate[] localDates) {
//        开启分页
        Page<Employee> employeePage=new Page<>(pageNum,pageSize);
        IPage<Employee> employees = employeeMapper.getEmployees(employeePage, employee, localDates);
        ResponsePageBean responsePageBean=new ResponsePageBean();
        responsePageBean.setTotal(employees.getTotal()).setData(employees.getRecords());
        return responsePageBean;
    }

    @Override
    public ResponseBean addEmployee(Employee employee) {
        LocalDate beginContract = employee.getBeginContract();
        LocalDate endContract = employee.getEndContract();
//        计算合同期限
        long until = beginContract.until(endContract, ChronoUnit.DAYS);
//        保留两位小数
        DecimalFormat decimalFormat=new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(until/365.00)));
        if (employeeMapper.insert(employee)==1){
//            发送消息前首先在数据库记录消息
            employee=getEmployeeById(employee.getId()).get(0);
            String msgId= UUID.randomUUID().toString();
            MailLog mailLog=new MailLog();
            mailLog
                    .setMsgId(msgId)
                    .setCount(0)
                    .setCreateTime(LocalDateTime.now())
                    .setTryTime(LocalDateTime.now().plusMinutes(1))
                    .setEid(employee.getId())
                    .setExchange(MailConstants.MAIL_EXCHANGE_NAME.getName())
                    .setStatus(0)
                    .setRouteKey(MailConstants.MAIL_ROUTING_KEY_NAME.getName())
                    .setUpdateTime(LocalDateTime.now());
            mailLogService.save(mailLog);
//            获取插入的对象id,其实插入后自动写入了,但是职位信息啥的都没有，所以需要重新获取一次
//            直接发送，设置好routerKey
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME.getName()
                    ,MailConstants.MAIL_QUEUE_NAME.getName()
                    ,employee
                    ,new CorrelationData(msgId));
            return ResponseBean.success("插入成功");
        }else {
            return ResponseBean.error("插入失败");
        }
    }

    @Override
    public List<Employee> getEmployeeById(Integer id) {
        return employeeMapper.getEmployeeById(id);
    }
}
