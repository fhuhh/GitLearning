package com.example.server.pojo;

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
 * @since 2022-07-15
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("t_employee_remove")
@ApiModel(value = "EmployeeRemove对象", description = "")
public class EmployeeRemove implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("员工id")
    @TableField("eid")
    private Integer eid;

    @ApiModelProperty("调动后部门")
    @TableField("afterDepId")
    private Integer afterDepId;

    @ApiModelProperty("调动后职位")
    @TableField("afterJobId")
    private Integer afterJobId;

    @ApiModelProperty("调动日期")
    @TableField("removeDate")
    private LocalDate removeDate;

    @ApiModelProperty("调动原因")
    @TableField("reason")
    private String reason;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;


}
