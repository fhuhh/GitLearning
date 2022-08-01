package com.example.server.mapper;

import com.example.server.pojo.AdminRole;
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
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer updateAdminRole(Integer id, List<Integer> idList);
}
