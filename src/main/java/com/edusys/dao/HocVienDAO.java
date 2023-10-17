/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entity.HocVien;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HocVienDAO extends EduSysDAO<HocVien, String>{
    final String INSERT_SQL =" insert into HocVien(MaKH,MaNH,Diem) VALUES(?,?,?)";
    final String UPDATE_SQL ="Update HocVien set MaKH= ?,MaNH= ?,Diem= ? WHERE MaHV = ?";
    final String DELETE_SQL ="DELETE from HocVien WHERE MaHV = ?";
    final String SELECT_ALL_SQL ="SELECT * FROM HocVien";
    final String SELECT_BY_ID_SQL ="SELECT * FROM HocVien where MaHV= ?";
    final String SELECT_BY_KHOA_HOC_SQL ="SELECT * FROM HocVien where MaKH= ?";
    @Override
    public void insert(HocVien entity) {
        JdbcHelper.Update(INSERT_SQL, entity.getMaKH(),entity.getMaNH(),entity.getDiem());
    }

    @Override
    public void update(HocVien entity) {
        JdbcHelper.Update(UPDATE_SQL,entity.getMaKH(),entity.getMaNH(),entity.getDiem(), entity.getMaHV());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.Update(DELETE_SQL,id);
    }

    @Override
    public List<HocVien> selectALl() {
         return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public HocVien selectById(String id) {
         List<HocVien> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<HocVien> selectBySql(String sql, Object... args) {
        List<HocVien> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()){
                HocVien entity = new HocVien();
                entity.setMaHV(rs.getInt("MaHV"));
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaNH(rs.getString("MaNH"));
                entity.setDiem(Double.parseDouble(rs.getString("Diem")));
           
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<HocVien> selectByKhoaHoc(int maKH) {
         return selectBySql(SELECT_BY_KHOA_HOC_SQL,maKH);
    }
    
}
