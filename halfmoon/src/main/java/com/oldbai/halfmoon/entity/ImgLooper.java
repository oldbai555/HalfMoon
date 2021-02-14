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
@TableName("tb_img_looper")
@ApiModel(value="ImgLooper对象", description="")
public class ImgLooper implements Serializable {

    private static final long serialVersionUID=1L;

      @ApiModelProperty(value = "ID")
        @TableId(value = "id", type = IdType.ASSIGN_UUID)
      private String id;

      @ApiModelProperty(value = "轮播图标题")
      private String title;

      @ApiModelProperty(value = "顺序")
      private Integer order;

      @ApiModelProperty(value = "状态：0表示不可用，1表示正常")
      private String state;

      @ApiModelProperty(value = "目标URL")
      private String targetUrl;

      @ApiModelProperty(value = "图片地址")
      private String imageUrl;

      @ApiModelProperty(value = "创建时间")
      private Date createTime;

      @ApiModelProperty(value = "更新时间")
      private Date updateTime;


}
