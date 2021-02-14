package com.oldbai.halfmoon.entity;

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
 * 
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Data
  @EqualsAndHashCode(callSuper = false)
  @Accessors(chain = true)
@TableName("tb_refresh_token")
@ApiModel(value="RefreshToken对象", description="")
public class RefreshToken implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.ASSIGN_UUID)
      private String id;

      @ApiModelProperty(value = "token")
      private String refreshToken;

      @ApiModelProperty(value = "用户Id")
      private String userId;

      @ApiModelProperty(value = "token_key ，存放在redis中需要的用到的key")
      private String tokenKey;

      @ApiModelProperty(value = "发布时间")
      private Date createTime;

      @ApiModelProperty(value = "更新时间")
      private Date updateTime;


}
