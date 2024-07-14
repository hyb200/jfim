package com.abin.chatserver.user.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendRequestResp {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "申请人 uid")
    private Long uid;

    @Schema(description = "申请信息")
    private String msg;

    @Schema(description = "申请状态 1待审批 2同意")
    private Integer status;
}
