package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.abin.chatserver.chat.domain.entity.msg.MsgRecall;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.service.cache.UserCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RecallMsgHandler extends AbstractMsgHandler<Object> {

    private final UserCache userCache;
    private final MessageDao messageDao;

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.RECALL;
    }

    @Override
    protected void saveMsg(Message message, Object body) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object showMsg(Message msg) {
        MsgRecall recall = msg.getExtra().getRecall();
        User user = userCache.getUserInfo(recall.getRecallUid());
        if (!Objects.equals(recall.getRecallUid(), msg.getFromUid())) {
            return String.format("管理员\"%s\"撤回了一条消息", user.getNickname());
        }
        return String.format("\"%s\"撤回了一条消息", user.getNickname());
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "消息已撤回";
    }

    @Override
    public String showContactMsg(Message msg) {
        return "撤回了一条信息";
    }

    public void recall(Long uid, Message message) {
        MessageExtra extra = message.getExtra();
        extra.setRecall(new MsgRecall(uid, new Date()));
        Message update = Message.builder()
                .id(message.getId())
                .type(MessageTypeEnum.RECALL.getType())
                .extra(extra)
                .build();
        messageDao.updateById(update);
        //  todo 消息撤回事件
    }
}
