package com.example.server.controller;

import com.example.server.pojo.Department;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/system/basic/dep")
public class DepartmentController {
    private final DepartmentService departmentService;
    @Autowired
    public DepartmentController(DepartmentService departmentService){
        this.departmentService=departmentService;
    }

    @ApiOperation(value = "获得所有部门")
    @GetMapping("/get")
    public ResponseBean getAllDepartments(){
        List<Department> departments=departmentService.getAllDepartments();
        if (null==departments){
            return ResponseBean.error("查询失败");
        }
        return ResponseBean.success("查询成功",departments);
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/add")
    public ResponseBean addDepartment(@RequestBody Department department){
//        使用service调用mapper，sql语句中使用存储过程
        if (departmentService.addDepartment(department)){
            return ResponseBean.success("添加成功");
        }else {
            return ResponseBean.error("添加失败");
        }
    }

    @ApiOperation(value = "删除部门")
    @GetMapping("/delete/{id}")
    public ResponseBean deleteDepartment(@PathVariable Integer id){
        Integer deleteResult=departmentService.deleteDepartment(id);
        if (deleteResult==1){
            return ResponseBean.success("删除成功");
        }else if (deleteResult==-2){
            return ResponseBean.error("该部门还有子部门，删除失败");
        }else {
            return ResponseBean.error("该部门下还有员工，禁止删除");
        }
    }
}
