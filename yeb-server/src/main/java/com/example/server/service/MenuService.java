package com.example.server.service;

import com.example.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.server.pojo.common.ResponseBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
public interface MenuService extends IService<Menu> {
//    根据角色获取菜单列表
    List<Menu> getMenusByRoles();

    List<Menu> getMenusLevels();
}
