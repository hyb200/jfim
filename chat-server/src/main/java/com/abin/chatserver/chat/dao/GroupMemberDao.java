package com.abin.chatserver.chat.dao;

import com.abin.chatserver.chat.domain.enums.GroupRoleEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.GroupMember;
import com.abin.chatserver.chat.mapper.GroupMemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
* @author Yibin Huang
* @description 针对表【group_member(群成员表)】的数据库操作
* @createDate 2024-07-17 21:02:33
*/
@Service
public class GroupMemberDao extends ServiceImpl<GroupMemberMapper, GroupMember> {

    public GroupMember getMember(Long id, Long uid) {
        return lambdaQuery().eq(GroupMember::getGroupId, id)
                .eq(GroupMember::getUid, uid)
                .one();
    }

    public boolean isManager(Long sessionId, Long uid) {
        GroupMember member = lambdaQuery().eq(GroupMember::getGroupId, sessionId)
                .eq(GroupMember::getUid, uid)
                .eq(GroupMember::getRole, GroupRoleEnum.MANAGER.getType())
                .one();
        return Objects.nonNull(member);
    }

    public List<Long> getMemberUids(Long groupId) {
        List<GroupMember> groupMembers = lambdaQuery().eq(GroupMember::getGroupId, groupId)
                .select(GroupMember::getUid)
                .list();
        return groupMembers.stream().map(GroupMember::getUid).toList();
    }
}




