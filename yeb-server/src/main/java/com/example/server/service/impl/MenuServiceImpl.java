package com.example.server.service.impl;

import com.example.server.pojo.Admin;
import com.example.server.pojo.Menu;
import com.example.server.mapper.MenuMapper;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.AdminRoleService;
import com.example.server.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    private final MenuMapper menuMapper;
    private final AdminRoleService adminRoleService;
    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper
            ,AdminRoleService adminRoleService){
        this.menuMapper=menuMapper;
        this.adminRoleService=adminRoleService;
    }
    @Override
    public List<Menu> getMenusByRoles() {
        return menuMapper.getMenusByRoles();
    }

    @Override
    public List<Menu> getMenusLevels() {
//        首先需要获得role

        return menuMapper.getMenusLevels();
    }
}
