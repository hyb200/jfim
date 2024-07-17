package com.abin.chatserver.common.service.cache;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.common.utils.RedisUtils;
import org.springframework.data.util.Pair;

import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 旁路策略（模板方法）
 */
public abstract class AbstractRedisStringCache<K, V> implements BatchCache<K, V> {

    private Class<V> outClass;

    protected AbstractRedisStringCache() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.outClass = (Class<V>) genericSuperclass.getActualTypeArguments()[1];
    }

    protected abstract String getKey(K key);

    /**
     * 获取过期时间（单位：s）
     */
    protected abstract Long getExpireTime();

    protected abstract Map<K, V> load(List<K> keys);

    @Override
    public V get(K key) {
        return getBatch(Collections.singletonList(key)).get(key);
    }

    @Override
    public Map<K, V> getBatch(List<K> keys) {
        if (CollUtil.isEmpty(keys)) {
            return new HashMap<>();
        }
        keys = keys.stream().distinct().toList();
        List<String> keyLists = keys.stream().map(this::getKey).toList();
        //  查 redis
        List<V> values = RedisUtils.mget(keyLists, outClass);
        //  计算差值
        List<K> needLoadKeys = new ArrayList<>();
        for (int i = 0; i < values.size(); i ++ ) {
            if (Objects.isNull(values.get(i))) {
                needLoadKeys.add(keys.get(i));
            }
        }
        Map<K, V> load = new HashMap<>();
        if (CollUtil.isNotEmpty(needLoadKeys)) {
            //  查 DB
            load = load(needLoadKeys);
            Map<String, V> loadMap = load.entrySet().stream()
                    .map(o -> Pair.of(getKey(o.getKey()), o.getValue()))
                    .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            //  重新放回 redis
            RedisUtils.mset(loadMap, getExpireTime());
        }
        //  组装结果
        Map<K, V> result = new HashMap<>();
        for (int i = 0; i < keys.size(); i ++ ) {
            K k = keys.get(i);
            V v = Optional.ofNullable(values.get(i))
                    .orElse(load.get(k));
            result.put(k, v);
        }
        return result;
    }

    @Override
    public void delete(K key) {
        deleteBatch(Collections.singletonList(key));
    }

    @Override
    public void deleteBatch(List<K> keys) {
        List<String> keyList = keys.stream().map(this::getKey).toList();
        RedisUtils.del(keyList);
    }
}
