package com.abin.chatserver.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SummaryInfoDTO {

    @Schema(description = "用户ID")
    private Long uid;

    @Schema(description = "是否需要刷新")
    private Boolean needRefresh = Boolean.TRUE;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    public static SummaryInfoDTO skip(Long uid) {
        return SummaryInfoDTO.builder()
                .uid(uid)
                .needRefresh(Boolean.FALSE)
                .build();
    }
}
