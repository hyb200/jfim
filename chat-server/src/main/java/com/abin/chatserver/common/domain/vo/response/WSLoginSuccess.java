package com.abin.chatserver.common.domain.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WSLoginSuccess {

    private Long uid;

    private String avatar;

    private String token;

    private String name;

    /**
     *  用户权限 <br>
     *  0 - 普通用户 <br>
     *  1 - 管理员
     */
    //  todo 权限控制
    private Integer rule;
}
