package com.example.server.controller;

import com.example.server.pojo.Joblevel;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.JoblevelService;
import com.example.server.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/system/basic/jobLevel")
@Api("/jobLevel")
public class JoblevelController {
    private final JoblevelService joblevelService;
//    还是他妈的增删改查！！！！
    @Autowired
    public JoblevelController(JoblevelService joblevelService){
        this.joblevelService=joblevelService;
    }
    @ApiOperation(value = "获取所有职称")
    @GetMapping("/get")
    public ResponseBean getAllJobLevel(){
        return ResponseBean.success("查询成功",joblevelService.list());
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/add")
    public ResponseBean addJobLevel(@RequestBody Joblevel joblevel){
        if (joblevelService.save(joblevel)){
            return ResponseBean.success("成功添加");
        }else {
            return ResponseBean.error("添加失败");
        }
    }

    @ApiOperation(value = "删除职称")
    @GetMapping("/delete/{id}")
    public ResponseBean deleteJobLevel(@PathVariable Integer id){
        if (joblevelService.removeById(id)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }

    @ApiOperation(value = "批量删除")
    @GetMapping("/delete")
    public ResponseBean deleteJobLevelBatch(@RequestParam(name = "ids") String ids){
        List<Integer> idList= CommonUtils.stringToList(ids,Integer.class);
        if (joblevelService.removeByIds(idList)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }
}
