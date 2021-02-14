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
@TableName("tb_images")
@ApiModel(value="Images对象", description="")
public class Images implements Serializable {

    private static final long serialVersionUID=1L;

      @ApiModelProperty(value = "ID")
        @TableId(value = "id", type = IdType.ASSIGN_UUID)
      private String id;

      @ApiModelProperty(value = "用户ID")
      private String userId;

      @ApiModelProperty(value = "图片名称")
      private String name;

      @ApiModelProperty(value = "路径")
      private String url;

      @ApiModelProperty(value = "图片类型")
      private String contentType;

      @ApiModelProperty(value = "状态（0表示删除，1表正常）")
      private String state;

      @ApiModelProperty(value = "创建时间")
      private Date createTime;

      @ApiModelProperty(value = "更新时间")
      private Date updateTime;

      @ApiModelProperty(value = "(0表示不删除，1表示删除)")
      private Integer isDelete;


}
