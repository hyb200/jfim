package com.abin.chatserver.chat.domain.entity.msg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MsgRecall implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //撤回消息的uid
    private Long recallUid;

    //撤回的时间点
    private Date recallTime;
}
