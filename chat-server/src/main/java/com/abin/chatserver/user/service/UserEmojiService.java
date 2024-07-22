package com.abin.chatserver.user.service;

import com.abin.chatserver.user.domain.vo.req.AddEmojiReq;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import com.abin.chatserver.user.domain.vo.resp.UserEmojiResp;

import java.util.List;

public interface UserEmojiService {

    List<UserEmojiResp> getEmojiList(Long uid);

    Long addEmoji(Long uid, AddEmojiReq req);

    void delEmoji(Long uid, IdReq req);
}
