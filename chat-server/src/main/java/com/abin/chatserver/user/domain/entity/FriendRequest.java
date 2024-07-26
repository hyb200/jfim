package com.abin.chatserver.user.domain.entity;

import java.io.Serial;
import java.io.Serializable;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import com.baomidou.mybatisplus.annotation.TableName;

/**
* 好友申请表
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("friend_request")
@Tag(name = "FriendRequest对象", description = "好友申请表")
public class FriendRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "申请人 uid")
    private Long uid;

    @Schema(description = "接收人 uid")
    private Long targetId;

    @Schema(description = "申请信息")
    private String msg;

    @Schema(description = "申请状态 1待审批 2同意")
    private Integer status;

    @Schema(description = "阅读状态 1未读 2已读")
    private Integer readStatus;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}