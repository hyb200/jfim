package com.abin.chatserver.chat.domain.vo.req;

import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagePageReq extends CursorPageBaseReq {

    @NotNull
    @Schema(description = "会话id")
    private Long sessionId;
}
