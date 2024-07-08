package com.abin.chatserver.user.domain.entity;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 用户表
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@Schema(name = "User对象", description = "用户表")
public class User implements Serializable {

    @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "用户昵称")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "在线状态")
    private Integer activeStatus;

    @Schema(description = "账户状态（正常 true，封号 false）")
    private Integer status;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}