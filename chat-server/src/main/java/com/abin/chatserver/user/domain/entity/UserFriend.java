package com.abin.chatserver.user.domain.entity;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 用户好友表
*/
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_friend")
@Schema(name = "UserFriend对象", description = "用户好友表")
public class UserFriend implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "uid")
    private Long uid;

    @Schema(description = "好友uid")
    private Long friendUid;

    @Schema(description = "逻辑删除(0-正常,1-删除)")
    private Integer deleted;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}