package com.abin.chatserver.common.event.listener;

import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSFriendRequest;
import com.abin.chatserver.common.event.FriendRequestEvent;
import com.abin.chatserver.user.dao.FriendRequestDao;
import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FriendRequestListener {

    private final FriendRequestDao friendRequestDao;

    private final PushMsgService pushMsgService;

    @Async
    @TransactionalEventListener(classes = FriendRequestEvent.class, fallbackExecution = true)
    public void notifyFriend(FriendRequestEvent event) {
        FriendRequest friendRequest = event.getFriendRequest();
        Integer unreadCount = friendRequestDao.getUnreadCount(friendRequest.getTargetId());

        WSFriendRequest wsFriendRequest = new WSFriendRequest(friendRequest.getUid(), unreadCount);
        WSBaseResp<WSFriendRequest> resp = WSBaseResp.<WSFriendRequest>builder()
                .type(WSResqTypeEnum.APPLY.getType())
                .data(wsFriendRequest)
                .build();

        pushMsgService.sendPushMsg(resp, friendRequest.getTargetId());
    }
}
