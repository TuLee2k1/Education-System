/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import java.util.List;

/**
 *
 * @author Admin
 */
public abstract class EduSysDAO<EntityType, KeyType> {
    public abstract void insert(EntityType entity);
    public abstract void update(EntityType entity);
    public abstract void delete (KeyType id);
    public abstract List<EntityType> selectALl();
    public abstract EntityType selectById(KeyType id);
    public abstract List<EntityType> selectBySql(String sql, Object...args);
}
