package com.oldbai.halfmoon.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author oldbai
 * @since 2021-02-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user_view")
@ApiModel(value = "UserView对象", description = "VIEW")
public class UserView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

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

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "(0表示不删除，1表示删除)")
    private Integer isDelete;


}
