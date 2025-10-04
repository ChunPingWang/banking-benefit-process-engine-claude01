package com.example.banking.benefit.domain.port.output;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 快取服務介面
 * Secondary Port - 輸出埠
 */
public interface CachePort {
    
    /**
     * 設定快取
     *
     * @param key 鍵值
     * @param value 內容
     * @param ttl 存活時間
     */
    void set(String key, Object value, Duration ttl);
    
    /**
     * 取得快取
     *
     * @param key 鍵值
     * @param clazz 回傳型別
     * @return 快取內容
     */
    <T> Optional<T> get(String key, Class<T> clazz);
    
    /**
     * 刪除快取
     *
     * @param key 鍵值
     */
    void delete(String key);
    
    /**
     * 批次刪除快取
     *
     * @param keys 鍵值列表
     */
    void deleteAll(List<String> keys);
    
    /**
     * 設定集合快取
     *
     * @param key 鍵值
     * @param values 內容集合
     * @param ttl 存活時間
     */
    void setSet(String key, Set<?> values, Duration ttl);
    
    /**
     * 取得集合快取
     *
     * @param key 鍵值
     * @param clazz 集合元素型別
     * @return 快取內容集合
     */
    <T> Set<T> getSet(String key, Class<T> clazz);
    
    /**
     * 新增元素到集合快取
     *
     * @param key 鍵值
     * @param value 新元素
     */
    void addToSet(String key, Object value);
    
    /**
     * 從集合快取移除元素
     *
     * @param key 鍵值
     * @param value 要移除的元素
     */
    void removeFromSet(String key, Object value);
    
    /**
     * 檢查快取是否存在
     *
     * @param key 鍵值
     * @return 是否存在
     */
    boolean exists(String key);
    
    /**
     * 取得快取剩餘存活時間
     *
     * @param key 鍵值
     * @return 剩餘存活時間
     */
    Optional<Duration> getTtl(String key);
    
    /**
     * 更新快取存活時間
     *
     * @param key 鍵值
     * @param ttl 新的存活時間
     */
    void updateTtl(String key, Duration ttl);
    
    /**
     * 清除所有快取
     */
    void clear();
}