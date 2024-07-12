package com.abin.chatserver.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.dao.FriendRequestDao;
import com.abin.chatserver.user.dao.UserFriendDao;
import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.abin.chatserver.user.domain.entity.UserFriend;
import com.abin.chatserver.user.domain.enums.ApplyStatusEnum;
import com.abin.chatserver.user.domain.enums.ReadStatusEnum;
import com.abin.chatserver.user.domain.vo.req.AgreeRequestReq;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.req.FriendCheckReq;
import com.abin.chatserver.user.domain.vo.req.FriendRequestReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.abin.chatserver.user.domain.vo.resp.FriendCheckResp;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;
import com.abin.chatserver.user.service.FriendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private UserFriendDao userFriendDao;

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Override
    public FriendCheckResp check(Long uid, FriendCheckReq friendCheckReq) {
        List<UserFriend> allFriends = userFriendDao.getAllFriends(uid, friendCheckReq.getUids());
        Set<Long> friendUids = allFriends.stream().map(UserFriend::getFriendUid).collect(Collectors.toSet());
        List<FriendCheckResp.FriendCheck> friendCheckList = friendCheckReq.getUids().stream().map(friendUid -> {
            FriendCheckResp.FriendCheck friendCheck = new FriendCheckResp.FriendCheck();
            friendCheck.setUid(friendUid);
            friendCheck.setIsFriend(friendUids.contains(friendUid));
            return friendCheck;
        }).toList();
        return new FriendCheckResp(friendCheckList);
    }

    @Override
    @RedissonLock(key = "#uid")
    public void doFriendRequest(Long uid, FriendRequestReq req) {
        Long targetUid = req.getTargetUid();
        String message = req.getMessage();

        //  查看是否已经添加好友
        UserFriend friend = userFriendDao.getFriend(uid, targetUid);
        if (Objects.nonNull(friend)) {
            throw new BusinessException("你们已经是好友了");
        }

        //  是否已经有申请记录 (我 -> ta)
        FriendRequest selfRequest = friendRequestDao.getApplicationRecord(uid, targetUid);
        if (Objects.nonNull(selfRequest)) {
            if (!selfRequest.getMsg().equals(message)) {
                //  再次申请更新信息 todo 测试该逻辑
                friendRequestDao.updateMessage(selfRequest.getId(), message);
            }
            //  todo 通知再次申请更新 message
            return;
        }

        //  是否已经有申请记录 (ta -> 我)
        FriendRequest friendRequest = friendRequestDao.getApplicationRecord(targetUid, uid);
        if (Objects.nonNull(friendRequest)) {
            //  如果对方已经申请，直接同意添加
            ((FriendService) AopContext.currentProxy()).agreeRequest(uid, new AgreeRequestReq(friendRequest.getId()));
            return;
        }

        FriendRequest insert = FriendRequest.builder()
                .uid(uid)
                .targetId(targetUid)
                .msg(message)
                .status(ApplyStatusEnum.PENDING.getCode())
                .readStatus(ReadStatusEnum.UNREAD.getCode())
                .build();
        friendRequestDao.save(insert);
        //  todo 发布申请事件
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @RedissonLock(key = "#uid")
    public void agreeRequest(Long uid, AgreeRequestReq req) {
        Long applyId = req.getApplyId();
        FriendRequest friendRequest = friendRequestDao.getById(applyId);

        if (Objects.isNull(friendRequest)) {
            throw new BusinessException("该申请记录不存在");
        }

        //  同意申请
        friendRequestDao.agree(applyId);
        //  创建好友关系
        createFriend(uid, friendRequest.getUid());
        //  todo 创建新会话，发送消息
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(Long uid, Long targetUid) {
        List<UserFriend> relations = userFriendDao.getUserFriend(uid, targetUid);
        if (CollUtil.isEmpty(relations)) {
            log.info("双方不是好友关系: {}, {}", uid, targetUid);
            return;
        }
        List<Long> relationIds = relations.stream().map(UserFriend::getId).toList();
        userFriendDao.removeByIds(relationIds);
        //  todo 禁用会话
    }


    @Override
    public CursorPageBaseResp<UserInfoResp> friendList(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, cursorPageBaseReq);

        return null;
    }

    private void createFriend(Long uid, Long friendUid) {
        UserFriend a = UserFriend.builder()
                .uid(uid)
                .friendUid(friendUid)
                .build();
        UserFriend b = UserFriend.builder()
                .uid(friendUid)
                .friendUid(uid)
                .build();
        userFriendDao.saveBatch(List.of(a, b));
    }
}
