/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entity.NhanVien;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NhanVienDAO extends EduSysDAO<NhanVien, String>{
    final String INSERT_SQL ="insert into NhanVien(MaNV,MatKhau,HoTen,VaiTro) VALUES(?,?,?,?)";
    final String UPDATE_SQL ="UPDATE NhanVien SET MatKhau=?,HoTen=?,VaiTro=? WHERE MaNV=?";
    final String DELETE_SQL ="DELETE from NhanVien WHERE MaNV = ?";
    final String SELECT_ALL_SQL ="SELECT * FROM NhanVien";
    final String SELECT_BY_ID_SQL ="SELECT * FROM NhanVien WHERE MaNV = ?";

    @Override
    public void insert(NhanVien entity) {
        JdbcHelper.Update(INSERT_SQL, entity.getMaNV(),entity.getMatKhau(),entity.getHoTen(),entity.isVaiTro());
    }

    @Override
    public void update(NhanVien entity) {
        JdbcHelper.Update(UPDATE_SQL,entity.getMatKhau(),entity.getHoTen(),entity.isVaiTro(),entity.getMaNV());
    }

    @Override
    public void delete(String id) {
         JdbcHelper.Update(DELETE_SQL,id);
    }

    @Override
    public List<NhanVien> selectALl() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()){
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString("MaNV"));
                entity.setMatKhau(rs.getString("MatKhau"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setVaiTro(rs.getBoolean("VaiTro"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    
}
