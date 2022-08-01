package com.example.server.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.example.server.pojo.Employee;
import com.example.server.pojo.Nation;
import com.example.server.pojo.PoliticsStatus;
import com.example.server.pojo.common.ResponseBean;
import com.example.server.pojo.common.ResponsePageBean;
import com.example.server.service.EmployeeService;
import com.example.server.service.NationService;
import com.example.server.service.PoliticsStatusService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final NationService nationService;
    private final PoliticsStatusService politicsStatusService;
    @Autowired
    public EmployeeController(EmployeeService employeeService
            ,NationService nationService
            ,PoliticsStatusService politicsStatusService){
        this.employeeService=employeeService;
        this.nationService=nationService;
        this.politicsStatusService=politicsStatusService;
    }

    @PostMapping("/search")
    @ApiOperation("分页查询相关员工")
    public ResponsePageBean getEmployees(@RequestParam(name = "pageNum") Integer pageNum
            , @RequestParam(name = "pageSize") Integer pageSize
            , @RequestBody Employee employee
            , LocalDate[] localDates){
        return employeeService.getEmployees(pageNum,pageSize,employee,localDates);
    }

    @ApiOperation("添加员工")
    @PostMapping("/add")
    public ResponseBean addEmployee(@RequestBody Employee employee){
        return employeeService.addEmployee(employee);
    }

    @ApiOperation("修改员工")
    @PostMapping("/update")
    public ResponseBean updateEmployee(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return ResponseBean.success("修改成功");
        }else {
            return ResponseBean.error("修改失败");
        }
    }

    @ApiOperation("删除员工")
    @GetMapping("/delete/{id}")
    public ResponseBean deleteEmployee(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return ResponseBean.success("删除成功");
        }else {
            return ResponseBean.error("删除失败");
        }
    }

    @ApiOperation(value = "导出员工",produces = "application/octet-stream")
    @GetMapping("/export")
//    使用流的形式传输文件
    public void exportEmployee(HttpServletResponse response) throws IOException {
        List<Employee> employees=employeeService.getEmployeeById(null);
//        导出代码
        ExportParams params=new ExportParams("员工表","员工表", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, employees);
        ServletOutputStream outputStream = null;
        try {
            response.setHeader("content-type","application/octet-stream");
//            针对中文乱码
            response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("员工表.xlsx","utf-8"));
            outputStream=response.getOutputStream();
//            传输
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=outputStream){
                outputStream.close();
            }
        }
    }

    @ApiOperation(value = "导入员工文件")
    @PostMapping("/import")

    public ResponseBean importEmployee(MultipartFile file){
        ImportParams params=new ImportParams();
//        标题行不要
        params.setTitleRows(1);
        List<Nation> nationList=nationService.list();
        List<PoliticsStatus> politicsStatusList=politicsStatusService.list();
        try {
            List<Employee> list= ExcelImportUtil.importExcel(file.getInputStream(),Employee.class,params);
            list.forEach(employee -> {
//                通过数据库的信息得到nationID
                Nation nation=nationList.get(nationList.indexOf(new Nation(employee.getNation().getName())));
                employee.setNationId(nation.getId());
//                得到politicalStatus的ID
                PoliticsStatus politicsStatus=politicsStatusList.get(politicsStatusList.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName())));
                employee.setPoliticId(politicsStatus.getId());
            });
            if (employeeService.saveBatch(list)){
                return ResponseBean.success("导入成功");
            }else {
                return ResponseBean.error("导入失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseBean.error("导入失败");
    }

}
