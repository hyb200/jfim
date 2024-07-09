package com.abin.chatserver.common.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMsgMark {

    private List<WSMsgMarkItem> markList;

    @Data
    public static class WSMsgMarkItem {

        @Schema(name = "操作者")
        private Long uid;

        @Schema(name = "消息id")
        private Long msgId;

        /**
         * @see com.abin.mallchat.common.chat.domain.enums.MessageMarkTypeEnum
         */
        @Schema(name = "标记类型 1点赞 2举报")
        private Integer markType;

        @Schema(name = "被标记的数量")
        private Integer markCount;

        /**
         * @see com.abin.mallchat.common.chat.domain.enums.MessageMarkActTypeEnum
         */
        @Schema(name = "动作类型 1确认 2取消")
        private Integer actType;
    }
}
