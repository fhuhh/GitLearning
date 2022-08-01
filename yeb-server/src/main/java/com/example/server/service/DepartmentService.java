package com.example.server.service;

import com.example.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
public interface DepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    boolean addDepartment(Department department);

    Integer deleteDepartment(Integer id);
}
