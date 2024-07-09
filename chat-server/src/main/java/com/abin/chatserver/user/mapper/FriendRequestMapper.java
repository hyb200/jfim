package com.abin.chatserver.user.mapper;

import com.abin.chatserver.user.domain.entity.FriendRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Yibin Huang
* @description 针对表【friend_request(好友申请表)】的数据库操作Mapper
* @createDate 2024-07-08 15:45:53
* @Entity com.abin.chatserver.user.domain.entity.FriendRequest
*/
@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

}




