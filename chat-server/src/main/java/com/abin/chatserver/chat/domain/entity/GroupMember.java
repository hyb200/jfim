package com.abin.chatserver.chat.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 群成员表
 * @TableName group_member
 */
@TableName(value ="group_member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 群组id
     */
    @TableField(value = "group_id")
    private Long groupId;

    /**
     * 成员uid
     */
    @TableField(value = "uid")
    private Long uid;

    /**
     * 成员角色 1群管理员 2普通成员
     * @see com.abin.chatserver.chat.domain.enums.GroupRoleEnum
     */
    @TableField(value = "role")
    private Integer role;

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