package com.example.server.controller;

import com.example.server.pojo.Position;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.service.PositionService;
import com.example.server.utils.CommonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
@RequestMapping("/system/basic/pos")
@Api(value = "/position")
public class PositionController {
    private final PositionService positionService;
    @Autowired
    public PositionController(PositionService positionService){
        this.positionService=positionService;
    }
//    增删改查
    @ApiOperation(value = "获取所有的职位信息")
    @GetMapping("/get")
    public ResponseBean getAllPosition(){
        return ResponseBean.success("查询成功",positionService.list());
    }

    @ApiOperation(value = "添加职位信息")
    @PostMapping("/add")
    public ResponseBean addPosition(@RequestBody Position position){
        position.setCreateDate(LocalDateTime.now());
        if (positionService.save(position)){
            return ResponseBean.success("添加成功");
        }else {
            return ResponseBean.error("添加失败");
        }
    }

    @ApiOperation(value = "更新职位")
    @PostMapping("/update")
    public ResponseBean updatePosition(@RequestBody Position position){
        if (positionService.updateById(position)){
            return ResponseBean.success("更新成功");
        }else {
            return ResponseBean.error("更新失败");
        }
    }

    @ApiOperation(value = "删除职位")
    @GetMapping("/delete/{id}")
    public ResponseBean deletePosition(@PathVariable Integer id){
        if (positionService.removeById(id)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }

    @ApiOperation(value = "批量删除")
    @GetMapping("/delete")
    public ResponseBean deleteBatchByIds (@RequestParam(name = "ids") String ids){
        List<Integer> idList= CommonUtils.stringToList(ids,Integer.class);
        if (positionService.removeByIds(idList)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }
}
