package com.example.server.mapper;

import com.example.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesById(Integer userId);
}
