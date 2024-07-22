package com.abin.chatserver.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEmojiResp {

    @Schema(description = "表情id")
    private Long id;

    @Schema(description = "表情url")
    private String emojiUrl;
}
