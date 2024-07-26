package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestReq {

    @NotNull
    @Schema(description = "申请对象uid")
    private Long targetUid;

    @NotBlank(message = "请求消息不能为空")
    @Schema(description = "申请信息")
    private String message;
}
