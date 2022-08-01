package com.example.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.pojo.Menu;
import com.example.server.pojo.Role;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.functional.AdminLogin;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
public interface AdminService extends IService<Admin> {

    ResponseBean handleLogin(AdminLogin adminLogin, HttpServletRequest request);

    ResponseBean getUserInfoByUsername(String username);


    List<Menu> getMenusByAdminId();

    List<Role> getRolesById(Integer userId);

    List<Admin> getAdminsByKeyword(String keyword);

    boolean updateAdminRole(Integer id, List<Integer> idList);

    ResponseBean updateAdminUserface(String url, Integer id, Authentication authentication);
}
