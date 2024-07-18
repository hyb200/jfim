package com.abin.chatserver.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 消息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResp {

    @Schema(description = "发送者信息")
    private UserInfo fromUser;

    @Schema(description = "消息详情")
    private Message message;

    @Data
    @Builder
    public static class UserInfo {
        @Schema(description = "用户id")
        private Long uid;
    }

    @Data
    public static class Message {
        @Schema(description = "消息id")
        private Long id;

        @Schema(description = "会话id")
        private Long sessionId;

        @Schema(description = "消息发送时间")
        private Date sendTime;

        @Schema(description = "消息类型")
        private Integer type;

        @Schema(description = "消息内容")
        private Object body;
    }

}
