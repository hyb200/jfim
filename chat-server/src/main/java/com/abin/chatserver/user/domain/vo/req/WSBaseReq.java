package com.abin.chatserver.user.domain.vo.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WSBaseReq {

    /**
     * 请求类型
     */
    private Integer type;

    /**
     * 请求数据
     */
    private String data;
}
