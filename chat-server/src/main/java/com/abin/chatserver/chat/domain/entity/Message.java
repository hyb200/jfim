package com.abin.chatserver.chat.domain.entity;

import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

/**
 * 消息表
 * @TableName message
 */
@TableName(value ="message", autoResultMap = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "消息")
public class Message implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 会话表id
     */
    @TableField(value = "session_id")
    private Long sessionId;

    /**
     * 消息发送者uid
     */
    @TableField(value = "from_uid")
    private Long fromUid;

    /**
     * 消息内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 回复的消息内容
     */
    @TableField(value = "reply_msg_id")
    private Long replyMsgId;

    /**
     * 消息状态 0正常 1删除
     * @see com.abin.chatserver.chat.domain.enums.MessageStatusEnum
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 与回复的消息间隔多少条
     */
    @TableField(value = "gap_count")
    private Integer gapCount;

    /**
     * 消息类型 1正常文本 2.撤回消息
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 扩展信息
     */
    @TableField(value = "extra", typeHandler = JacksonTypeHandler.class)
    private MessageExtra extra;

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