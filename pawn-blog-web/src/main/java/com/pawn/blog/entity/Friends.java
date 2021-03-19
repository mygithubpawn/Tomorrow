package com.pawn.blog.entity;

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
 * 友情连接
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_friends")
@ApiModel(value = "Friends对象", description = "")
public class Friends implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "友情链接名称")
    private String name;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "友情链接logo")
    private String logo;

    @ApiModelProperty(value = "友情链接")
    private String url;

    @ApiModelProperty(value = "顺序")
    @TableField("`order`")
    private Integer order;


    @ApiModelProperty(value = "友情链接状态:0表示不可用，1表示正常")
    private String state = "1";

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
