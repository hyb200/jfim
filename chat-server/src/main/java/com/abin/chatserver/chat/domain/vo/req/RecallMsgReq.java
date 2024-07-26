package com.abin.chatserver.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "消息撤回请求体")
@NoArgsConstructor
@AllArgsConstructor
public class RecallMsgReq {

    @NotNull
    @Schema(description = "会话id")
    private Long sessionId;

    @NotNull
    @Schema(description = "消息id")
    private Long msgId;
}
