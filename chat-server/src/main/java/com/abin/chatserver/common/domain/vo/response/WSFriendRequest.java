package com.abin.chatserver.common.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSFriendRequest {

    @Schema(name = "申请人")
    private Long uid;

    @Schema(name = "申请未读数")
    private Integer unreadCount;
}
