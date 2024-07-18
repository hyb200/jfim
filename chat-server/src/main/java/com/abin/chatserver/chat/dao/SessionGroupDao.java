package com.abin.chatserver.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import com.abin.chatserver.chat.mapper.SessionGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author Yibin Huang
* @description 针对表【session_group(群聊表)】的数据库操作
* @createDate 2024-07-17 20:41:14
*/
@Service
public class SessionGroupDao extends ServiceImpl<SessionGroupMapper, SessionGroup> {

}




