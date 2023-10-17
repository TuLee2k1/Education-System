/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.edusys.ui;

import com.edusys.dao.NhanVienDAO;
import com.edusys.dao.ThongKeDAO;
import com.edusys.entity.NhanVien;
import java.util.List;

/**
 *
 * @author Admin
 */
public class TestDemo {
    public static void main(String[] args) {
        ThongKeDAO tkdao = new ThongKeDAO();
    List<Object[]> list = tkdao.getDoanhThu(2018);

    for (Object[] obj : list) {
        if (obj.length >= 2) {
            System.out.println("-->" + obj[0] + "--" + obj[1]+"--"+obj[2]);
        } else {
            System.out.println("Invalid array with fewer than two elements");
        }
    }


//        for (Object[] obj : list) {
//    if (obj.length >= 2) {
//        System.out.println("-->" + obj[0] + "--" + obj[1]);
//    } else {
//        System.out.println("Invalid array with fewer than two elements");
//    }
}


//        for (Object[] obj : list) {
//            System.out.println("-->"+obj[0]+"--"+obj[1]);
//        }
//        NhanVienDAO dao = new NhanVienDAO();
//        dao.insert(new NhanVien("Tu", "NGuyễn Đình Tú", "123", true));
//        List<NhanVien> ls = dao.selectALl();
//        for(NhanVien nv:ls){
//            System.out.println("-->"+nv.toString());
//        }
    }
    

