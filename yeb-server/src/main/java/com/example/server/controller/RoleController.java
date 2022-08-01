package com.example.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.server.pojo.Menu;
import com.example.server.pojo.MenuRole;
import com.example.server.pojo.Role;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.MenuRoleService;
import com.example.server.service.MenuService;
import com.example.server.service.RoleService;
import com.example.server.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/system/basic/role")
@Api(value = "/role")
public class RoleController {
    private final RoleService roleService;
    private final MenuService menuService;
    private final MenuRoleService menuRoleService;

    public RoleController(RoleService roleService
            ,MenuService menuService
            ,MenuRoleService menuRoleService){
        this.menuService=menuService;
        this.menuRoleService=menuRoleService;
        this.roleService=roleService;
    }

    @ApiOperation(value = "查询所有角色")
    @GetMapping("/get")
    public ResponseBean getAllRoles(){
        return ResponseBean.success("获取成功",roleService.list());
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/add")
    public ResponseBean addRole(@RequestBody Role role){
        if (!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }

        if (roleService.save(role)){
            return ResponseBean.success("添加成功");
        }else {
            return ResponseBean.error("添加失败");
        }
    }

    @ApiOperation(value = "删除角色")
    @GetMapping("/delete/{id}")
    public ResponseBean deleteRole(@PathVariable Integer id){
        if (roleService.removeById(id)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }

    @ApiOperation(value = "查询层级化菜单")
    @GetMapping("/menus")
    public ResponseBean getMenus(){
        List<Menu> menuList=menuService.getMenusLevels();
        if (null!=menuList){
            return ResponseBean.success("查询成功",menuList);
        }else {
            return ResponseBean.error("查询失败");
        }
    }

    @ApiOperation(value = "查询角色对应的菜单")
    @GetMapping("/menus/{roleId}")
    public ResponseBean getMenusByRoleId(@PathVariable Integer roleId){
        List<Integer> menuIds=menuRoleService.list(new LambdaQueryWrapper<MenuRole>().eq(MenuRole::getRid,roleId))
                .stream()
                .map(MenuRole::getMid)
                .collect(Collectors.toList());

        return ResponseBean.success("查询成功",menuIds);

    }

    @ApiOperation(value = "更新角色对应菜单")
    @GetMapping("/updateMenus/{roleId}")
    public ResponseBean updateMenus(@PathVariable Integer roleId,@RequestParam(name = "menuIds") String menuIds){
//        首先把menuIds转化为List
        List<Integer> menuIdList= CommonUtils.stringToList(menuIds,Integer.class);

        if (menuRoleService.updateByRoleId(roleId,menuIdList)){
            return ResponseBean.success("更新成功");
        }else {
            return ResponseBean.error("更新出错");
        }
    }
}
