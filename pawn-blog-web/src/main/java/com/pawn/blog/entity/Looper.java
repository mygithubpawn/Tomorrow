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
 * 轮播图
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_looper")
@ApiModel(value = "Looper对象", description = "")
public class Looper implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "轮播图标题")
    private String title;

    @ApiModelProperty(value = "轮播图描述")
    private String introduction;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "顺序")
    @TableField("`order`")
    private Integer order;

    @ApiModelProperty(value = "状态：0表示不可用，1表示正常")
    private String state = "1";

    @ApiModelProperty(value = "目标URL")
    private String targetUrl;

    @ApiModelProperty(value = "图片地址")
    private String imageUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
