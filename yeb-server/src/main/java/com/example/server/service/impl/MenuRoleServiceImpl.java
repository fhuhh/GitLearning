package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.server.pojo.MenuRole;
import com.example.server.mapper.MenuRoleMapper;
import com.example.server.service.MenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {

    @Override
    @Transactional
    public boolean updateByRoleId(Integer roleId, List<Integer> menuIdList) {
//        首先去删除一下对应roleId的相关条目
        this.getBaseMapper().delete(new LambdaQueryWrapper<MenuRole>().eq(MenuRole::getRid,roleId));
//        看下是不是menuIdList是不是空
        if (null==menuIdList||menuIdList.size()==0){
            return true;
        }
        for (Integer menuId:menuIdList){
            this.getBaseMapper().insert(new MenuRole().setMid(menuId).setRid(roleId));
        }
        return true;
    }
}
