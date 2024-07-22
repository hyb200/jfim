package com.abin.chatserver.user.service;

import com.abin.chatserver.common.domain.vo.response.OssResp;
import com.abin.chatserver.user.domain.vo.req.UploadUrlReq;

public interface OssService {

    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
