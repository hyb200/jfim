package com.abin.chatserver.user.dao;

import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.abin.chatserver.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【user(用户表)】的数据库操作
* @createDate 2024-07-08 15:45:50
*/
@Service
public class UserDao extends ServiceImpl<UserMapper, User> {

    public User getByUsername(String username) {
        return lambdaQuery().eq(User::getUsername, username).one();
    }

    public void modifyName(Long uid, String name) {
        lambdaUpdate().set(User::getNickname, name)
                .eq(User::getUid, uid)
                .update();
    }

    public List<User> getFriendList(List<Long> uids) {
        return lambdaQuery().in(User::getUid, uids).list();
    }

    public List<User> getMembers() {
        return lambdaQuery()
                .eq(User::getStatus, CommonStatusEnum.NORMAL.getStatus())
                .orderByDesc(User::getUpdateTime)
                .last("limit 1000")
                .select(User::getUid, User::getNickname, User::getAvatar)
                .list();
    }
}




