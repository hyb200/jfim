package com.abin.chatserver.user.mapper;

import com.abin.chatserver.user.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Yibin Huang
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-07-08 15:45:50
* @Entity com.abin.chatserver.user.domain.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




