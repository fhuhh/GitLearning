package com.example.server.service.impl;

import com.example.server.pojo.Department;
import com.example.server.mapper.DepartmentMapper;
import com.example.server.service.DepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    private final DepartmentMapper departmentMapper;
    public DepartmentServiceImpl(DepartmentMapper departmentMapper){
        this.departmentMapper=departmentMapper;
    }


    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public boolean addDepartment(Department department) {
//        首先department的enabled设置为true
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
        return 1 == department.getResult();
    }

    @Override
    @Transactional
    public Integer deleteDepartment(Integer id) {
//        直接用Department中的result来接受存储过程的出参
        Department department=new Department();
        department.setId(id);
        departmentMapper.deleteDepartment(department);
        return department.getResult();
    }
}
