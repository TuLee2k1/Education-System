/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.edusys.ui;

import com.edusys.dao.ChuyenDeDAO;
import com.edusys.dao.HocVienDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.dao.NguoiHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.HocVien;
import com.edusys.entity.KhoaHoc;
import com.edusys.entity.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XImage;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class HocVienJDialog extends javax.swing.JDialog {
    ChuyenDeDAO cdDAO = new ChuyenDeDAO();
    KhoaHocDAO khDAO = new KhoaHocDAO();
    NguoiHocDAO nhDAO = new NguoiHocDAO();
    HocVienDAO hvDAO = new HocVienDAO();
    /**
     * Creates new form HocVienJDialog
     */
    public HocVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setIconImage(XImage.getAppIcon());
        setLocationRelativeTo(null);
        setTitle("EDUSYS Quản Lí Học Viên");
        setLocation(350, 50);
        initComponents();
        fillComboBoxChuyenDe();
        this.selectTabs(0);
    }
    
    void fillComboBoxChuyenDe(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)cboChuyenDe.getModel();
        model.removeAllElements();
        try {
            List<ChuyenDe> list = cdDAO.selectALl();
            for (ChuyenDe cd : list) {
                model.addElement(cd);
            }
            fillComboBoxKhoaHoc();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    void fillComboBoxKhoaHoc(){
        DefaultComboBoxModel model = (DefaultComboBoxModel)cboKhoaHoc.getModel();
        model.removeAllElements();
        try {
            ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
            if(chuyenDe != null){
                List<KhoaHoc> list = khDAO.selectKhoaHocByChuyenDe(chuyenDe.getMaCD());
                for(KhoaHoc khoahoc:list){
                    model.addElement(khoahoc);
                }
            }
            fillTableHocVien();
        } catch (Exception e) {
        }
    }
    
    public void fillTableNguoiHoc(){
        DefaultTableModel model = (DefaultTableModel)tblNguoiHoc.getModel();
        model.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            if(kh != null){
                System.out.println("MaKH: "+kh.getMaKH());
                String keyword = txtFind.getText();
                List<NguoiHoc> list = nhDAO.selectNotInCourse(kh.getMaKH(), keyword);
                for (NguoiHoc nh : list) {
                    Object[] row ={
                        nh.getMaNH(),nh.getHoTen(),nh.getNgaySinh(),nh.isGioiTinh()?"Nam":"Nữ",
                        nh.getDienThoai(),nh.getEmail()
                    };
                    model.addRow(row);
                }
            }
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!filltableNguoiHoc");
        }
    }
    
    public void fillTableHocVien(){
        DefaultTableModel model = (DefaultTableModel)tblHocVien.getModel();
        model.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cboKhoaHoc.getSelectedItem();
            if(kh != null){
                System.out.println("fillTableHocVien MaKH: "+kh.getMaKH());
                List<HocVien> list = hvDAO.selectByKhoaHoc(kh.getMaKH());
                System.out.println("list: "+list.size());
                for(int i =0; i<list.size();i++){
                    HocVien hv = list.get(i);
                    String hoTen = nhDAO.selectById(hv.getMaNH()).getHoTen();
                    Object[] row = {
                        i+1,hv.getMaHV(),hv.getMaNH(),hoTen,hv.getDiem()
                    };
                    model.addRow(row);
                }
            }
            fillTableNguoiHoc();
        } catch (Exception e) {
            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }
    
    void AddHocVien(){
        KhoaHoc khoaHoc = (KhoaHoc) cboKhoaHoc.getSelectedItem();
        for(int row: tblNguoiHoc.getSelectedRows()){
            HocVien hv = new HocVien();
            hv.setMaKH(khoaHoc.getMaKH());
            hv.setDiem(0);
            hv.setMaNH((String) tblNguoiHoc.getValueAt(row, 0));
            System.out.println("=>"+hv.getMaKH()+"-"+hv.getMaNH()+"-"+hv.getDiem());
            hvDAO.insert(hv);
        }
        fillTableHocVien();
        tab.setSelectedIndex(0);
    }
    
    void removeHocVien(){
        if(!Auth.isManager()){
            MsgBox.alert(this, "Bạn không đủ quyền để xoá học viên!");
        }else{
            if(MsgBox.confirm(this, "Bạn muốn xoá học viên này?")){
                for(int row:tblHocVien.getSelectedRows()){
                    Integer maHV = (Integer)tblHocVien.getValueAt(row, 1);
                    hvDAO.delete(maHV.toString());
                }
                MsgBox.alert(this, "Xoá thành công!");
                fillTableHocVien();
            }
        }
    }
    
    void updateDiem(){
        for(int i=0; i<tblHocVien.getRowCount();i++){
            String maHV = String.valueOf(tblHocVien.getValueAt(i, 1));
            HocVien hocVien = hvDAO.selectById(maHV);
            double diem = Double.parseDouble(String.valueOf(tblHocVien.getValueAt(i, 4)));
            hocVien.setDiem(diem);
            hvDAO.update(hocVien);
        }
        MsgBox.alert(this, "Cập nhật thành công!");
        fillTableHocVien();
    }
    
        public void selectTabs(int index){
        tab.setSelectedIndex(index);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cboChuyenDe = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        cboKhoaHoc = new javax.swing.JComboBox<>();
        tab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHocVien = new javax.swing.JTable();
        btnRemoveHocVien = new javax.swing.JButton();
        btnUpdateDiem = new javax.swing.JButton();
        tabs = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNguoiHoc = new javax.swing.JTable();
        btnAddHocVien1 = new javax.swing.JButton();
        btnAddHocVien = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Chuyên đề:");

        cboChuyenDe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        jLabel2.setText("Khoá học:");

        cboKhoaHoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tblHocVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Mã HV", "Mã NH", "Học và Tên", "Điểm"
            }
        ));
        jScrollPane2.setViewportView(tblHocVien);
        if (tblHocVien.getColumnModel().getColumnCount() > 0) {
            tblHocVien.getColumnModel().getColumn(0).setResizable(false);
            tblHocVien.getColumnModel().getColumn(0).setPreferredWidth(15);
            tblHocVien.getColumnModel().getColumn(1).setResizable(false);
            tblHocVien.getColumnModel().getColumn(1).setPreferredWidth(15);
            tblHocVien.getColumnModel().getColumn(2).setResizable(false);
            tblHocVien.getColumnModel().getColumn(2).setPreferredWidth(15);
            tblHocVien.getColumnModel().getColumn(3).setResizable(false);
            tblHocVien.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblHocVien.getColumnModel().getColumn(4).setResizable(false);
            tblHocVien.getColumnModel().getColumn(4).setPreferredWidth(15);
        }

        btnRemoveHocVien.setText("Xoá học viên");
        btnRemoveHocVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveHocVienActionPerformed(evt);
            }
        });

        btnUpdateDiem.setText("Cập nhật điểm");
        btnUpdateDiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRemoveHocVien, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnUpdateDiem)
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateDiem)
                    .addComponent(btnRemoveHocVien))
                .addGap(12, 12, 12))
        );

        tab.addTab("HỌC VIÊN", jPanel1);

        jLabel3.setText("Tìm kiếm:");

        tblNguoiHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ma NH", "Họ và Tên", "Ngày sinh", "Gioi tinh", "Điện thoại", "Email"
            }
        ));
        jScrollPane1.setViewportView(tblNguoiHoc);

        btnAddHocVien1.setText("Thêm học viên vào khoá học");

        btnAddHocVien.setText("Thêm học viên vào khoá học");
        btnAddHocVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHocVienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tabsLayout = new javax.swing.GroupLayout(tabs);
        tabs.setLayout(tabsLayout);
        tabsLayout.setHorizontalGroup(
            tabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFind)
                    .addGroup(tabsLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabsLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddHocVien)
                .addGap(39, 39, 39))
            .addGroup(tabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabsLayout.createSequentialGroup()
                    .addGap(199, 199, 199)
                    .addComponent(btnAddHocVien1)
                    .addContainerGap(199, Short.MAX_VALUE)))
        );
        tabsLayout.setVerticalGroup(
            tabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tabsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAddHocVien)
                .addContainerGap(12, Short.MAX_VALUE))
            .addGroup(tabsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(tabsLayout.createSequentialGroup()
                    .addGap(238, 238, 238)
                    .addComponent(btnAddHocVien1)
                    .addContainerGap(199, Short.MAX_VALUE)))
        );

        tab.addTab("NGƯỜI HỌC", tabs);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tab)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboKhoaHoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tab, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        // TODO add your handling code here:
        fillComboBoxKhoaHoc();
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void btnRemoveHocVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveHocVienActionPerformed
        // TODO add your handling code here:
        removeHocVien();
    }//GEN-LAST:event_btnRemoveHocVienActionPerformed

    private void btnUpdateDiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDiemActionPerformed
        // TODO add your handling code here:
        updateDiem();
    }//GEN-LAST:event_btnUpdateDiemActionPerformed

    private void btnAddHocVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddHocVienActionPerformed
        // TODO add your handling code here:
        AddHocVien();
    }//GEN-LAST:event_btnAddHocVienActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HocVienJDialog dialog = new HocVienJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddHocVien;
    private javax.swing.JButton btnAddHocVien1;
    private javax.swing.JButton btnRemoveHocVien;
    private javax.swing.JButton btnUpdateDiem;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JComboBox<String> cboKhoaHoc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tab;
    private javax.swing.JPanel tabs;
    private javax.swing.JTable tblHocVien;
    private javax.swing.JTable tblNguoiHoc;
    private javax.swing.JTextField txtFind;
    // End of variables declaration//GEN-END:variables
}
