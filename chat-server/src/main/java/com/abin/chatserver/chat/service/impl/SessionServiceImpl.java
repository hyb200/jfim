package com.abin.chatserver.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.chat.dao.SessionDao;
import com.abin.chatserver.chat.dao.SessionSingleDao;
import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.domain.enums.HotFlagEnum;
import com.abin.chatserver.chat.domain.enums.SessionTypeEnum;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.abin.chatserver.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionSingleDao sessionSingleDao;

    private final SessionDao sessionDao;

    @Override
    public SessionSingle newSingleSession(List<Long> uids) {
        if (CollUtil.isEmpty(uids) || uids.size() != 2) {
            throw new BusinessException("会话创建失败");
        }
        String key = generateSessionKey(uids);
        SessionSingle sessionSingle = sessionSingleDao.getByKey(key);
        if (Objects.nonNull(sessionSingle)) {
            //  已经存在就恢复
            if (sessionSingle.getStatus().equals(CommonStatusEnum.NOT_NORMAL.getStatus())) {
                sessionSingleDao.renewSession(sessionSingle.getId());
            }
        } else {//  新建会话
            Session session = createSession(SessionTypeEnum.SINGLE);
            sessionSingle = createSingleSession(session.getId(), uids);
        }
        return sessionSingle;
    }

    private SessionSingle createSingleSession(Long sessionId, List<Long> uids) {
        uids = uids.stream().sorted().toList();
        SessionSingle insert = SessionSingle.builder()
                .sessionId(sessionId)
                .sessionKey(generateSessionKey(uids))
                .uid1(uids.get(0))
                .uid2(uids.get(1))
                .status(CommonStatusEnum.NORMAL.getStatus())
                .build();
        sessionSingleDao.save(insert);
        return insert;
    }

    private Session createSession(SessionTypeEnum sessionTypeEnum) {
        Session insert = Session.builder()
                .type(sessionTypeEnum.getType())
                .hotFlag(HotFlagEnum.NOT.getType())
                .build();
        sessionDao.save(insert);
        return insert;
    }

    private String generateSessionKey(List<Long> uids) {
        return uids.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("#"));
    }
}
