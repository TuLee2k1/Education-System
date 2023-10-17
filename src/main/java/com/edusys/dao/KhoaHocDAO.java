/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entity.KhoaHoc;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class KhoaHocDAO extends EduSysDAO<KhoaHoc, String>{
    final String INSERT_SQL ="insert into KhoaHoc(MaCD,HocPhi,ThoiLuong,NgayKG,GhiChu,MaNV,NgayTao) VALUES(?,?,?,?,?,?,?)";
    final String UPDATE_SQL ="UPDATE KhoaHoc SET MaCD= ?,HocPhi= ?,ThoiLuong= ?,NgayKG= ?,GhiChu= ?,MaNV= ?,NgayTao= ? WHERE MaKH = ?";
    final String DELETE_SQL ="DELETE from KhoaHoc WHERE MaKH = ?";
    final String SELECT_ALL_SQL ="SELECT * FROM KhoaHoc";
    final String SELECT_BY_ID_SQL ="SELECT * FROM KhoaHoc WHERE MaKH = ?";
    final String SELECT_MA_CD_SQL ="SELECT * FROM KhoaHoc WHERE MaCD = ?";
    @Override
    public void insert(KhoaHoc entity) {
        JdbcHelper.Update(INSERT_SQL,entity.getMaCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayTao());
    }

    @Override
    public void update(KhoaHoc entity) {
        JdbcHelper.Update(UPDATE_SQL,entity.getMaCD(),entity.getHocPhi(),entity.getThoiLuong(),entity.getNgayKG(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayTao(), entity.getMaKH());
    }

    @Override
    public void delete(String id) {
         JdbcHelper.Update(DELETE_SQL,id);
    }

    @Override
    public List<KhoaHoc> selectALl() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public KhoaHoc selectById(String id) {
        List<KhoaHoc> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<KhoaHoc> selectBySql(String sql, Object... args) {
        List<KhoaHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()){
                KhoaHoc entity = new KhoaHoc();
                entity.setMaKH(rs.getInt("MaKH"));
                entity.setMaCD(rs.getString("MaCD"));
                entity.setHocPhi(Float.parseFloat(rs.getString("HocPhi")));
                entity.setThoiLuong(rs.getInt("ThoiLuong"));
                entity.setNgayKG(rs.getDate("NgayKG"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayTao(rs.getDate("NgayTao"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<KhoaHoc> selectKhoaHocByChuyenDe(String maCD) {
        return selectBySql(SELECT_MA_CD_SQL,maCD);
    }
    
    public List<Integer> selectYear(){
        String sql = "select distinct Year(NgayKG) Year from KhoaHoc order by Year desc";
        List<Integer> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql);
            while(rs.next()){
                list.add(rs.getInt(1));
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
