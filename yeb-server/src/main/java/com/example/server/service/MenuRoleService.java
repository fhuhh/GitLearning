package com.example.server.service;

import com.example.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
public interface MenuRoleService extends IService<MenuRole> {

    boolean updateByRoleId(Integer roleId, List<Integer> menuIdList);
}
