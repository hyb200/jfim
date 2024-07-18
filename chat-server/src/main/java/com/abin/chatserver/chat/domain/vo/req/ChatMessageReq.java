package com.abin.chatserver.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageReq {

    @NotNull
    @Schema(description = "会话id")
    private Long sessionId;

    /**
     * @see com.abin.chatserver.chat.domain.enums.MessageTypeEnum
     */
    @Schema(description = "消息类型")
    @NotNull
    private Integer msgType;

    /**
     * @see com.abin.chatserver.chat.domain.entity.msg
     */
    @Schema(description = "消息内容")
    @NotNull
    private Object body;
}
