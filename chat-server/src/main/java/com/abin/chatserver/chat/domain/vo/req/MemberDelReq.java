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
public class MemberDelReq {
    
    @NotNull
    @Schema(description = "会话id")
    private Long sessionId;

    @NotNull
    @Schema(description = "被移除的uid（主动退群填自己）")
    private Long uid;
}
