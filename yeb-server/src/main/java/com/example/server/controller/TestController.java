package com.example.server.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/test")
public class TestController {
    @GetMapping("/employee/basic/hello")
    @ApiOperation(value = "测试基本接口")
    public String basicHello(){
        return "basicHello";
    }

    @GetMapping("/employee/advanced/hello")
    @ApiOperation(value = "测试高级接口")
    public String advancedHello(){
        return "advancedHello";
    }
}
