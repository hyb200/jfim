package com.abin.transaction.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本地消息表
 * @TableName secure_invoke_record
 */
@TableName(value ="secure_invoke_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecureInvokeRecord implements Serializable {

    public static final byte WAIT = 1;

    public static final byte FAIL = 2;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 请求参数
     */
    @TableField(value = "secure_invoke_json", typeHandler = JacksonTypeHandler.class)
    private SecureInvokeDTO secureInvokeDTO;

    /**
     * 状态 1待执行 2已失败
     */
    @TableField(value = "status")
    @Builder.Default
    private byte status = SecureInvokeRecord.WAIT;

    /**
     * 下一次重试的时间
     */
    @TableField(value = "next_retry_time")
    @Builder.Default
    private Date nextRetryTime = new Date();

    /**
     * 已经重试次数
     */
    @TableField(value = "retry_times")
    @Builder.Default
    private Integer retryTimes = 0;

    /**
     * 最大重试次数
     */
    @TableField(value = "max_retry_times")
    private Integer maxRetryTimes;

    /**
     * 执行失败的堆栈
     */
    @TableField(value = "fail_reason")
    private String failReason;

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
}