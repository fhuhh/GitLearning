package com.example.generator.mapper;

import com.example.generator.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dys
 * @since 2022-07-14
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
