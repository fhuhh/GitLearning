package com.example.server.mapper;

import com.example.server.pojo.Admin;
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
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAdminsByKeyword(String keyword, Integer id);
}
