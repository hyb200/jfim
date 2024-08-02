package com.abin.chatserver.chat.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.enums.MessageStatusEnum;
import com.abin.chatserver.common.utils.CursorUtils;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.Contact;
import com.abin.chatserver.chat.mapper.ContactMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
* @author Yibin Huang
* @description 针对表【contact(会话列表)】的数据库操作
* @createDate 2024-07-20 18:07:54
*/
@Service
public class ContactDao extends ServiceImpl<ContactMapper, Contact> {

    public void refreshOrCreateActiveTime(Long sessionId, List<Long> uids, Long msgId, Date createTime) {
        baseMapper.refreshOrCreateActiveTime(sessionId, uids, msgId, createTime);
    }

    public Contact get(Long receiverUid, Long sessionId) {
        return lambdaQuery()
                .eq(Contact::getUid, receiverUid)
                .eq(Contact::getSessionId, sessionId)
                .one();
    }

    public CursorPageBaseResp<Contact> getContactPage(Long uid, CursorPageBaseReq req) {
        LambdaQueryChainWrapper<Contact> wrapper = lambdaQuery();
        //  游标字段（快速定位索引的位置）
        wrapper.lt(StrUtil.isNotEmpty(req.getCursor()), Contact::getActiveTime, CursorUtils.parserCursor(req.getCursor(), Date.class));
        //  额外查询条件
        wrapper.eq(Contact::getUid, uid);
        //  游标方向
        wrapper.orderByDesc(Contact::getActiveTime);
        Page<Contact> page = wrapper.page(req.plusPage());

        String cursor = Optional.ofNullable(CollUtil.getLast(page.getRecords()))
                .map(Contact::getActiveTime)
                .map(CursorUtils::toCursor)
                .orElse(null);
        boolean isLast = page.getRecords().size() != req.getPageSize();

        return new CursorPageBaseResp<>(cursor, isLast, page.getRecords());
    }

    public List<Contact> getBySessionIds(List<Long> sessionIds, Long uid) {
        return lambdaQuery()
                .in(Contact::getSessionId, sessionIds)
                .eq(Contact::getUid, uid)
                .list();
    }

    /**
     * 根据会话ID删除会话
     *
     * @param sessionId  会话ID
     * @param uids 群成员列表
     * @return 是否删除成功
     */
    public Boolean removeBySessionId(Long sessionId, List<Long> uids) {
        LambdaQueryWrapper<Contact> wrapper = new QueryWrapper<Contact>().lambda()
                .eq(Contact::getSessionId, sessionId)
                .in(CollUtil.isNotEmpty(uids), Contact::getUid, uids);
        return remove(wrapper);
    }
}




