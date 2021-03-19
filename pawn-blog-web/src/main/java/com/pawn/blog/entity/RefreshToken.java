package com.pawn.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/***
 * description: token表
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/24 14:11
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_refresh_token")
@ApiModel(value = "RefreshToken对象", description = "")
public class RefreshToken implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "刷新的token")
    private String refreshToken;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "token的key")
    private String tokenKey;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
