package com.example.banking.benefit.domain.repository;

import java.util.Optional;
import java.util.List;

/**
 * 基礎儲存庫介面
 *
 * @param <T> 實體類型
 * @param <ID> ID類型
 */
public interface BaseRepository<T, ID> {
    
    /**
     * 根據ID查詢實體
     *
     * @param id ID
     * @return 實體
     */
    Optional<T> findById(ID id);
    
    /**
     * 查詢所有實體
     *
     * @return 實體列表
     */
    List<T> findAll();
    
    /**
     * 儲存實體
     *
     * @param entity 實體
     * @return 已儲存的實體
     */
    T save(T entity);
    
    /**
     * 刪除實體
     *
     * @param entity 實體
     */
    void delete(T entity);
    
    /**
     * 根據ID刪除實體
     *
     * @param id ID
     */
    void deleteById(ID id);
    
    /**
     * 檢查ID是否存在
     *
     * @param id ID
     * @return 是否存在
     */
    boolean existsById(ID id);
    
    /**
     * 取得實體數量
     *
     * @return 實體數量
     */
    long count();
}