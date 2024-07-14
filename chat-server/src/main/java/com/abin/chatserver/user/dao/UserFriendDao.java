package com.abin.chatserver.user.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.abin.chatserver.user.domain.entity.UserFriend;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.user.mapper.UserFriendMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        wrapper.lt(StrUtil.isNotEmpty(cursorPageBaseReq.getCursor()), UserFriend::getId, cursorPageBaseReq.getCursor());
        //  额外查询条件
        wrapper.eq(UserFriend::getUid, uid);
        //  游标方向
        wrapper.orderByDesc(UserFriend::getId);
        Page<UserFriend> page = wrapper.page(cursorPageBaseReq.plusPage());

        String cursor = Optional.ofNullable(CollUtil.getLast(page.getRecords()))
                .map(UserFriend::getId)
                .map(String::valueOf)
                .orElse(null);
        boolean isLast = page.getRecords().size() != cursorPageBaseReq.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
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

    public List<UserFriend> getUserFriend(Long uid, Long targetUid) {
        return lambdaQuery().eq(UserFriend::getUid, uid)
                .eq(UserFriend::getFriendUid, targetUid)
                .or()
                .eq(UserFriend::getUid, targetUid)
                .eq(UserFriend::getFriendUid, uid)
                .select(UserFriend::getId)
                .list();
    }
}




