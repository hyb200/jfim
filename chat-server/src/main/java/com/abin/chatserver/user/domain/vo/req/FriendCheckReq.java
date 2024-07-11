package com.abin.chatserver.user.domain.vo.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendCheckReq {

    @NotEmpty
    @Size(max = 50)
    @Schema(description = "需要查询的uid")
    private List<Long> uids;
}
