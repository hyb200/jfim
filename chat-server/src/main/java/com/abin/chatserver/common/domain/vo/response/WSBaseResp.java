package com.abin.chatserver.common.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseResp<T> {

    /**
     * WebSocket 推送给前端的信息
     *
     * @see com.abin.chatserver.common.domain.enums.WSResqTypeEnum
     */
    private Integer type;

    private T data;
}
