package com.abin.chatserver.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.dao.FriendRequestDao;
import com.abin.chatserver.user.dao.UserDao;
import com.abin.chatserver.user.dao.UserFriendDao;
import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.entity.UserFriend;
import com.abin.chatserver.user.domain.enums.ApplyStatusEnum;
import com.abin.chatserver.user.domain.enums.ReadStatusEnum;
import com.abin.chatserver.user.domain.vo.req.*;
import com.abin.chatserver.user.domain.vo.resp.*;
import com.abin.chatserver.user.service.FriendService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final UserFriendDao userFriendDao;

    private final FriendRequestDao friendRequestDao;

    private final UserDao userDao;

    private final SessionService sessionService;

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
        SessionSingle sessionSingle = sessionService.newSingleSession(Arrays.asList(uid, friendRequest.getUid()));

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
    public RequestUnreadResp unread(Long uid) {
        Integer unread = friendRequestDao.getUnreadCount(uid);
        return RequestUnreadResp.builder()
                .unread(unread)
                .build();
    }

    @Override
    public PageBaseResp<FriendRequestResp> requestList(Long uid, PageBaseReq req) {
        IPage<FriendRequest> page = friendRequestDao.getRequestPage(uid, req.plusPage());
        if (CollUtil.isEmpty(page.getRecords())) {
            return PageBaseResp.empty();
        }
        //  设置为已读状态
        setRequestReadStatus(uid, page);

        List<FriendRequestResp> requestRespList = page.getRecords().stream()
                .map(friendRequest -> FriendRequestResp.builder()
                        .id(friendRequest.getId())
                        .uid(friendRequest.getUid())
                        .msg(friendRequest.getMsg())
                        .status(friendRequest.getStatus())
                        .build())
                .toList();
        return PageBaseResp.init(page, requestRespList);
    }

    private void setRequestReadStatus(Long uid, IPage<FriendRequest> page) {
        List<Long> ids = page.getRecords().stream()
                .map(FriendRequest::getId)
                .toList();
        friendRequestDao.updateReadStatusByIds(uid, ids);
    }


    @Override
    public CursorPageBaseResp<UserInfoResp> friendList(Long uid, CursorPageBaseReq cursorPageBaseReq) {
        CursorPageBaseResp<UserFriend> friendPage = userFriendDao.getFriendPage(uid, cursorPageBaseReq);
        if (CollUtil.isEmpty(friendPage.getList())) {
            return CursorPageBaseResp.empty();
        }
        List<Long> friendUids = friendPage.getList().stream()
                .map(UserFriend::getFriendUid).collect(Collectors.toList());
        List<User> friends = userDao.getFriendList(friendUids);
        List<UserInfoResp> userInfoList = friends.stream()
                .map(user -> UserInfoResp.builder().uid(user.getUid())
                        .nickname(user.getNickname())
                        .avatar(user.getAvatar())
                        .build()).toList();

        return CursorPageBaseResp.<UserInfoResp>builder()
                .list(userInfoList)
                .isLast(friendPage.getIsLast())
                .cursor(friendPage.getCursor())
                .build();
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
