package com.abin.chatserver.chat.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "邀请好友请求")
public class MemberAddReq {

    @NotNull
    @Schema(description = "会话id")
    private Long sessionId;

    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "邀请的uid")
    private List<Long> uids;
}
