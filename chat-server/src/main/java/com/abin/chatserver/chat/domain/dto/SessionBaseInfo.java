package com.abin.chatserver.chat.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionBaseInfo {

    @Schema(description = "会话id")
    private Long sessionId;

    @Schema(description = "会话名称")
    private String name;

    @Schema(description = "会话头像")
    private String avatar;

    /**
     * @see com.abin.chatserver.chat.domain.enums.SessionTypeEnum
     */
    @Schema(description = "会话类型")
    private Integer type;

    /**
     * @see com.abin.chatserver.chat.domain.enums.HotFlagEnum
     */
    @Schema(description = "是否是热点群聊")
    private Integer hotFlag;

    @Schema(description = "群最后消息的更新时间")
    private Date activeTime;

    @Schema(description = "最后一条消息id")
    private Long lastMsgId;
}
