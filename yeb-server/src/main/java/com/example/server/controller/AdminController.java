package com.example.server.controller;

import com.example.server.pojo.Admin;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.functional.AdminLogin;
import com.example.server.service.AdminService;
import com.example.server.service.RoleService;
import com.example.server.utils.CommonUtils;
import com.example.server.utils.FastDfsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
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
@Api(value = "/admin")
public class AdminController {
    private final AdminService adminService;
    private final RoleService roleService;
    @Autowired
    public AdminController(AdminService adminService
            ,RoleService roleService){
        this.adminService=adminService;
        this.roleService=roleService;
    }
//    如果就写在这里呢？
    @PostMapping("/admin/login")
//    写明swagger注解
    @ApiOperation(value = "处理登录")
    public ResponseBean loginHandler(@RequestBody AdminLogin adminLogin, HttpServletRequest request){
        return adminService.handleLogin(adminLogin,request);
    }

    @GetMapping("/admin/logout")
    @ApiOperation(value = "退出登录")
    public ResponseBean logoutHandler(){
//        前端收到消息只需要把token抹除就行了
        return ResponseBean.success("登出成功");
    }

//    获得登陆用户的信息
    @GetMapping("/admin/userInfo")
    @ApiOperation(value = "获取用户信息")
//    principal参数可以被框架自动识别为已经登陆的用户
    public ResponseBean userInfoGetter(Principal principal){
        if (null==principal){
            return ResponseBean.error("未登录");
        }
        String username = principal.getName();
        ResponseBean res=adminService.getUserInfoByUsername(username);
        Admin admin=(Admin) res.getObj();
        admin.setPassword("");
        admin.setRoles(adminService.getRolesById(admin.getId()));
        return res;
    }

    @ApiOperation(value = "查询管理员")
    @GetMapping("/system/admin/get/{keyword}")
    public ResponseBean getAdminsByKeyWord(@PathVariable String keyword){
        List<Admin> adminList=adminService.getAdminsByKeyword(keyword);
        if (null==adminList||adminList.isEmpty()){
            return ResponseBean.error("查询不到");
        }else {
            return ResponseBean.success("查询成功",adminList);
        }
    }

    @ApiOperation(value = "更新管理员")
    @PostMapping("/system/admin/update")
    public ResponseBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return ResponseBean.success("更新成功");
        }else {
            return ResponseBean.error("更新失败");
        }
    }

    @ApiOperation(value = "删除管理员")
    @GetMapping("/system/admin/delete/{id}")
    public ResponseBean deleteAdminById(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }

    @ApiOperation(value = "获取所有的角色")
    @GetMapping("/system/admin/roles")
    public ResponseBean getAllRoles(){
        return ResponseBean.success("查询成功",roleService.list());
    }

    @ApiOperation(value = "更新管理员角色")
    @GetMapping("/system/admin/update/{id}")
    public ResponseBean updateAdminRole(@PathVariable Integer id,@RequestParam(name = "roleIds") String roleIds){
        List<Integer> idList= CommonUtils.stringToList(roleIds,Integer.class);
        if (adminService.updateAdminRole(id,idList)){
            return ResponseBean.success("更新成功");
        }else{
            return ResponseBean.error("更新失败");
        }
    }

    @ApiOperation(value = "更新个人信息")
    @PostMapping("/system/admin/updateSelf")
    public ResponseBean updateSelf(@RequestBody Admin admin
            , Authentication authentication){
        if (adminService.updateById(admin)){
//            必须更新SecurityContextHolder中的内容
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return ResponseBean.success("更新成功");
        }
        return ResponseBean.error("更新失败");
    }

//    修改密码的功能：达成强制重新登录的效果
//    参数是id，旧密码和新密码

//    更新用户头像
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParams({@ApiImplicitParam(name = "file",value = "头像",dataType = "MultipartFile")})
    @PutMapping("/system/admin/userface")
    public ResponseBean updateUserface(MultipartFile file
            ,Integer id,Authentication authentication) throws IOException {
        String[] uploadPath=FastDfsUtils.upload(file);
//        传给前端的url
        assert uploadPath != null;
        String url=FastDfsUtils.getTrackerUrl()+uploadPath[0]+"/"+uploadPath[1];
        return adminService.updateAdminUserface(url,id,authentication);
    }
}
