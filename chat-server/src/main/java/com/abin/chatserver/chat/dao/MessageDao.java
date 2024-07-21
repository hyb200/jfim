package com.abin.chatserver.chat.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.abin.chatserver.chat.domain.enums.MessageStatusEnum;
import com.abin.chatserver.chat.domain.vo.req.ChatMessagePageReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
* @author Yibin Huang
* @description 针对表【message(消息表)】的数据库操作
* @createDate 2024-07-17 23:23:22
*/
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

    public Integer getGapCount(Long sessionId, Long from, Long to) {
        return Math.toIntExact(lambdaQuery().eq(Message::getSessionId, sessionId)
                .gt(Message::getId, from)
                .le(Message::getId, to)
                .count());
    }

    public CursorPageBaseResp<Message> getCursorPage(Long sessionId, ChatMessagePageReq req, Long lastMsgId) {
        LambdaQueryChainWrapper<Message> wrapper = lambdaQuery();
        //  游标字段（快速定位索引的位置）
        wrapper.lt(StrUtil.isNotEmpty(req.getCursor()), Message::getId, req.getCursor());
        //  额外查询条件
        wrapper.eq(Message::getSessionId, sessionId);
        wrapper.eq(Message::getStatus, MessageStatusEnum.NORMAL.getStatus());
        wrapper.le(Objects.nonNull(lastMsgId), Message::getId, lastMsgId);
        //  游标方向
        wrapper.orderByDesc(Message::getId);
        Page<Message> page = wrapper.page(req.plusPage());

        String cursor = Optional.ofNullable(CollUtil.getLast(page.getRecords()))
                .map(Message::getId)
                .map(String::valueOf)
                .orElse(null);
        boolean isLast = page.getRecords().size() != req.getPageSize();
        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }
}




