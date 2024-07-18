package com.abin.chatserver.chat.service.strategy.msg;

import cn.hutool.core.bean.BeanUtil;
import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.enums.MessageStatusEnum;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.common.utils.CheckUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractMsgHandler<T> {

    @Autowired
    private MessageDao messageDao;

    private Class<T> bodyClz;

    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClz = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMessageTypeEnum().getType(), this);
    }

    abstract MessageTypeEnum getMessageTypeEnum();

    protected void checkMsg(T body, Long sessionId, Long uid) {};

    @Transactional
    public Long checkAndSaveMsg(Long uid, ChatMessageReq req) {
        T body = this.toBean(req.getBody());
        //  body 的参数校验
        CheckUtils.allCheckValidateThrow(body);
        //  子类扩展的参数校验
        checkMsg(body, req.getSessionId(), uid);

        Message insert = Message.builder()
                .fromUid(uid)
                .sessionId(req.getSessionId())
                .type(req.getMsgType())
                .status(MessageStatusEnum.NORMAL.getStatus())
                .build();
        //  统一保存
        messageDao.save(insert);
        //  拓展
        saveMsg(insert, body);
        return insert.getId();
    }

    protected abstract void saveMsg(Message message, T body);

    private T toBean(Object body) {
        if (bodyClz.isAssignableFrom(body.getClass())) {
            return (T) body;
        }
        return BeanUtil.toBean(body, bodyClz);
    }

    /**
     * 展示消息
     */
    public abstract Object showMsg(Message msg);

    /**
     * 被回复时展示的消息
     */
    public abstract Object showReplyMsg(Message msg);

    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(Message msg);
}
