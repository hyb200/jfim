package com.abin.chatserver.common.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WSBaseResp<T> {

    /**
     * WebSocket 推送给前端的信息
     *
     * @see com.abin.chatserver.commom.domain.enums.WSRespTypeEnum
     */
    private Integer type;

    private T data;
}
