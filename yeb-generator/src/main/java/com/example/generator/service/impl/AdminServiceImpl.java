package com.example.generator.service.impl;

import com.example.generator.pojo.Admin;
import com.example.generator.mapper.AdminMapper;
import com.example.generator.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dys
 * @since 2022-07-14
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
