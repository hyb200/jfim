package com.abin.chatserver.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionResp {

    @Schema(description = "会话id")
    private Long sessionId;

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

    @Schema(description = "最新消息")
    private String text;

    @Schema(description = "会话名称")
    private String name;

    @Schema(description = "会话头像")
    private String avatar;

    @Schema(description = "会话最后活跃时间(用来排序)")
    private Date activeTime;

    @Schema(description = "未读数")
    private Integer unreadCount;
}
