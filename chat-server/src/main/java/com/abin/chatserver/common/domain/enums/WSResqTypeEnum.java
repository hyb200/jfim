package com.abin.chatserver.common.domain.enums;

import com.abin.chatserver.common.domain.vo.response.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * WebSocket 前端请求类型枚举
 */
@AllArgsConstructor
@Getter
public enum WSResqTypeEnum {
    MESSAGE(1, "新消息", WSMessage.class),
    RECALL(2, "消息撤回", WSMsgRecall.class),
    APPLY(3, "好友申请", WSFriendRequest.class),
//    ONLINE_OFFLINE_NOTIFY(2, "上下线通知", WSOnlineOfflineNotify.class),
//    INVALIDATE_TOKEN(6, "使前端的token失效，意味着前端需要重新登录", null),
//    BLACK(7, "拉黑用户", WSBlack.class),
//    MARK(8, "消息标记", WSMsgMark.class),
//

//    MEMBER_CHANGE(11, "成员变动", WSMemberChange.class),
    ;

    private final Integer type;
    private final String description;
    private final Class clazz;

    private static final Map<Integer, WSResqTypeEnum> cache;

    static {
        cache = Arrays.stream(WSResqTypeEnum.values()).collect(Collectors.toMap(WSResqTypeEnum::getType, Function.identity()));
    }

    public static WSResqTypeEnum of(Integer type) {
        return cache.get(type);
    }
}
