package com.abin.chatserver.user.service.impl;

import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.dao.UserEmojiDao;
import com.abin.chatserver.user.domain.entity.UserEmoji;
import com.abin.chatserver.user.domain.vo.req.AddEmojiReq;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import com.abin.chatserver.user.domain.vo.resp.UserEmojiResp;
import com.abin.chatserver.user.service.UserEmojiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserEmojiServiceImpl implements UserEmojiService {

    private final UserEmojiDao userEmojiDao;

    @Override
    public List<UserEmojiResp> getEmojiList(Long uid) {
        return userEmojiDao.listByUid(uid).stream()
                .map(o -> UserEmojiResp.builder()
                                .id(o.getId())
                                .emojiUrl(o.getEmojiUrl())
                                .build()
                ).toList();
    }

    @Override
    @RedissonLock(key = "#uid")
    public Long addEmoji(Long uid, AddEmojiReq req) {
        Long cnt = userEmojiDao.countByUid(uid);
        if (cnt > 50) throw new BusinessException("表情包数量超过上限");

        Long count = userEmojiDao.lambdaQuery()
                .eq(UserEmoji::getUid, uid)
                .eq(UserEmoji::getEmojiUrl, req.getEmojiUrl())
                .count();
        if (count > 0) {
            throw new BusinessException("表情包已存在");
        }

        UserEmoji insert = UserEmoji.builder().uid(uid).emojiUrl(req.getEmojiUrl()).build();
        userEmojiDao.save(insert);
        return insert.getId();
    }

    @Override
    public void delEmoji(Long uid, IdReq req) {
        UserEmoji userEmoji = userEmojiDao.getById(req.getId());
        if (Objects.isNull(userEmoji) || !Objects.equals(userEmoji.getUid(), uid)) {
            throw new BusinessException("表情包删除失败");
        }
        userEmojiDao.removeById(req.getId());
    }
}
