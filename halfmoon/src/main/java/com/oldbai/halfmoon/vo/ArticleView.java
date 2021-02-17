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
@TableName("tb_article_view")
@ApiModel(value="ArticleView对象", description="VIEW")
public class ArticleView implements Serializable {

    private static final long serialVersionUID=1L;

      @ApiModelProperty(value = "ID")
      private String id;

      @ApiModelProperty(value = "标题")
      private String title;

      @ApiModelProperty(value = "用户ID")
      private String userId;

      @ApiModelProperty(value = "用户头像")
      private String userAvatar;

      @ApiModelProperty(value = "用户昵称")
      private String userName;

      @ApiModelProperty(value = "分类ID")
      private String categoryId;

      @ApiModelProperty(value = "类型（0表示富文本，1表示markdown）")
      private String type;

      @ApiModelProperty(value = "状态（0表示已发布，1表示草稿，2表示删除）")
      private String state;

      @ApiModelProperty(value = "摘要")
      private String summary;

      @ApiModelProperty(value = "标签")
      private String labels;

      @ApiModelProperty(value = "阅读数量")
      private Integer viewCount;

      @ApiModelProperty(value = "发布时间")
      private Date createTime;

      @ApiModelProperty(value = "更新时间")
      private Date updateTime;

      @ApiModelProperty(value = "(0表示不删除,1表示删除)")
      private Integer isDelete;

      @ApiModelProperty(value = "封面")
      private String cover;


}
