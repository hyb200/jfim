package com.abin.chatserver.common.event.listener;

import com.abin.chatserver.chat.domain.entity.GroupMember;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.chat.service.cache.GroupMemberCache;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSMemberChange;
import com.abin.chatserver.common.event.GroupMemberAddEvent;
import com.abin.chatserver.user.dao.UserDao;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.service.cache.UserInfoCache;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMemberAddListener {

    private final UserInfoCache userInfoCache;

    private final ChatService chatService;

    private final GroupMemberCache groupMemberCache;

    private final UserDao userDao;

    private final PushMsgService pushMsgService;

    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event) {
        List<GroupMember> members = event.getMembers();
        SessionGroup sessionGroup = event.getSessionGroup();
        Long inviter = event.getInviter();
        UserInfoDTO userInfoDTO = userInfoCache.get(inviter);
        List<Long> uids = members.stream().map(GroupMember::getUid).toList();
        Map<Long, UserInfoDTO> map = userInfoCache.getBatch(uids);

        ChatMessageReq req = new ChatMessageReq();
        req.setSessionId(sessionGroup.getSessionId());
        req.setMsgType(MessageTypeEnum.SYSTEM.getType());
        String sb = "\"" +
                userInfoDTO.getNickname() +
                "\"" +
                "邀请" +
                map.values().stream().map(o -> "\"" + o.getNickname() + "\"").collect(Collectors.joining(",")) +
                "加入群聊";
        req.setBody(sb);

        chatService.sendMsg(User.SYSTEM_UID, req);
    }

    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event) {
        List<GroupMember> members = event.getMembers(); //   新成员
        SessionGroup sessionGroup = event.getSessionGroup();

        List<Long> memberUids = groupMemberCache.getMemberUids(sessionGroup.getSessionId());    //  旧成员
        List<Long> newMemberUids = members.stream().map(GroupMember::getUid).toList();
        List<User> users = userDao.listByIds(newMemberUids);
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = new WSBaseResp<>();
            ws.setType(WSResqTypeEnum.MEMBER_CHANGE.getType());

            WSMemberChange wsMemberChange = WSMemberChange.builder()
                    .uid(user.getUid())
                    .sessionId(sessionGroup.getSessionId())
                    .changeType(WSMemberChange.CHANGE_TYPE_ADD)
                    .build();
            ws.setData(wsMemberChange);
            pushMsgService.sendPushMsg(ws, memberUids);
        });
        //  移除缓存
        groupMemberCache.evictMemberUids(sessionGroup.getSessionId());
    }
}
