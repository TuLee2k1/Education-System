/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.dao;

import com.edusys.entity.NguoiHoc;
import com.edusys.utils.JdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NguoiHocDAO extends EduSysDAO<NguoiHoc, String>{
    final String INSERT_SQL ="insert into NguoiHoc(MaNH,HoTen,NgaySinh,GioiTinh,DienThoai,Email,GhiChu,MaNV,NgayDK) VALUES(?,?,?,?,?,?,?,?,?)";
    final String UPDATE_SQL ="UPDATE NguoiHoc SET HoTen= ?,NgaySinh= ?,GioiTinh= ?,DienThoai= ?,Email= ?,GhiChu= ?,MaNV= ?,NgayDK= ? WHERE MaNH= ?";
    final String DELETE_SQL ="DELETE from NguoiHoc WHERE MaNH = ?";
    final String SELECT_ALL_SQL ="SELECT * FROM NguoiHoc";
    final String SELECT_BY_ID_SQL ="SELECT * FROM NguoiHoc WHERE MaNH = ?";
    final String SELECT_NOT_IN_COURSE_SQL ="SELECT * FROM NguoiHoc WHERE HoTen like ? and MaNH not in(Select MaNH from HocVien where MaKH =?)";
    @Override
    public void insert(NguoiHoc entity) {
        JdbcHelper.Update(INSERT_SQL, entity.getMaNH(),entity.getHoTen(),entity.getNgaySinh(),entity.isGioiTinh(),entity.getDienThoai(),entity.getEmail(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayDK());
    }

    @Override
    public void update(NguoiHoc entity) {
        JdbcHelper.Update(UPDATE_SQL,entity.getHoTen(),entity.getNgaySinh(),entity.isGioiTinh(),entity.getDienThoai(),entity.getEmail(),entity.getGhiChu(),entity.getMaNV(),entity.getNgayDK(), entity.getMaNH());
    }

    @Override
    public void delete(String id) {
        JdbcHelper.Update(DELETE_SQL,id);
    }

    @Override
    public List<NguoiHoc> selectALl() {
        return selectBySql(SELECT_ALL_SQL);
    }

    @Override
    public NguoiHoc selectById(String id) {
         List<NguoiHoc> list = selectBySql(SELECT_BY_ID_SQL, id);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<NguoiHoc> selectBySql(String sql, Object... args) {
        List<NguoiHoc> list = new ArrayList<>();
        try {
            ResultSet rs = JdbcHelper.query(sql, args);
            while(rs.next()){
                NguoiHoc entity = new NguoiHoc();
                entity.setMaNH(rs.getString("MaNH"));
                entity.setHoTen(rs.getString("HoTen"));
                entity.setNgaySinh(rs.getDate("NgaySinh"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setDienThoai(rs.getString("DienThoai"));
                entity.setEmail(rs.getString("Email"));
                entity.setGhiChu(rs.getString("GhiChu"));
                entity.setMaNV(rs.getString("MaNV"));
                entity.setNgayDK(rs.getDate("NgayDK"));
                list.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<NguoiHoc> selectNotInCourse(int maKH, String keyword) {
        return selectBySql(SELECT_NOT_IN_COURSE_SQL,"%"+keyword+"%",maKH);
    }
    
}
