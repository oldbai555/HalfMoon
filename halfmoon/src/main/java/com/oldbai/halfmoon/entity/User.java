package com.oldbai.halfmoon.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private String roles;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "状态：0表示删除，1表示正常")
    private String state;

    @ApiModelProperty(value = "注册ip")
    private String regIp;

    @ApiModelProperty(value = "登录Ip")
    private String loginIp;

    @ApiModelProperty(value = "发布时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "(0表示不删除，1表示删除)")
    private Integer isDelete;


}
