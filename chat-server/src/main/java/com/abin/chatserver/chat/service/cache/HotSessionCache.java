package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.common.constants.RedisKey;
import com.abin.chatserver.common.utils.RedisUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class HotSessionCache {

    /**
     * 更新热门群聊的最新消息时间
     */
    public void refreshActiveTime(Long sessionId, Date refreshTime) {
        RedisUtils.zAdd(RedisKey.getKey(RedisKey.HOT_SESSION_ZSET), sessionId, (double) refreshTime.getTime());
    }

    public Set<ZSetOperations.TypedTuple<String>> getSessionRange(Double hotStart, Double hotEnd) {
        return RedisUtils.zRangeByScoreWithScores(RedisKey.getKey(RedisKey.HOT_SESSION_ZSET), hotStart, hotEnd);
    }
}
