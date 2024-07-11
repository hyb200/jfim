package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgreeRequestReq {

    @NotNull
    @Schema(description = "申请id")
    private Long applyId;
}
