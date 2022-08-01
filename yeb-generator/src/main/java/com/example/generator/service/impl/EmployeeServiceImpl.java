package com.example.generator.service.impl;

import com.example.generator.pojo.Employee;
import com.example.generator.mapper.EmployeeMapper;
import com.example.generator.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dys
 * @since 2022-07-14
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
