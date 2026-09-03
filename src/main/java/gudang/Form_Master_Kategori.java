/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gudang;

import com.mycompany.gudang.koneksi;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PagarKun
 */
public class Form_Master_Kategori extends javax.swing.JFrame { 
    koneksi kon = new koneksi(); 

    private Object[][] datakategori = null; 
    private String[] label = {"Id Kategori", "Nama Kategori"};
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Form_Master_Kategori.class.getName());

    /**
     * Creates new form Form_Master_Kategori
     */
   public Form_Master_Kategori() { 
        initComponents(); 
        if (kon.st == null) {
            kon.setkoneksi(); // Inisialisasi ulang jika Statement masih null
        }
        nonaktif(); 
        BacaTabelKategori(); 
    }
    
    private String NoKategori() { 
        String urutan = "AHI/KT/0001"; 
        try { 
            kon.setkoneksi();
            // Ambil id_kategori terakhir
            kon.rs = kon.st.executeQuery("SELECT id_kategori FROM tb_kategori ORDER BY id_kategori DESC LIMIT 1"); 
            if (kon.rs.next()) { 
                String lastId = kon.rs.getString("id_kategori");
                if (lastId != null && lastId.length() >= 4) {
                    int nomor = Integer.parseInt(lastId.substring(lastId.length() - 4)) + 1;
                    urutan = String.format("AHI/KT/%04d", nomor);
                }
            } 
            tid_kategori.setText(urutan); 
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(null, "Error NoKategori: " + e.getMessage()); 
        } 
        return urutan; 
    }
    
    private void BacaTabelKategori() { 
        try { 
            // Buat statement baru jika kon.st bernilai null
            if (kon.st == null) {
                kon.setkoneksi();
            }

            String sql = "SELECT * FROM tb_kategori ORDER BY id_kategori"; 
            kon.rs = kon.st.executeQuery(sql); 
            ResultSetMetaData m = kon.rs.getMetaData(); 
            int kolom = m.getColumnCount(); 
            int baris = 0; 

            while (kon.rs.next()) { 
                baris = kon.rs.getRow(); 
            } 

            datakategori = new Object[baris][kolom]; 
            int x = 0; 
            kon.rs.beforeFirst(); 

            while (kon.rs.next()) { 
                datakategori[x][0] = kon.rs.getString("id_kategori"); 
                datakategori[x][1] = kon.rs.getString("nm_kategori"); 
                x++; 
            } 

            tbl_kategori.setModel(new DefaultTableModel(datakategori, label)); 
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Error Baca Tabel: " + e.getMessage()); 
        } 
    }
    
    private void BacaTabelKategori2() { 
        try { 
            String sql = "select * from tb_kategori where nm_kategori like '%" + tcari.getText() + "%'"; 
            kon.rs = kon.st.executeQuery(sql); 
            ResultSetMetaData m = kon.rs.getMetaData(); 
            int kolom = m.getColumnCount(); 
            int baris = 0; 

            while (kon.rs.next()) { 
                baris = kon.rs.getRow(); 
            } 

            datakategori = new Object[baris][kolom]; 
            int x = 0; 
            kon.rs.beforeFirst(); 

            while (kon.rs.next()) { 
                datakategori[x][0] = kon.rs.getString("id_kategori"); 
                datakategori[x][1] = kon.rs.getString("nm_kategori"); 
                x++; 
            } 

            tbl_kategori.setModel(new DefaultTableModel(datakategori, label)); 
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, e); 
        } 
    }
    
    private void setTable() { 
        int row = tbl_kategori.getSelectedRow(); 
        tid_kategori.setText((String) tbl_kategori.getValueAt(row, 0)); 
        tnm_kategori.setText((String) tbl_kategori.getValueAt(row, 1)); 
    }
    
    private void BersihField() { 
        tnm_kategori.setText(""); 
        tid_kategori.setText(""); 
        tcari.setText(""); 
    }
    
    private void aktif() { 
        tnm_kategori.setEnabled(true); 
        tid_kategori.setEnabled(false); 
    }
    
    private void nonaktif() { 
        tnm_kategori.setEnabled(false); 
        tid_kategori.setEnabled(false); 
        bt_edit.setEnabled(false); 
        bt_update.setEnabled(false); 
        bt_hapus.setEnabled(false); 
        bt_simpan.setEnabled(false); 
    }
    
    
    // 1. Method Simpan Data Kategori
    private void SimpanData() {
        try {
            kon.setkoneksi();
            String sql = "INSERT INTO tb_kategori (id_kategori, nm_kategori) VALUES ('"
                    + tid_kategori.getText() + "', '"
                    + tnm_kategori.getText() + "')";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Kategori Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelKategori();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Menyimpan Data Kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. Method Edit/Update Data Kategori
    private void EditData() {
        try {
            kon.setkoneksi();
            String sql = "UPDATE tb_kategori SET "
                    + "nm_kategori='" + tnm_kategori.getText() + "' "
                    + "WHERE id_kategori='" + tid_kategori.getText() + "'";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Kategori Berhasil Diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelKategori();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Memperbarui Data Kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3. Method Hapus Data Kategori
    private void HapusData() {
        try {
            kon.setkoneksi();
            String sql = "DELETE FROM tb_kategori WHERE id_kategori='" + tid_kategori.getText() + "'";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Kategori Berhasil Dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelKategori();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Menghapus Data Kategori: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        tid_kategori = new javax.swing.JTextField();
        tnm_kategori = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        tcari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_kategori = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        bt_edit = new javax.swing.JButton();
        bt_update = new javax.swing.JButton();
        bt_tambah = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_batal = new javax.swing.JButton();
        bt_simpan = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 51, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tid_kategori.setText("Id Kategori");

        tnm_kategori.setText("Nama Kategori");
        tnm_kategori.addActionListener(this::tnm_kategoriActionPerformed);

        jLabel3.setText("Nama Kategori");

        jLabel4.setText("Id Kategori");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tid_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tid_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 520, 60));

        jPanel2.setBackground(new java.awt.Color(255, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tcari.setText("Cari Kategori");
        tcari.addActionListener(this::tcariActionPerformed);
        tcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tcariKeyTyped(evt);
            }
        });

        tbl_kategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_kategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_kategoriMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_kategori);

        jLabel5.setText("Cari Kategori");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tcari, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tcari, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, 520, 240));

        jPanel3.setBackground(new java.awt.Color(255, 51, 51));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        bt_edit.setText("Edit");
        bt_edit.addActionListener(this::bt_editActionPerformed);

        bt_update.setText("Update");
        bt_update.addActionListener(this::bt_updateActionPerformed);

        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(this::bt_tambahActionPerformed);

        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(this::bt_hapusActionPerformed);

        bt_batal.setText("Batal");
        bt_batal.addActionListener(this::bt_batalActionPerformed);

        bt_simpan.setText("Simpan");
        bt_simpan.addActionListener(this::bt_simpanActionPerformed);

        bt_keluar.setText("Tutup");
        bt_keluar.addActionListener(this::bt_keluarActionPerformed);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(bt_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(bt_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bt_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(bt_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_edit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bt_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 660, -1));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcariActionPerformed

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_updateActionPerformed
        bt_update.setEnabled(false); 
        bt_tambah.setEnabled(true); 
        EditData(); 
    }//GEN-LAST:event_bt_updateActionPerformed

    private void bt_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_simpanActionPerformed
          if (tnm_kategori.getText().isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Lengkapi Data", 
"Konfirmasi", JOptionPane.INFORMATION_MESSAGE); 
            bt_tambah.setEnabled(true); 
        } else { 
            bt_tambah.setEnabled(true); 
            bt_keluar.setEnabled(true); 
            SimpanData(); 
        }
    }//GEN-LAST:event_bt_simpanActionPerformed

    private void tcariKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tcariKeyTyped
        BacaTabelKategori2();
    }//GEN-LAST:event_tcariKeyTyped

    private void tbl_kategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_kategoriMouseClicked
        setTable(); 
        bt_hapus.setEnabled(true); 
        bt_edit.setEnabled(true); 
        bt_tambah.setEnabled(false);
    }//GEN-LAST:event_tbl_kategoriMouseClicked

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahActionPerformed
        BersihField(); 
        aktif(); 
        NoKategori(); 
        tnm_kategori.setEnabled(true); 
        tid_kategori.requestFocus(); 
        bt_batal.setEnabled(true); 
        bt_tambah.setEnabled(false); 
        bt_simpan.setEnabled(true); 
    }//GEN-LAST:event_bt_tambahActionPerformed

    private void bt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editActionPerformed
          aktif(); 
        bt_edit.setEnabled(false); 
        bt_update.setEnabled(true); 
        bt_batal.setEnabled(true); 
        bt_hapus.setEnabled(false); 
        bt_tambah.setEnabled(false);
    }//GEN-LAST:event_bt_editActionPerformed

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
       if (JOptionPane.showConfirmDialog(this, "yakin mau dihapus?", "konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { 
    HapusData(); 
    bt_tambah.setEnabled(true); 
} else { 
    JOptionPane.showMessageDialog(this, "Data Batal Dihapus", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE); 
    bt_tambah.setEnabled(true); 
    return; 
} 

    }//GEN-LAST:event_bt_hapusActionPerformed

    private void bt_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_batalActionPerformed
        nonaktif(); 
        BersihField(); 
        bt_tambah.setEnabled(true); 
    }//GEN-LAST:event_bt_batalActionPerformed

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_keluarActionPerformed
         this.dispose(); 
    }//GEN-LAST:event_bt_keluarActionPerformed

    private void tnm_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnm_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnm_kategoriActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Form_Master_Kategori().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_batal;
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_simpan;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JButton bt_update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_kategori;
    private javax.swing.JTextField tcari;
    private javax.swing.JTextField tid_kategori;
    private javax.swing.JTextField tnm_kategori;
    // End of variables declaration//GEN-END:variables
}
