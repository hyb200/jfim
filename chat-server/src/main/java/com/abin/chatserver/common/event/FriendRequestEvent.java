package com.abin.chatserver.common.event;

import com.abin.chatserver.user.domain.entity.FriendRequest;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FriendRequestEvent extends ApplicationEvent {

    private final FriendRequest friendRequest;

    public FriendRequestEvent(Object source, FriendRequest friendRequest) {
        super(source);
        this.friendRequest = friendRequest;
    }
}
