package com.abin.chatserver.common.service.cache;

import java.util.List;
import java.util.Map;

public interface BatchCache<K, V> {

    /**
     * 获取单个
     */
    V get(K key);

    /**
     * 获取批量
     */
    Map<K, V> getBatch(List<K> keys);

    /**
     * 删除单个
     */
    void delete(K key);

    /**
     * 删除多个
     */
    void deleteBatch(List<K> keys);
}
