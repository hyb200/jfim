package com.abin.chatserver.user.domain.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表情包
 * @TableName user_emoji
 */
@TableName(value ="user_emoji")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEmoji implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户表ID
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 表情地址
     */
    @TableField(value = "emoji_url")
    private String emojiUrl;

    /**
     * 逻辑删除(0-正常,1-删除)
     */
    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}