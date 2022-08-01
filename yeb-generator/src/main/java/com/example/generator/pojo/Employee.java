package com.example.generator.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
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
 * @since 2022-07-14
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
    private String name;

    @ApiModelProperty("性别")
    @TableField("gender")
    private String gender;

    @ApiModelProperty("出生日期")
    @TableField("birthday")
    private LocalDate birthday;

    @ApiModelProperty("身份证号")
    @TableField("idCard")
    private String idCard;

    @ApiModelProperty("婚姻状况")
    @TableField("wedlock")
    private String wedlock;

    @ApiModelProperty("民族")
    @TableField("nationId")
    private Integer nationId;

    @ApiModelProperty("籍贯")
    @TableField("nativePlace")
    private String nativePlace;

    @ApiModelProperty("政治面貌")
    @TableField("politicId")
    private Integer politicId;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("电话号码")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("联系地址")
    @TableField("address")
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
    private String engageForm;

    @ApiModelProperty("最高学历")
    @TableField("tiptopDegree")
    private String tiptopDegree;

    @ApiModelProperty("所属专业")
    @TableField("specialty")
    private String specialty;

    @ApiModelProperty("毕业院校")
    @TableField("school")
    private String school;

    @ApiModelProperty("入职日期")
    @TableField("beginDate")
    private LocalDate beginDate;

    @ApiModelProperty("在职状态")
    @TableField("workState")
    private String workState;

    @ApiModelProperty("工号")
    @TableField("workID")
    private String workID;

    @ApiModelProperty("合同期限")
    @TableField("contractTerm")
    private Double contractTerm;

    @ApiModelProperty("转正日期")
    @TableField("conversionTime")
    private LocalDate conversionTime;

    @ApiModelProperty("离职日期")
    @TableField("notWorkDate")
    private LocalDate notWorkDate;

    @ApiModelProperty("合同起始日期")
    @TableField("beginContract")
    private LocalDate beginContract;

    @ApiModelProperty("合同终止日期")
    @TableField("endContract")
    private LocalDate endContract;

    @ApiModelProperty("工龄")
    @TableField("workAge")
    private Integer workAge;

    @ApiModelProperty("工资账套ID")
    @TableField("salaryId")
    private Integer salaryId;


}
