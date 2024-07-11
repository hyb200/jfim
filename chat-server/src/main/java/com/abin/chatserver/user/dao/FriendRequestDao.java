package com.abin.chatserver.user.dao;

import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.abin.chatserver.user.domain.enums.ApplyStatusEnum;
import com.abin.chatserver.user.domain.enums.ReadStatusEnum;
import com.abin.chatserver.user.mapper.FriendRequestMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


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
}




