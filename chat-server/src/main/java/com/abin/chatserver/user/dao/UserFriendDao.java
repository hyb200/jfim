package com.abin.chatserver.user.dao;

import com.abin.chatserver.user.domain.entity.UserFriend;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【user_friend(用户好友表)】的数据库操作
* @createDate 2024-07-08 15:45:44
*/
@Service
public class UserFriendDao extends ServiceImpl<UserFriendMapper, UserFriend> {

    public CursorPageBaseResp<UserFriend> getFriendPage(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        LambdaQueryChainWrapper<UserFriend> wrapper = lambdaQuery();
        //  游标字段（快速定位索引的位置）
        wrapper.lt(UserFriend::getId, cursorPageBaseReq.getCursor());
        //  游标方向
        wrapper.orderByDesc(UserFriend::getId);
        return null;
    }

    public List<UserFriend> getAllFriends(Long uid, List<Long> uids) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .in(UserFriend::getFriendUid, uids)
                .list();
    }

    public UserFriend getFriend(Long uid, Long target) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, target)
                .one();
    }
}




