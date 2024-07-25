package com.abin.chatserver.chat.domain.vo.resp;

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
public class GroupMemberResp {

    @Schema(description = "所有成员的uid")
    private List<Long> uids;

    @Schema(description = "管理员的uid")
    private List<Long> managerUids;
}
