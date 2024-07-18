package com.abin.chatserver.chat.service.strategy.msg;

import java.util.HashMap;
import java.util.Map;

public class MsgHandlerFactory {

    private static final Map<Integer, AbstractMsgHandler> STRATEGY = new HashMap<>();

    public static void register(Integer type, AbstractMsgHandler handler) {
        STRATEGY.put(type, handler);
    }

    public static AbstractMsgHandler getHandler(Integer type) {
        return STRATEGY.get(type);
    }
}
