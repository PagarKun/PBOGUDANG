/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gudang;

import com.mycompany.gudang.koneksi;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PagarKun
 */
public class Form_Master_Barang extends javax.swing.JFrame {
     koneksi kon=new koneksi();     
     private Object [][] dataproduk=null;     
     private String[]label={"KODE BARANG","KATEGORI","NAMA BARANG","STOK","MIN.STOK"}; 
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Form_Master_Barang.class.getName());

    /**
     * Creates new form Form_Master_Barang
     */
      public Form_Master_Barang() {
        initComponents();
        kon.setkoneksi();
        nonaktif();
        BacaTabelBarang();
        isiNamaKategori();
        BersihField();                 // Set nilai awal 0 untuk stok & min stok
        tid_kategori.setVisible(true);  // Tampilkan field kategori sejak awal
    } 

         private void setTable(){         
             int row=tbl_brg.getSelectedRow();         
             tkd_barang.setText((String)tbl_brg.getValueAt(row,0));         
             tid_kategori.setText((String)tbl_brg.getValueAt(row,1));         
             tnm_barang.setText((String)tbl_brg.getValueAt(row,2));         
             tstok.setText((String)tbl_brg.getValueAt(row,3));         
             tmin_stok.setText((String)tbl_brg.getValueAt(row,4));     }
         
          private void BersihField(){         
           tkd_barang.setText("");
            tid_kategori.setText("");
            tnm_barang.setText("");
            tstok.setText("0");      // Isi dengan "0" agar ukuran TextField tetap terjaga
            tmin_stok.setText("0");  // Isi dengan "0" agar ukuran TextField tetap terjaga
            tcari.setText("");
            if (cbkategori.getItemCount() > 0) {
                cbkategori.setSelectedIndex(0);
            }
          } 
          
            private void aktif(){         
                tkd_barang.setEnabled(true);         
                tnm_barang.setEnabled(true);
                tstok.setEnabled(true);         
                tmin_stok.setEnabled(true);         
                cbkategori.setEnabled(true);     } 
            
             private void nonaktif() {
                tkd_barang.setEnabled(false);
                tnm_barang.setEnabled(false);
                tstok.setEnabled(false);
                tmin_stok.setEnabled(false);
                bt_edit.setEnabled(false);
                bt_update.setEnabled(false);
                bt_hapus.setEnabled(false);
                bt_simpan.setEnabled(false);
                cbkategori.setEnabled(false);
                tid_kategori.setEnabled(false); // Kunci textfield agar tidak disunting manual
                tid_kategori.setVisible(true);  // Pastikan selalu terlihat di layar
            }   
              
               private void isiNamaKategori(){         
                   try {
                        kon.setkoneksi();
                        // Lepas listener sementara agar tidak crash saat removeAllItems
                        if (cbkategori.getActionListeners().length > 0) {
                            cbkategori.removeActionListener(cbkategori.getActionListeners()[0]);
                        }

                        cbkategori.removeAllItems();
                        cbkategori.addItem("=PILIH="); // Opsi default agar aman

                        String sql = "SELECT nm_kategori FROM tb_kategori ORDER BY nm_kategori ASC";
                        kon.rs = kon.st.executeQuery(sql);
                        while (kon.rs.next()) {
                            cbkategori.addItem(kon.rs.getString("nm_kategori"));
                        }

                        // Pasang kembali listener setelah item selesai dimasukkan
                        cbkategori.addActionListener(this::cbkategoriActionPerformed);
                        cbkategori.setSelectedIndex(0);
                    } catch (Exception e) {
                        System.out.println("Gagal memuat kategori: " + e.getMessage());
                    }
                }
               
               private String NoBarang() {
                    String urutan = "AHI/KB/0001"; // Default awal jika tabel masih kosong
                    try {
                        kon.setkoneksi();
                        // Query dipastikan mengambil kolom kd_brg dari tb_barang
                        String sql = "SELECT kd_brg FROM tb_barang WHERE kd_brg LIKE 'AHI/KB/%' ORDER BY kd_brg DESC LIMIT 1";
                        kon.rs = kon.st.executeQuery(sql);

                        if (kon.rs.next()) {
                            String lastKd = kon.rs.getString("kd_brg");
                            // Mengambil substring angka setelah garis miring (/) terakhir
                            String stringAngka = lastKd.substring(lastKd.lastIndexOf("/") + 1);
                            int nomor = Integer.parseInt(stringAngka) + 1;

                            // Format kembali menjadi 4 digit angka (misal: 0002, 0003, dst)
                            urutan = "AHI/KB/" + String.format("%04d", nomor);
                        }

                        tkd_barang.setText(urutan);
                    } catch (Exception e) {
                        System.out.println("Error penomoran barang: " + e.getMessage());
                        tkd_barang.setText(urutan);
                    }
                    return urutan;
                }
               
                private void BacaTabelBarang() {
                    try {
                      String sql = "Select *From tb_barang order by kd_brg";
                      kon.rs = kon.st.executeQuery(sql);
                      ResultSetMetaData m = kon.rs.getMetaData();
                      int kolom = m.getColumnCount();
                      int baris = 0;
                      while (kon.rs.next()) {
                        baris = kon.rs.getRow();
                      }
                      dataproduk = new Object[baris][kolom];
                      int x = 0;
                      kon.rs.beforeFirst();
                      while (kon.rs.next()) {
                        dataproduk[x][0] = kon.rs.getString("kd_brg");
                        dataproduk[x][1] = kon.rs.getString("nm_kategori");
                        dataproduk[x][2] = kon.rs.getString("nm_brg");
                        dataproduk[x][3] = kon.rs.getString("stok");
                        dataproduk[x][4] = kon.rs.getString("min_stok");
                        x++;
                      }
                      tbl_brg.setModel(new DefaultTableModel(dataproduk, label));
                    } catch (SQLException e) {
                      JOptionPane.showMessageDialog(null, e);
                    }
                  }
                
  private void BacaTabelBarang2() {
    try {
      String sql = "Select *From tb_barang where kd_brg like '%" + tcari.getText() + "%'" + "or nm_kategori like '%" + tcari.getText() + "%'" + "or nm_brg like '%" + tcari.getText() + "%'";
      kon.rs = kon.st.executeQuery(sql);
      ResultSetMetaData m = kon.rs.getMetaData();
      int kolom = m.getColumnCount();
      int baris = 0;
      while (kon.rs.next()) {
        baris = kon.rs.getRow();
      }
      dataproduk = new Object[baris][kolom];
      int x = 0;
      kon.rs.beforeFirst();
      while (kon.rs.next()) {
        dataproduk[x][0] = kon.rs.getString("kd_brg");
        dataproduk[x][1] = kon.rs.getString("nm_kategori");
        dataproduk[x][2] = kon.rs.getString("nm_brg");
        dataproduk[x][3] = kon.rs.getString("stok");
        dataproduk[x][4] = kon.rs.getString("min_stok");
        x++;
      }
      tbl_brg.setModel(new DefaultTableModel(dataproduk, label));
    } catch (SQLException e) {
      JOptionPane.showMessageDialog(null, e);
    }
  }
  void isiNamaKategori2() {
    try {
      kon.setkoneksi();
      String sql = "Select *From tb_kategori where nm_kategori='" + tid_kategori.getText() + "'";
      kon.rs = kon.st.executeQuery(sql);
      if (kon.rs.next()) {
        cbkategori.setSelectedItem(kon.rs.getString("nm_kategori"));
      }
    } catch (SQLException e) {
      System.out.println("Koneksi Gagal" + e.toString());
    }
  }
    
    // 1. Method Simpan Data Baru ke Database
   // 1. Method Simpan Data Baru (Tanpa id_kategori)
    private void SimpanData() {
        try {
            kon.setkoneksi();
            String sql = "INSERT INTO tb_barang (kd_brg, nm_kategori, nm_brg, stok, min_stok) VALUES ('"
                    + tkd_barang.getText() + "', '"
                    + cbkategori.getSelectedItem() + "', '"
                    + tnm_barang.getText() + "', '"
                    + tstok.getText() + "', '"
                    + tmin_stok.getText() + "')";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelBarang();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Menyimpan Data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 2. Method Update/Edit Data (Tanpa id_kategori)
    private void EditData() {
        try {
            kon.setkoneksi();
            String sql = "UPDATE tb_barang SET "
                    + "nm_kategori='" + cbkategori.getSelectedItem() + "', "
                    + "nm_brg='" + tnm_barang.getText() + "', "
                    + "stok='" + tstok.getText() + "', "
                    + "min_stok='" + tmin_stok.getText() + "' "
                    + "WHERE kd_brg='" + tkd_barang.getText() + "'";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Berhasil Diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelBarang();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Memperbarui Data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 3. Method Hapus Data
    private void HapusData() {
        try {
            kon.setkoneksi();
            String sql = "DELETE FROM tb_barang WHERE kd_brg='" + tkd_barang.getText() + "'";

            kon.st.executeUpdate(sql);
            JOptionPane.showMessageDialog(this, "Data Berhasil Dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            BacaTabelBarang();
            nonaktif();
            BersihField();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal Menghapus Data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jScrollBar1 = new javax.swing.JScrollBar();
        tkd_barang = new javax.swing.JTextField();
        cbkategori = new javax.swing.JComboBox<>();
        tid_kategori = new javax.swing.JTextField();
        tnm_barang = new javax.swing.JTextField();
        tcari = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_brg = new javax.swing.JTable();
        bt_tambah = new javax.swing.JButton();
        bt_simpan = new javax.swing.JButton();
        bt_edit = new javax.swing.JButton();
        bt_update = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_batal = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tstok = new javax.swing.JTextField();
        tmin_stok = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 204, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tkd_barang.setText("Kode Barang");
        tkd_barang.addActionListener(this::tkd_barangActionPerformed);
        getContentPane().add(tkd_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 100, -1));

        cbkategori.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Barang 1", "Barang 2", "Barang 3", "Barang 4" }));
        cbkategori.addActionListener(this::cbkategoriActionPerformed);
        getContentPane().add(cbkategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 100, -1));

        tid_kategori.addActionListener(this::tid_kategoriActionPerformed);
        getContentPane().add(tid_kategori, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 110, -1));

        tnm_barang.addActionListener(this::tnm_barangActionPerformed);
        getContentPane().add(tnm_barang, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 210, -1));

        tcari.setText("Ketik Nama Produk");
        tcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tcariKeyPressed(evt);
            }
        });
        getContentPane().add(tcari, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 174, 210, -1));

        tbl_brg.setBackground(new java.awt.Color(153, 153, 255));
        tbl_brg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No.", "Kode Barang", "Nama Barang", "Stok"
            }
        ));
        tbl_brg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_brgMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_brg);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 214, 463, 118));

        bt_tambah.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(this::bt_tambahActionPerformed);
        getContentPane().add(bt_tambah, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 350, -1, -1));

        bt_simpan.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_simpan.setText("Simpan");
        bt_simpan.addActionListener(this::bt_simpanActionPerformed);
        getContentPane().add(bt_simpan, new org.netbeans.lib.awtextra.AbsoluteConstraints(122, 350, -1, -1));

        bt_edit.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_edit.setText("Edit");
        bt_edit.addActionListener(this::bt_editActionPerformed);
        getContentPane().add(bt_edit, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 350, -1, -1));

        bt_update.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_update.setText("Update");
        bt_update.addActionListener(this::bt_updateActionPerformed);
        getContentPane().add(bt_update, new org.netbeans.lib.awtextra.AbsoluteConstraints(278, 350, -1, -1));

        bt_hapus.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(this::bt_hapusActionPerformed);
        getContentPane().add(bt_hapus, new org.netbeans.lib.awtextra.AbsoluteConstraints(356, 350, -1, -1));

        bt_batal.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        bt_batal.setText("Batal");
        bt_batal.addActionListener(this::bt_batalActionPerformed);
        getContentPane().add(bt_batal, new org.netbeans.lib.awtextra.AbsoluteConstraints(434, 350, -1, -1));

        bt_keluar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bt_keluar.setText("Keluar");
        bt_keluar.addActionListener(this::bt_keluarActionPerformed);
        getContentPane().add(bt_keluar, new org.netbeans.lib.awtextra.AbsoluteConstraints(43, 385, 463, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Data Barang");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 120, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel2.setText("Cari Nama Barang");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, -1, -1));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel3.setText("Nama Barang");

        jLabel4.setText("Stok");

        tmin_stok.addActionListener(this::tmin_stokActionPerformed);

        jLabel5.setText("Minimal Stok");

        jLabel6.setText("Kategori Barang");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tmin_stok)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tstok))
                .addGap(144, 144, 144))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addGap(15, 15, 15)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tstok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tmin_stok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(280, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 530, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tkd_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkd_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tkd_barangActionPerformed

    private void cbkategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkategoriActionPerformed
       // Cegah NullPointerException saat item dihapus/di-reset
    if (cbkategori.getSelectedItem() == null || cbkategori.getSelectedItem().equals("=PILIH=")) {
        tid_kategori.setText("");
        return;
    }

    try {
        kon.setkoneksi();
        String sql = "SELECT id_kategori FROM tb_kategori WHERE nm_kategori='" + cbkategori.getSelectedItem() + "'";
        kon.rs = kon.st.executeQuery(sql);
        if (kon.rs.next()) {
            tid_kategori.setText(kon.rs.getString("id_kategori"));
        }
    } catch (SQLException e) {
        System.out.println("Error ambil ID Kategori: " + e.getMessage());
    }

    }//GEN-LAST:event_cbkategoriActionPerformed

    private void tid_kategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tid_kategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tid_kategoriActionPerformed

    private void tnm_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnm_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnm_barangActionPerformed

    private void tmin_stokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tmin_stokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tmin_stokActionPerformed

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahActionPerformed
        NoBarang();
        aktif();
        bt_batal.setEnabled(true);
        bt_tambah.setEnabled(false);
        bt_simpan.setEnabled(true);
        cbkategori.setEnabled(true);

    }//GEN-LAST:event_bt_tambahActionPerformed

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
        if (JOptionPane.showConfirmDialog(this, "yakin mau dihapus?", "konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            HapusData();
            bt_tambah.setEnabled(true);
            nonaktif();
            BersihField();
            cbkategori.setSelectedItem("=PILIH=");
          } else {
            JOptionPane.showMessageDialog(this, "Data Batal Dihapus", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE);
            bt_tambah.setEnabled(true);
            nonaktif();
            BersihField();
            cbkategori.setSelectedItem("=PILIH=");
            return;
          }
          formWindowActivated(null);

    }//GEN-LAST:event_bt_hapusActionPerformed

    private void bt_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_simpanActionPerformed
      if (tkd_barang.getText().isEmpty() || tnm_barang.getText().isEmpty() || tstok.getText().isEmpty() || tmin_stok.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi Semua Data Terlebih Dahulu!", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            SimpanData();
            bt_tambah.setEnabled(true);
            bt_keluar.setEnabled(true);
        }

    }//GEN-LAST:event_bt_simpanActionPerformed

    private void bt_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_editActionPerformed
         aktif();
            tkd_barang.setEnabled(false);
            bt_edit.setEnabled(false);
            bt_update.setEnabled(true);
            bt_batal.setEnabled(true);
            bt_hapus.setEnabled(true);
            bt_tambah.setEnabled(false);

    }//GEN-LAST:event_bt_editActionPerformed

    private void bt_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_batalActionPerformed
        nonaktif();
        BersihField();
        bt_tambah.setEnabled(true);
        cbkategori.setEnabled(true);
        cbkategori.setSelectedItem("=PILIH=");
        try {
          kon.st.getConnection();
        } catch (SQLException ex) {
          Logger.getLogger(Form_Master_Barang.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_bt_batalActionPerformed

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_keluarActionPerformed
        // MenuUtama menu = new MenuUtama();
        //  menu.setLocationRelativeTo(null);
        // menu.setVisible(true);
        dispose(); 

    }//GEN-LAST:event_bt_keluarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void tcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tcariKeyPressed
          kon.setkoneksi();
          BacaTabelBarang2(); 
    }//GEN-LAST:event_tcariKeyPressed

    private void tbl_brgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_brgMouseClicked
        setTable();
        isiNamaKategori2();
        bt_hapus.setEnabled(false);
        bt_edit.setEnabled(true);
        bt_tambah.setEnabled(false);

    }//GEN-LAST:event_tbl_brgMouseClicked

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_updateActionPerformed
        aktif();
            bt_update.setEnabled(false);
            bt_tambah.setEnabled(true);
            EditData();
            cbkategori.setSelectedItem("=PILIH=");

    }//GEN-LAST:event_bt_updateActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Form_Master_Barang().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_batal;
    private javax.swing.JButton bt_edit;
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_simpan;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JButton bt_update;
    private javax.swing.JComboBox<String> cbkategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_brg;
    private javax.swing.JTextField tcari;
    private javax.swing.JTextField tid_kategori;
    private javax.swing.JTextField tkd_barang;
    private javax.swing.JTextField tmin_stok;
    private javax.swing.JTextField tnm_barang;
    private javax.swing.JTextField tstok;
    // End of variables declaration//GEN-END:variables
}
