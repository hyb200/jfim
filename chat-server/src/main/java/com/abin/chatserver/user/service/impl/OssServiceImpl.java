package com.abin.chatserver.user.service.impl;

import com.abin.chatserver.common.domain.vo.request.OssReq;
import com.abin.chatserver.common.domain.vo.response.OssResp;
import com.abin.chatserver.common.service.MinIOService;
import com.abin.chatserver.user.domain.enums.OssSceneEnum;
import com.abin.chatserver.user.domain.vo.req.UploadUrlReq;
import com.abin.chatserver.user.service.OssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final MinIOService minIOService;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(OssSceneEnum.of(req.getScene()).getPath())
                .uid(uid)
                .build();
        return minIOService.getPreSignedObjectUrl(ossReq);
    }
}
