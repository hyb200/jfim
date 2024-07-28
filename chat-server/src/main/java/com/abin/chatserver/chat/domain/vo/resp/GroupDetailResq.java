package com.abin.chatserver.chat.domain.vo.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "群聊详情请求")
public class GroupDetailResq {

    @Schema(description = "会话id")
    private Long sessionId;

    @Schema(description = "群名称")
    private String groupName;

    @Schema(description = "群头像")
    private String avatar;

    /**
     * @see com.abin.chatserver.chat.domain.enums.GroupRoleEnum
     */
    @Schema(description = "请求者的角色 1管理员 2普通成员")
    private Integer role;
}
