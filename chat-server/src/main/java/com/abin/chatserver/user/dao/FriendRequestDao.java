package com.abin.chatserver.user.dao;

import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.abin.chatserver.user.domain.entity.UserFriend;
import com.abin.chatserver.user.domain.enums.ApplyStatusEnum;
import com.abin.chatserver.user.domain.enums.ReadStatusEnum;
import com.abin.chatserver.user.mapper.FriendRequestMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FriendRequestDao extends ServiceImpl<FriendRequestMapper, FriendRequest> {

    public FriendRequest getApplicationRecord(Long uid, Long targetUid) {
        return lambdaQuery().eq(FriendRequest::getUid, uid)
                .eq(FriendRequest::getTargetId, targetUid)
                .eq(FriendRequest::getStatus, ApplyStatusEnum.PENDING.getCode())
                .one();
    }

    public void updateMessage(Long id, String message) {
        lambdaUpdate().set(FriendRequest::getMsg, message)
                .set(FriendRequest::getReadStatus, ReadStatusEnum.UNREAD.getCode())
                .eq(FriendRequest::getId, id)
                .update();
    }

    public void agree(Long applyId) {
        lambdaUpdate().set(FriendRequest::getStatus, ApplyStatusEnum.AGREE.getCode())
                .eq(FriendRequest::getId, applyId)
                .update();
    }

    public Integer getUnreadCount(Long uid) {
        return Math.toIntExact(lambdaQuery().eq(FriendRequest::getTargetId, uid)
                .eq(FriendRequest::getReadStatus, ReadStatusEnum.UNREAD.getCode())
                .count());
    }

    public IPage<FriendRequest> getRequestPage(Long uid, Page page) {
        return lambdaQuery().eq(FriendRequest::getTargetId, uid)
                .orderByDesc(FriendRequest::getCreateTime)
                .page(page);
    }

    public void updateReadStatusByIds(Long uid, List<Long> ids) {
        lambdaUpdate().set(FriendRequest::getReadStatus, ReadStatusEnum.READ.getCode())
                .eq(FriendRequest::getReadStatus, ReadStatusEnum.UNREAD.getCode())
                .in(FriendRequest::getId, ids)
                .eq(FriendRequest::getTargetId, uid)
                .update();
    }
}




