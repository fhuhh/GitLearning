package com.example.server.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

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
@TableName("t_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("url")
    @TableField("url")
    private String url;

    @ApiModelProperty("path")
    @TableField("path")
    private String path;

    @ApiModelProperty("组件")
    @TableField("component")
    private String component;

    @ApiModelProperty("菜单名")
    @TableField("name")
    private String name;

    @ApiModelProperty("图标")
    @TableField("iconCls")
    private String iconCls;

    @ApiModelProperty("是否保持激活")
    @TableField("keepAlive")
    private Boolean keepAlive;

    @ApiModelProperty("是否要求权限")
    @TableField("requireAuth")
    private Boolean requireAuth;

    @ApiModelProperty("父id")
    @TableField("parentId")
    private Integer parentId;

    @ApiModelProperty("是否启用")
    @TableField("enabled")
    private Boolean enabled;

//    子菜单
    @ApiModelProperty("子菜单列表")
    @TableField(exist = false)
    private List<Menu> children;

//    可以访问本菜单的角色
    @ApiModelProperty("角色列表")
    @TableField(exist = false)
    private List<Role> roles;


}
