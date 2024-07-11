package com.abin.chatserver.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FriendCheckResp {

    @Schema(description = "查询结果")
    private List<FriendCheck> result;

    @Data
    public static class FriendCheck {
        private Long uid;
        private Boolean isFriend;
    }
}
