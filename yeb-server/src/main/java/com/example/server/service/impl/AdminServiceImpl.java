package com.example.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.config.constants.RedisKeyConstants;
import com.example.server.config.jwt.JwtTokenUtils;
import com.example.server.mapper.AdminMapper;
import com.example.server.mapper.AdminRoleMapper;
import com.example.server.mapper.MenuMapper;
import com.example.server.mapper.RoleMapper;
import com.example.server.pojo.Admin;
import com.example.server.pojo.AdminRole;
import com.example.server.pojo.Menu;
import com.example.server.pojo.Role;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.functional.AdminLogin;
import com.example.server.service.AdminService;
import com.example.server.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService,UserDetailsService {
//    需要这个东西来完成登录,需要自己重写一个UserDetailsService，并且重写loadUserByUserName
    private final JwtTokenUtils jwtTokenUtils;
    private final MenuMapper menuMapper;
    private final RoleMapper roleMapper;
    private final AdminMapper adminMapper;
    private final AdminRoleMapper adminRoleMapper;
    private final RedisTemplate<String,Object> redisTemplate;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    public AdminServiceImpl(JwtTokenUtils jwtTokenUtils
            ,MenuMapper menuMapper
            ,RoleMapper roleMapper
            ,AdminMapper adminMapper
            ,AdminRoleMapper adminRoleMapper
            ,RedisTemplate<String,Object> redisTemplate){
        this.jwtTokenUtils=jwtTokenUtils;
        this.menuMapper=menuMapper;
        this.redisTemplate=redisTemplate;
        this.roleMapper=roleMapper;
        this.adminMapper=adminMapper;
        this.adminRoleMapper=adminRoleMapper;
    }

    @Override
    public ResponseBean handleLogin(AdminLogin adminLogin, HttpServletRequest request) {
//        首先从request中的Session中获得text
        String captcha=(String) request.getSession().getAttribute("captcha");
//        if (!StringUtils.hasText(adminLogin.getCode())||!adminLogin.getCode().equals(captcha)){
//            return ResponseBean.error("验证码输入错误");
//        }
//        从数据库查一下用户
        UserDetails userDetails=this.loadUserByUsername(adminLogin.getUsername());
//        这个地方需要判断用户名和数据库的一样吗
        if (Objects.isNull(userDetails)||!new BCryptPasswordEncoder().matches(adminLogin.getPassword(), userDetails.getPassword())){
            return ResponseBean.error("用户名或密码错误");
        }
        if (!userDetails.isEnabled()){
            return ResponseBean.error("用户禁用");
        }
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//        更新SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        获得Token
        String token=jwtTokenUtils.generateToken(userDetails);
//        设置一个Map返回给前端
        Map<String,Object> map=new HashMap<>();
        map.put("token",token);
        map.put("tokenHeader",tokenHead);
        map.put("userId",((Admin) userDetails).getId());
        return ResponseBean.success("登陆成功",map);
    }

    @Override
    public ResponseBean getUserInfoByUsername(String username) {
        Admin admin= baseMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername,username).eq(Admin::getEnabled,true));
        return ResponseBean.success("查询成功",admin);
    }

    @Override
    public List<Menu> getMenusByAdminId() {
//        首先需要获取到用户的ID
        Admin admin= WebUtils.getCurrentAdmin();
        Integer userId = admin.getId();
//        通过ValueOperations来获取Redis中的值
        ValueOperations<String,Object> valueOperations=redisTemplate.opsForValue();
        String redisContent=JSON.toJSONString(valueOperations.get(RedisKeyConstants.MENU_KEY_PRE.getKey()+userId));
        List<Menu> menuList=JSON.parseArray(redisContent,Menu.class);
        /*
        * 对于表的增删改查，一定要更新Redis的内容*/
        if (CollectionUtils.isEmpty(menuList)){
//            说明redis里面就没有,直接访问数据库拿就行了
            menuList=menuMapper.getMenusByUserId(userId);
//            存在Redis里面
            valueOperations.set(RedisKeyConstants.MENU_KEY_PRE.getKey()+userId,menuList);
            return menuList;
        }
        return menuList;
    }

    @Override
    public List<Role> getRolesById(Integer userId) {
        return roleMapper.getRolesById(userId);
    }

    @Override
    public List<Admin> getAdminsByKeyword(String keyword) {
        Admin curUser=(Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return adminMapper.getAdminsByKeyword(keyword,curUser.getId());
    }

    @Override
    @Transactional
    public boolean updateAdminRole(Integer id, List<Integer> idList) {
//        首先删除所有的aid为id的条目
        adminRoleMapper.delete(new LambdaQueryWrapper<AdminRole>().eq(AdminRole::getAdminId,id));
        Integer result=adminRoleMapper.updateAdminRole(id,idList);
        return result==idList.size();
    }

    @Override
    public ResponseBean updateAdminUserface(String url, Integer id, Authentication authentication) {
        Admin admin= adminMapper.selectById(id);
        admin.setUserFace(url);
        int i=adminMapper.updateById(admin);
        if (i==1){
            Admin principal=(Admin) authentication.getPrincipal();
            principal.setUserFace(url);
//            更新后端的Authentication
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin
                    ,authentication.getCredentials()
                    ,authentication.getAuthorities()));
            return ResponseBean.success("更新成功",url);
        }
        return ResponseBean.error("更新失败");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin=(Admin)getUserInfoByUsername(username).getObj();
        if (null!=admin){
            admin.setRoles(getRolesById(admin.getId()));
            return admin;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
