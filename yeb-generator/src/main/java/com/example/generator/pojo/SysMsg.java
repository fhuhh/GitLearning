package com.example.generator.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("t_sys_msg")
@ApiModel(value = "SysMsg对象", description = "")
public class SysMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("消息id")
    @TableField("mid")
    private Integer mid;

    @ApiModelProperty("0表示群发消息")
    @TableField("type")
    private Integer type;

    @ApiModelProperty("这条消息是给谁的")
    @TableField("adminid")
    private Integer adminid;

    @ApiModelProperty("0 未读 1 已读")
    @TableField("state")
    private Integer state;


}
