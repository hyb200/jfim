package com.abin.chatserver.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class RedisUtils {

    private static final StringRedisTemplate redisTemplate;

    static {
        redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
    }

    public static <T> List<T> mget(Collection<String> keys, Class<T> tClass) {
        List<String> list = redisTemplate.opsForValue().multiGet(keys);
        if (Objects.isNull(list)) return new ArrayList<>();
        return list.stream().map(o -> toBeanOrNull(o, tClass)).toList();
    }

    private static <T> T toBeanOrNull(String json, Class<T> tClass) {
        return json == null ? null : JsonUtils.toObj(json, tClass);
    }

    public static <T> void mset(Map<String, T> map, long time) {
        Map<String, String> collect = map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> objToStr(e.getValue())));
        redisTemplate.opsForValue().multiSet(collect);
        map.forEach((k, v) -> expire(k, time));
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     */
    private static Boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
        return true;
    }

    private static String objToStr(Object o) {
        return JsonUtils.toStr(o);
    }

    public static void del(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public static void del(String ... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                Boolean result = redisTemplate.delete(keys[0]);
                log.debug("--------------------------------------------");
                log.debug("删除缓存：" + keys[0] + "，结果：" + result);
            } else {
                Set<String> keySet = new HashSet<>();
                for (String key : keys) {
                    Set<String> stringSet = redisTemplate.keys(key);
                    if (Objects.nonNull(stringSet) && !stringSet.isEmpty()) {
                        keySet.addAll(stringSet);
                    }
                }
                Long count = redisTemplate.delete(keySet);
                log.debug("--------------------------------------------");
                log.debug("成功删除缓存：" + keySet);
                log.debug("缓存删除数量：" + count + "个");
            }
            log.debug("--------------------------------------------");
        }
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true 成功  false 失败
     */
    public static Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, objToStr(value));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /*------------------zSet相关操作--------------------------------*/

    /**
     * 添加元素,有序集合是按照元素的score值由小到大排列
     */
    public static Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public static Boolean zAdd(String key, Object value, double score) {
        return zAdd(key, value.toString(), score);
    }

    public static Boolean zIsMember(String key, Object value) {
        return Objects.nonNull(redisTemplate.opsForZSet().score(key, value.toString()));
    }

    public static Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, Double st, Double ed) {
        if (Objects.isNull(st)) {
            st = Double.MIN_VALUE;
        }
        if (Objects.isNull(ed)) {
            ed = Double.MAX_VALUE;
        }
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, st, ed);
    }
}
