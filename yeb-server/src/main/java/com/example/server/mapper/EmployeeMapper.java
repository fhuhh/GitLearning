package com.example.server.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.server.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
//    注意分页类型的返回值是IPage
    IPage<Employee> getEmployees(Page<Employee> employeePage, Employee employee, LocalDate[] localDates);

    List<Employee> getEmployeeById(Integer id);
}
