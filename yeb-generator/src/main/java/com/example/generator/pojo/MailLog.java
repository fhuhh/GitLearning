package com.example.generator.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
@TableName("t_mail_log")
@ApiModel(value = "MailLog对象", description = "")
public class MailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息id")
    @TableId("msgId")
    private String msgId;

    @ApiModelProperty("接收员工id")
    @TableField("eid")
    private Integer eid;

    @ApiModelProperty("状态（0:消息投递中 1:投递成功 2:投递失败）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("路由键")
    @TableField("routeKey")
    private String routeKey;

    @ApiModelProperty("交换机")
    @TableField("exchange")
    private String exchange;

    @ApiModelProperty("重试次数")
    @TableField("count")
    private Integer count;

    @ApiModelProperty("重试时间")
    @TableField("tryTime")
    private LocalDateTime tryTime;

    @ApiModelProperty("创建时间")
    @TableField("createTime")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField("updateTime")
    private LocalDateTime updateTime;


}
