package com.example.server.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author dys
 * @since 2022-07-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_employee")
@ApiModel(value = "Employee对象", description = "")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("员工编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工姓名")
    @TableField("name")
    @Excel(name = "员工姓名")
    private String name;

    @ApiModelProperty("性别")
    @TableField("gender")
    @Excel(name = "性别")
    private String gender;

    @ApiModelProperty("出生日期")
    @TableField("birthday")
    @Excel(name = "出生日期",width = 20,format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate birthday;

    @ApiModelProperty("身份证号")
    @TableField("idCard")
    @Excel(name = "身份证号",width = 30)
    private String idCard;

    @ApiModelProperty("婚姻状况")
    @TableField("wedlock")
    @Excel(name = "婚姻状况")
    private String wedlock;

    @ApiModelProperty("民族")
    @TableField("nationId")
    private Integer nationId;

    @ApiModelProperty("籍贯")
    @TableField("nativePlace")
    @Excel(name = "籍贯")
    private String nativePlace;

    @ApiModelProperty("政治面貌")
    @TableField("politicId")
    private Integer politicId;

    @ApiModelProperty("邮箱")
    @TableField("email")
    @Excel(name = "邮箱",width = 30)
    private String email;

    @ApiModelProperty("电话号码")
    @TableField("phone")
    @Excel(name = "电话号码",width = 15)
    private String phone;

    @ApiModelProperty("联系地址")
    @TableField("address")
    @Excel(name = "联系地址",width = 50)
    private String address;

    @ApiModelProperty("所属部门")
    @TableField("departmentId")
    private Integer departmentId;

    @ApiModelProperty("职称ID")
    @TableField("jobLevelId")
    private Integer jobLevelId;

    @ApiModelProperty("职位ID")
    @TableField("posId")
    private Integer posId;

    @ApiModelProperty("聘用形式")
    @TableField("engageForm")
    @Excel(name = "聘用方式")
    private String engageForm;

    @ApiModelProperty("最高学历")
    @TableField("tiptopDegree")
    private String tiptopDegree;

    @ApiModelProperty("所属专业")
    @TableField("specialty")
    private String specialty;

    @ApiModelProperty("毕业院校")
    @TableField("school")
    @Excel(name = "毕业院校",width = 20)
    private String school;

    @ApiModelProperty("入职日期")
    @TableField("beginDate")
    @Excel(name = "入职日期",width = 20,format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate beginDate;

    @ApiModelProperty("在职状态")
    @TableField("workState")
    private String workState;

    @ApiModelProperty("工号")
    @TableField("workID")
    @Excel(name = "工号")
    private String workID;

    @ApiModelProperty("合同期限")
    @TableField("contractTerm")
    @Excel(name = "合同期限",suffix = "年")
    private Double contractTerm;

    @ApiModelProperty("转正日期")
    @TableField("conversionTime")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate conversionTime;

    @ApiModelProperty("离职日期")
    @TableField("notWorkDate")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate notWorkDate;

    @ApiModelProperty("合同起始日期")
    @TableField("beginContract")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate beginContract;

    @ApiModelProperty("合同终止日期")
    @TableField("endContract")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate endContract;

    @ApiModelProperty("工龄")
    @TableField("workAge")
    private Integer workAge;

    @ApiModelProperty("工资账套ID")
    @TableField("salaryId")
    private Integer salaryId;

    @ApiModelProperty("名族")
    @TableField(exist = false)
//    如果是个实体用的是ExcelEntity
    @ExcelEntity(name = "民族")
    private Nation nation;

    @ApiModelProperty("政治面貌")
    @TableField(exist = false)
    @ExcelEntity(name = "政治面貌")
    private PoliticsStatus politicsStatus;

    @ApiModelProperty("部门")
    @TableField(exist = false)
    private Department department;

    @ApiModelProperty("职称")
    @TableField(exist = false)
    private Joblevel joblevel;

    @ApiModelProperty("职位")
    @TableField(exist = false)
    private Position position;

    @ApiModelProperty("账套信息")
    @TableField(exist = false)
    private Salary salary;
}
