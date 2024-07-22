package com.abin.chatserver.user.dao;

import com.abin.chatserver.user.domain.vo.resp.UserEmojiResp;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.user.domain.entity.UserEmoji;
import com.abin.chatserver.user.service.UserEmojiService;
import com.abin.chatserver.user.mapper.UserEmojiMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【user_emoji(用户表情包)】的数据库操作
* @createDate 2024-07-22 21:12:00
*/
@Service
public class UserEmojiDao extends ServiceImpl<UserEmojiMapper, UserEmoji> {

    public List<UserEmoji> listByUid(Long uid) {
        return lambdaQuery().eq(UserEmoji::getUid, uid).list();
    }

    public Long countByUid(Long uid) {
        return lambdaQuery().eq(UserEmoji::getUid, uid).count();
    }
}




