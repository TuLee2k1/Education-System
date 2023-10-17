/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.edusys.ui;

import com.edusys.dao.ChuyenDeDAO;
import com.edusys.dao.KhoaHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.KhoaHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.MsgBox;
import com.edusys.utils.XDate;
import com.edusys.utils.XImage;
import java.awt.HeadlessException;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class KhoaHocJDialog extends javax.swing.JDialog {
    ChuyenDeDAO cdDAO = new ChuyenDeDAO();
    KhoaHocDAO khDAO = new KhoaHocDAO();
    int row =0;
    /**
     * Creates new form KhoaHocJDialog
     */
    public KhoaHocJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setIconImage(XImage.getAppIcon());
        setLocation(400, 100);
        setTitle("Khoá Học");
        initComponents();
        fillComboBoxChuyenDe();
        btnThem.setEnabled(false);
    }

    void fillTable() {
        DefaultTableModel model = (DefaultTableModel) tblKhoaHoc.getModel();
        model.setRowCount(0);
        try {
            ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
            List<KhoaHoc> list = khDAO.selectKhoaHocByChuyenDe(chuyenDe.getMaCD());
            for (KhoaHoc kh : list) {
                Object[] row = {
                    kh.getMaKH(),
                    kh.getThoiLuong(),
                    kh.getHocPhi(),
                    XDate.toString(kh.getNgayKG(), "MM/dd/yyyy"),
                    kh.getMaNV(),
                    XDate.toString(kh.getNgayTao(), "MM/dd/yyyy")
                };
                model.addRow(row);
            }
        } 
        catch (Exception e) {
//            MsgBox.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void insert(){
        KhoaHoc model = getForm();
        model.setNgayTao(new Date());
        try {
            khDAO.insert(model);
            this.fillTable();
            this.clearForm();
            MsgBox.alert(this, "Thêm mới thành công!");
        } 
        catch (HeadlessException e) {
            MsgBox.alert(this, "Thêm mới thất bại!");
        }
    }

    void update(){
        KhoaHoc model = getForm();
        try {
            khDAO.update(model);
            this.fillTable();
            MsgBox.alert(this, "Cập nhật thành công!");
            this.fillTable();
        } 
        catch (Exception e) {
            MsgBox.alert(this, "Cập nhật thất bại!");
        }
    }

    void delete(){
        if(!Auth.isManager()){
            MsgBox.alert(this, "Bạn không có quyền xóa khóa học!");
        }
        else{
            if(MsgBox.confirm(this, "Bạn thực sự muốn xóa khóa học này?")){
                String makh = cboChuyenDe.getToolTipText();
                try {
                    khDAO.delete(makh);
                    this.fillTable();
                    this.clearForm();
                    MsgBox.alert(this, "Xóa thành công!");
                } 
                catch (Exception e) {
                    MsgBox.alert(this, "Xóa thất bại!");
                }
            }
        }
    }

        void clearForm() {
        txtMaCD.setText("");
        txtThoiLuong.setText("");
        txtHocPhi.setText("");
        txtGhiChu.setText("");
        txtNgayKG.setText("");
        txtNgayTao.setText("");
        txtMaNV.setText("");

        this.updateStatus();
        row = -1;
        updateStatus();
    }

    void edit() {
        ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
        Integer makh = (Integer) tblKhoaHoc.getValueAt(this.row, 0);
        KhoaHoc kh = khDAO.selectById(makh.toString());
        if(chuyenDe !=null){
        this.setForm(kh);
        this.updateStatus();
        tabs.setSelectedIndex(0);
        txtMaCD.setText(chuyenDe.getTenCD());
    }
    }
    void setForm(KhoaHoc kh){
        cboChuyenDe.setToolTipText(String.valueOf(kh.getMaKH()));
        cboChuyenDe.setSelectedItem(cddao.selectById(kh.getMaCD())); 
        txtNgayKG.setText(XDate.toString(kh.getNgayKG(), "MM/dd/yyyy"));
        txtHocPhi.setText(String.valueOf(kh.getHocPhi()));
        txtThoiLuong.setText(String.valueOf(kh.getThoiLuong()));
        txtMaNV.setText(kh.getMaNV());
        txtNgayTao.setText(XDate.toString(kh.getNgayTao(), "MM/dd/yyyy"));
        txtGhiChu.setText(kh.getGhiChu());
    }
    
    KhoaHoc getForm(){
        ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
        KhoaHoc kh = new KhoaHoc();
        kh.setMaCD(chuyenDe.getMaCD());
        kh.setNgayKG(XDate.toDate(txtNgayKG.getText(), "MM/dd/yyyy"));
        kh.setHocPhi(Double.valueOf(txtHocPhi.getText()));
        if(chuyenDe !=null){
        kh.setThoiLuong(Integer.valueOf(txtThoiLuong.getText()));
        }
        kh.setGhiChu(txtGhiChu.getText());
        kh.setMaNV(Auth.user.getMaNV());
        kh.setNgayTao(XDate.toDate(txtNgayTao.getText(), "MM/dd/yyyy"));
        kh.setMaKH(Integer.valueOf(cboChuyenDe.getToolTipText()));
        return kh;
    }

    void first(){
        this.row = 0;
        this.edit();
    }
    void prev(){
        if(this.row > 0){
            this.row--;
            this.edit();
        }
    }
    void next(){
        if(this.row < tblKhoaHoc.getRowCount() - 1){
            this.row++;
            this.edit();
        }
    }
    void last(){
         this.row = tblKhoaHoc.getRowCount() - 1;
        this.edit();
    }
    
    void updateStatus(){
        boolean edit = (this.row >= 0);
        boolean first = (this.row == 0);
        boolean last = (this.row == tblKhoaHoc.getRowCount() - 1);
        // Trạng thái form
        txtMaCD.setEditable(!edit);
        btnThem.setEnabled(!edit);
        btnCapNhat.setEnabled(edit);
        btnXoa.setEnabled(edit);
        
        btnFirst.setEnabled(edit && !first);
        btnPrev.setEnabled(edit && !first);
        btnNext.setEnabled(edit && !last);
        btnLast.setEnabled(edit && !last);
    }

    ChuyenDeDAO cddao = new ChuyenDeDAO();
    void fillComboBoxChuyenDe(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboChuyenDe.getModel();
        model.removeAllElements();
        List<ChuyenDe> list = cddao.selectALl();
        for(ChuyenDe cd : list){
             model.addElement(cd);
        }
    }
    void chonChuyenDe(){
        ChuyenDe chuyenDe = (ChuyenDe) cboChuyenDe.getSelectedItem();
        if(chuyenDe !=null){
        txtThoiLuong.setText(String.valueOf(chuyenDe.getThoiLuong()));
        txtHocPhi.setText(String.valueOf(chuyenDe.getHocPhi()));
        txtMaCD.setText(chuyenDe.getTenCD());
        txtGhiChu.setText(chuyenDe.getTenCD());
        }
        this.fillTable();
        this.row = -1;
        this.updateStatus();
        tabs.setSelectedIndex(1);
    }
    

    /**
     * 
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboChuyenDe = new javax.swing.JComboBox<>();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtMaCD = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtHocPhi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtThoiLuong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtNgayKG = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNgayTao = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        btnFirst = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhoaHoc = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("QUẢN LÝ KHOÁ HỌC ");

        jLabel2.setText("Chuyên đề:");

        cboChuyenDe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboChuyenDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboChuyenDeActionPerformed(evt);
            }
        });

        jLabel3.setText("Chuyên đề:");

        txtMaCD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaCDActionPerformed(evt);
            }
        });

        jLabel4.setText("Học phí:");

        jLabel5.setText("Thời lượng:");

        jLabel6.setText("Ngày khai giảng:");

        jLabel8.setText("Mã NV:");

        jLabel9.setText("Ngày tạo:");

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnXoa.setText("Xoá");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        jLabel10.setText("Ghi chú:");

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane2.setViewportView(txtGhiChu);

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icon/22.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icon/11.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icon/1.png"))); // NOI18N
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/edusys/icon/2.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtMaCD, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                                        .addComponent(txtHocPhi, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtThoiLuong, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(45, 45, 45)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9)
                                                .addComponent(jLabel8)
                                                .addComponent(jLabel6)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(73, 73, 73)
                                            .addComponent(txtNgayKG, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(btnFirst)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnNext)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnPrev)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnLast)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayKG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHocPhi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(11, 11, 11)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnThem)
                        .addComponent(btnCapNhat)
                        .addComponent(btnXoa)
                        .addComponent(btnMoi))
                    .addComponent(btnNext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnFirst, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPrev, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnLast, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬT", jPanel1);

        tblKhoaHoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã KH", "Thời lượng", "Học phí", "Ngày khai giảng", "Người tạo", "Ngày tạo"
            }
        ));
        tblKhoaHoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblKhoaHocMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblKhoaHoc);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 27, Short.MAX_VALUE))
        );

        tabs.addTab("DANH SÁCH", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tabs)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboChuyenDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboChuyenDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChuyenDeActionPerformed
        // TODO add your handling code here:
        chonChuyenDe();
    }//GEN-LAST:event_cboChuyenDeActionPerformed

    private void txtMaCDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaCDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaCDActionPerformed

    private void tblKhoaHocMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblKhoaHocMousePressed
        // TODO add your handling code here:
       if(evt.getClickCount() == 2){
            this.row = tblKhoaHoc.getSelectedRow();
            this.edit();
            btnThem.setEnabled(false);
        }
    }//GEN-LAST:event_tblKhoaHocMousePressed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
        btnThem.setEnabled(true);
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        first();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        next();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        prev();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        last();
    }//GEN-LAST:event_btnLastActionPerformed

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
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhoaHocJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                KhoaHocJDialog dialog = new KhoaHocJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChuyenDe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblKhoaHoc;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtHocPhi;
    private javax.swing.JTextField txtMaCD;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgayKG;
    private javax.swing.JTextField txtNgayTao;
    private javax.swing.JTextField txtThoiLuong;
    // End of variables declaration//GEN-END:variables
}
