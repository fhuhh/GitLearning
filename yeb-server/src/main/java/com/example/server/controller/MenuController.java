package com.example.server.controller;

import com.example.server.pojo.Menu;
import com.example.server.service.AdminService;
import com.example.server.service.MenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//根据表中的数据修改mapping
@RequestMapping("/system/cfg")
public class MenuController {
//    需要根据用户来确定可以访问的菜单
    private final AdminService adminService;
    public MenuController(AdminService adminService){
        this.adminService=adminService;
    }

    @ApiOperation(value = "通过用户的id查询菜单列表")
    @GetMapping("menu")
    public List<Menu> getMenusByAdminId(){
//        菜单信息应该放入Redis，提高多次访问的效率
        return adminService.getMenusByAdminId();
    }

}
