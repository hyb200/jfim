package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryInfoReq {

    @Schema(description = "用户信息入参")
    @Size(max = 50, message = "一次最多更新50条")
    private List<InfoReq> reqList;

    @Data
    public static class InfoReq {

        @Schema(description = "uid")
        private Long uid;

        @Schema(description = "上次更新时间")
        private Long lastModifyTime;
    }
}
