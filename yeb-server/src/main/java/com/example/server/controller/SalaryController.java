package com.example.server.controller;

import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.SalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/salary/sobcfg")
public class SalaryController {
    private final SalaryService salaryService;
    @Autowired
    public SalaryController(SalaryService salaryService){
        this.salaryService=salaryService;
    }

    @ApiOperation(value = "查询所有工资账套")
    @GetMapping("/get")
    public ResponseBean getAllSalaries(){
        return ResponseBean.success("查询成功",salaryService.list());
    }


}
