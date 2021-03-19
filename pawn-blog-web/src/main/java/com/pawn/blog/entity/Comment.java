package com.pawn.blog.entity;

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
 * 评论实体类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_comment")
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "父内容")
    private String parentContent;

    @ApiModelProperty(value = "文章发布人ID/父评论的ID")
    private String releaseId;

    @ApiModelProperty(value = "文章ID")
    private String articleId;

    @ApiModelProperty(value = "评论类型,1表示评论，2表示评论的回复")
    private String commentType;

    @ApiModelProperty(value = "评论内容")
    private String content;

    @ApiModelProperty(value = "评论用户的ID")
    private String userId;

    @ApiModelProperty(value = "评论用户的头像")
    private String userAvatar;

    @ApiModelProperty(value = "评论用户的名称")
    private String userName;

    @ApiModelProperty(value = "状态（0表示删除，1表示正常）")
    private String state = "1";

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
