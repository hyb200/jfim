package com.abin.chatserver.user.service;

import com.abin.chatserver.user.domain.dto.SummaryInfoDTO;
import com.abin.chatserver.user.domain.vo.req.SummaryInfoReq;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;

import java.util.List;

public interface UserService {

    UserInfoResp getUserInfo(Long uid);

    /**
     * 获取用户汇总信息
     */
    List<SummaryInfoDTO> getSummaryUserInfo(SummaryInfoReq req);
}
