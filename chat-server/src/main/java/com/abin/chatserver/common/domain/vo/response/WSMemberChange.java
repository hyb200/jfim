package com.abin.chatserver.common.domain.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSMemberChange {

    public static final Integer CHANGE_TYPE_ADD = 1;

    public static final Integer CHANGE_TYPE_REMOVE = 2;

    @Schema(name = "群组id")
    private Long sessionId;

    @Schema(name = "变动uid集合")
    private Long uid;

    @Schema(name = "变动类型 1加入群组 2移除群组")
    private Integer changeType;
}
