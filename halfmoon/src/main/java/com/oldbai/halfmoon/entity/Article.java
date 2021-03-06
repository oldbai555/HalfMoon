package com.oldbai.halfmoon.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

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
@TableName("tb_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "类型（0表示富文本，1表示markdown）")
    private String type;

    @ApiModelProperty(value = "状态（0表示已发布，1表示草稿，2表示删除）")
    private String state;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "标签")
    private String labels;

    @ApiModelProperty(value = "标签数组")
    @TableField(exist = false)
    private List<String> labelsList;

    @ApiModelProperty(value = "阅读数量")
    private Integer viewCount;

    @ApiModelProperty(value = "发布时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "(0表示不删除,1表示删除)")
    private Integer isDelete;

    @ApiModelProperty(value = "封面")
    private String cover;


}
