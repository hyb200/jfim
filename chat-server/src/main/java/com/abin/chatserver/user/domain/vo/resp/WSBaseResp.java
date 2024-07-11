package com.abin.chatserver.user.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WSBaseResp<T> {

    private Integer type;

    private T data;
}
