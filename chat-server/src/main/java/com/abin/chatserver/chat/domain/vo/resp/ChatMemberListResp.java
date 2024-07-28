package com.abin.chatserver.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberListResp {

    @Schema(description = "uid")
    private Long uid;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "头像")
    private String avatar;
}
