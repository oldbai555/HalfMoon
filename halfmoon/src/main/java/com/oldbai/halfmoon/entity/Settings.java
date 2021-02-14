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
@TableName("tb_settings")
@ApiModel(value="Settings对象", description="")
public class Settings implements Serializable {

    private static final long serialVersionUID=1L;

      @ApiModelProperty(value = "ID")
        @TableId(value = "id", type = IdType.ASSIGN_UUID)
      private String id;

      @ApiModelProperty(value = "键")
      private String key;

      @ApiModelProperty(value = "值")
      private String value;

      @ApiModelProperty(value = "创建时间")
      private Date createTime;

      @ApiModelProperty(value = "更新时间")
      private Date updateTime;


}
