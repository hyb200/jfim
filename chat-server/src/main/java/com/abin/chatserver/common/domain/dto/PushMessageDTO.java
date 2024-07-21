package com.abin.chatserver.common.domain.dto;

import com.abin.chatserver.common.domain.enums.WSPushTypeEnum;
import com.abin.chatserver.user.domain.vo.resp.WSBaseResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PushMessageDTO implements Serializable {

    private WSBaseResp<?> msg;

    private List<Long> uids;

    /**
     * @see WSPushTypeEnum
     */
    private Integer pushType;

    public PushMessageDTO(WSBaseResp<?> msg, List<Long> uids) {
        this.msg = msg;
        this.uids = uids;
        pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WSBaseResp<?> msg, Long uid) {
        this.msg = msg;
        this.uids = Collections.singletonList(uid);
        pushType = WSPushTypeEnum.USER.getType();
    }

    public PushMessageDTO(WSBaseResp<?> msg) {
        this.msg = msg;
        pushType = WSPushTypeEnum.ALL.getType();
    }
}
