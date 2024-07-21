package com.abin.chatserver.chat.mapper;

import com.abin.chatserver.chat.domain.entity.Contact;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【contact(会话列表)】的数据库操作Mapper
* @createDate 2024-07-20 18:07:54
* @Entity com.abin.chatserver.chat.domain.entity.Contact
*/
public interface ContactMapper extends BaseMapper<Contact> {

    void refreshOrCreateActiveTime(@Param("sessionId") Long sessionId, @Param("uids") List<Long> uids,  @Param("msgId") Long msgId,  @Param("activeTime") Date activeTime);
}




