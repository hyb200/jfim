package com.abin.chatserver.common.event;

import com.abin.chatserver.chat.domain.entity.GroupMember;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class GroupMemberAddEvent extends ApplicationEvent {

    private final List<GroupMember> members;

    private final SessionGroup sessionGroup;

    private final Long inviter;

    public GroupMemberAddEvent(Object source, SessionGroup sessionGroup, List<GroupMember> members, Long inviter) {
        super(source);
        this.sessionGroup = sessionGroup;
        this.members = members;
        this.inviter = inviter;
    }
}
