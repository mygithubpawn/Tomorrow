package com.pawn.blog.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
@ApiModel(value = "User对象", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "角色")
    private String roles;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱地址")
    private String email;

    @ApiModelProperty(value = "简介")
    private String sign;

    @ApiModelProperty(value = "职业")
    private String occupation;

    //逻辑删除（相当于执行修改方法）
    @TableLogic
    @ApiModelProperty(value = "状态：0表示删除，1表示正常")
    private String state = "1";

    @ApiModelProperty(value = "注册ip")
    private String regIp;

    @ApiModelProperty(value = "登录Ip")
    private String loginIp;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    public User(String id, String userName, String roles,
                String avatar, String email,
                String sign, String state, String regIp,
                String loginIp,
                String occupation,
                Date createTime, Date updateTime) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        this.avatar = avatar;
        this.email = email;
        this.sign = sign;
        this.state = state;
        this.regIp = regIp;
        this.loginIp = loginIp;
        this.occupation = occupation;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
}
