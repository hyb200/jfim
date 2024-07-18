package com.abin.chatserver.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * 单聊表
 * @TableName session_single
 */
@EqualsAndHashCode(callSuper = false)
@TableName(value ="session_single")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionSingle implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会话id
     */
    private Long sessionId;

    /**
     * uid1（更小的uid）
     */
    private Long uid1;

    /**
     * uid2（更大的uid）
     */
    private Long uid2;

    /**
     * 会话key（uid1_uid2）
     */
    private String sessionKey;

    /**
     * 会话状态 0正常 1禁用(删好友了禁用)
     */
    private Integer status;

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
}