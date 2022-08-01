package com.example.server.service;

import com.example.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.common.ResponsePageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
public interface EmployeeService extends IService<Employee> {

    ResponsePageBean getEmployees(Integer pageNum, Integer pageSize, Employee employee, LocalDate[] localDates);

    ResponseBean addEmployee(Employee employee);

    List<Employee> getEmployeeById(Integer id);
}
