package com.abin.chatserver.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.abin.chatserver.user.domain.dto.SummaryInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.SummaryInfoReq;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;
import com.abin.chatserver.user.service.UserService;
import com.abin.chatserver.user.service.cache.UserCache;
import com.abin.chatserver.user.service.cache.UserSummaryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCache userCache;
    @Autowired
    private UserSummaryCache userSummaryCache;

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userCache.getUserInfo(uid);
        return BeanUtil.copyProperties(user, UserInfoResp.class);
    }

    @Override
    public List<SummaryInfoDTO> getSummaryUserInfo(SummaryInfoReq req) {
        //  需要同步的 uids
        List<Long> needSyncUids = getNeedSyncUids(req.getReqList());

        Map<Long, SummaryInfoDTO> batch = userSummaryCache.getBatch(needSyncUids);
        return req.getReqList().stream()
                .map(o -> batch.containsKey(o.getUid()) ? batch.get(o.getUid()): SummaryInfoDTO.skip(o.getUid()))
                .filter(Objects::nonNull)
                .toList();
    }

    private List<Long> getNeedSyncUids(List<SummaryInfoReq.InfoReq> reqList) {
        List<Long> needSyncUids = new ArrayList<>();
        List<Long> userModifyTime = userCache.getModifyTime(reqList.stream().map(SummaryInfoReq.InfoReq::getUid).toList());
        for (int i = 0; i < reqList.size(); i ++ ) {
            SummaryInfoReq.InfoReq infoReq = reqList.get(i);
            Long modifyTime = userModifyTime.get(i);
            if (Objects.isNull(infoReq.getLastModifyTime()) || (Objects.nonNull(modifyTime) && modifyTime > infoReq.getLastModifyTime())) {
                needSyncUids.add(infoReq.getUid());
            }
        }
        return needSyncUids;
    }
}
