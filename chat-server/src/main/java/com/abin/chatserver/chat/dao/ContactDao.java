package com.abin.chatserver.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.Contact;
import com.abin.chatserver.chat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【contact(会话列表)】的数据库操作
* @createDate 2024-07-20 18:07:54
*/
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

    public void refreshOrCreateActiveTime(Long sessionId, List<Long> uids, Long msgId, Date createTime) {
        baseMapper.refreshOrCreateActiveTime(sessionId, uids, msgId, createTime);
    }

    public Contact get(Long receiverUid, Long sessionId) {
        return lambdaQuery()
                .eq(Contact::getUid, receiverUid)
                .eq(Contact::getSessionId, sessionId)
                .one();
    }
}




