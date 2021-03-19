package com.pawn.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 消息实体类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_news")
@ApiModel(value = "News对象", description = "")
public class News implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "发送人的昵称")
    private String newsName;

    @ApiModelProperty(value = "发送人的头像")
    private String newsAver;

    @ApiModelProperty(value = "发送人的id")
    private String newsId;

    @ApiModelProperty(value = "接收人的id，公告为123456")
    private String receiverId;

    @ApiModelProperty(value = "消息状态（1，表示公告，2，表示消息")
    private String announcement;

    @ApiModelProperty(value = "消息状态（1，表示未查看，2，表示已查看")
    private String view;

    @ApiModelProperty(value = "消息状态（0表示删除，1表示正常")
    private String state="1";

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
