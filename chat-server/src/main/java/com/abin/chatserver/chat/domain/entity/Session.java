package com.abin.chatserver.chat.domain.entity;

import com.abin.chatserver.chat.domain.enums.HotFlagEnum;
import com.abin.chatserver.chat.domain.enums.SessionTypeEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话表
 * @TableName session
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value ="session")
@Data
public class Session implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话类型 1群聊 2单聊
     * @see com.abin.chatserver.chat.domain.enums.SessionTypeEnum
     */
    private Integer type;

    /**
     * 是否为热点群聊
     * @see com.abin.chatserver.chat.domain.enums.HotFlagEnum
     */
    private Integer hotFlag;

    /**
     * 群消息的最后更新时间（热点群不需要写扩散，只更新这里）
     */
    private Date activeTime;

    /**
     * 会话中的最后一条消息id
     */
    private Long lastMsgId;

    /**
     * 额外信息（根据不同类型房间有不同存储的东西）
     */
    private String extJson;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    public Boolean isHotSession() {
        return HotFlagEnum.of(this.hotFlag) == HotFlagEnum.YES;
    }

    @JsonIgnore
    public boolean isSingleSession() {
        return SessionTypeEnum.of(this.type) == SessionTypeEnum.SINGLE;
    }

    @JsonIgnore
    public boolean isGroupSession() {
        return SessionTypeEnum.of(this.type) == SessionTypeEnum.GROUP;
    }
}