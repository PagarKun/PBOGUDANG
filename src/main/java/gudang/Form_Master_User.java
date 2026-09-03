/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gudang;

import com.mycompany.gudang.koneksi;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author PagarKun
 */
public class Form_Master_User extends javax.swing.JFrame { 
    
    koneksi kon = new koneksi(); 
    Connection connec; 
    PreparedStatement psmnt = null; 
    ImageIcon format; 
    File pics; 
    FileInputStream fistream; 
    ResultSet rs; 
    private String idUserPilihan;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Form_Master_User.class.getName());

    /**
     * Creates new form Form_Master_User
     */
    public Form_Master_User() {
    initComponents();
    kon.setkoneksi();
    
    // Inisialisasi Pilihan Level
    cblevel.removeAllItems();
    cblevel.addItem("=PILIH=");
    cblevel.addItem("1"); // Admin
    cblevel.addItem("2"); // User
    
    nonaktif();
    tlevel.setVisible(false);

    try {
        Class.forName("com.mysql.cj.jdbc.Driver"); 
        connec = DriverManager.getConnection("jdbc:mysql://localhost/inventory_db", "root", "");
    } catch (Exception e) {
        logger.severe("Gagal terhubung ke database: " + e.getMessage());
    }
}
    
    private void NoUser() {
        String sql_no = "SELECT id_user FROM tb_user WHERE id_user LIKE 'ID/%' ORDER BY id_user DESC LIMIT 1";
    
    try {
        kon.setkoneksi();
        ResultSet resultSet = kon.st.executeQuery(sql_no);

        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MM.yy");
        String tanggal = dateformat1.format(date);

        if (resultSet.next()) {
            String lastId = resultSet.getString("id_user");
            // Mengambil angka di paling belakang setelah tanda '/' terakhir
            String stringAngka = lastId.substring(lastId.lastIndexOf("/") + 1);
            int urutan = Integer.parseInt(stringAngka) + 1;
            
            tkd_user.setText("ID/AHI/" + tanggal + "/" + String.format("%04d", urutan));
        } else {
            // Jika belum ada data yang pake format ID/AHI/
            tkd_user.setText("ID/AHI/" + tanggal + "/0001");
        }

        resultSet.close();
    } catch (Exception e) {
        // Safe Fallback jika terjadi kesalahan parse
        java.util.Date date = new java.util.Date();
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MM.yy");
        String tanggal = dateformat1.format(date);
        tkd_user.setText("ID/AHI/" + tanggal + "/0001");
    }
    }
    
    private void nonaktif() {
        tkd_user.setEnabled(false);
        tnm_user.setEnabled(false);
        tpassword.setEnabled(false);
        cblevel.setEnabled(false);
        bt_browser.setEnabled(false);
    }

    private void aktif() {
        tkd_user.setEnabled(true);
        tnm_user.setEnabled(true);
        tpassword.setEnabled(true);
        cblevel.setEnabled(true);
        bt_browser.setEnabled(true);
    }

    private void BersihField() {
        tkd_user.setText("");
        tnm_user.setText("");
        tpassword.setText("");
        cblevel.setSelectedIndex(0);
        tlevel.setText("");
        photo.setIcon(null);
    }

    private void OpenPicture() {
        JFileChooser picChooser = new JFileChooser();
        int returnVal = picChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = picChooser.getSelectedFile();
                String filename = file.getAbsolutePath();
                pics = new File(filename);
                
                // Pastikan stream ditutup jika sebelumnya digunakan
                if (fistream != null) {
                    fistream.close();
                }
                fistream = new FileInputStream(pics);
                
                ImageIcon icon = new ImageIcon(filename);
                photo.setIcon(icon);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal memuat gambar: " + e.getMessage());
            }
        }
    }

    // 1. Method Simpan Data User (Termasuk Foto)
    private void SimpanData() {
        try {
        String sql = "INSERT INTO tb_user (id_user, nm_user, password, level, foto) VALUES (?, ?, ?, ?, ?)";
        psmnt = connec.prepareStatement(sql);
        
        psmnt.setString(1, tkd_user.getText());
        psmnt.setString(2, tnm_user.getText());
        psmnt.setString(3, String.valueOf(tpassword.getPassword()));
        
        // Ambil nilai level secara pasti (1 atau 2)
        String levelVal = cblevel.getSelectedItem().toString();
        psmnt.setString(4, levelVal);
        
        if (pics != null && fistream != null) {
            psmnt.setBinaryStream(5, fistream, (int) pics.length());
        } else {
            psmnt.setNull(5, java.sql.Types.BLOB);
        }

        psmnt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data User Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        nonaktif();
        BersihField();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal Simpan Data User: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    // 2. Method Edit/Update Data User
    private void EditData() {
        try {
        String sql;
        String levelVal = cblevel.getSelectedItem().toString();

        if (pics != null && fistream != null) {
            sql = "UPDATE tb_user SET nm_user=?, password=?, level=?, foto=? WHERE id_user=?";
            psmnt = connec.prepareStatement(sql);
            psmnt.setString(1, tnm_user.getText());
            psmnt.setString(2, String.valueOf(tpassword.getPassword()));
            psmnt.setString(3, levelVal);
            psmnt.setBinaryStream(4, fistream, (int) pics.length());
            psmnt.setString(5, tkd_user.getText());
        } else {
            sql = "UPDATE tb_user SET nm_user=?, password=?, level=? WHERE id_user=?";
            psmnt = connec.prepareStatement(sql);
            psmnt.setString(1, tnm_user.getText());
            psmnt.setString(2, String.valueOf(tpassword.getPassword()));
            psmnt.setString(3, levelVal);
            psmnt.setString(4, tkd_user.getText());
        }

        psmnt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Data User Berhasil Diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        nonaktif();
        BersihField();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal Update Data User: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    // 3. Method Hapus Data User
    private void HapusData() {
        try {
            String sql = "DELETE FROM tb_user WHERE id_user=?";
            psmnt = connec.prepareStatement(sql);
            psmnt.setString(1, tkd_user.getText());

            psmnt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data User Berhasil Dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

            nonaktif();
            BersihField();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal Hapus Data User: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        bt_tambah = new javax.swing.JButton();
        bt_simpan = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tkd_user = new javax.swing.JTextField();
        tnm_user = new javax.swing.JTextField();
        tpassword = new javax.swing.JPasswordField();
        cblevel = new javax.swing.JComboBox<>();
        tlevel = new javax.swing.JTextField();
        photo = new javax.swing.JLabel();
        bt_browser = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tcari = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(this::bt_tambahActionPerformed);

        bt_simpan.setText("Simpan");
        bt_simpan.addActionListener(this::bt_simpanActionPerformed);

        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(this::bt_hapusActionPerformed);

        bt_keluar.setText("Keluar");
        bt_keluar.addActionListener(this::bt_keluarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 21, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(6, 6, 6)
                            .addComponent(bt_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(9, 9, 9)
                            .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(bt_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 21, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(0, 18, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(bt_tambah)
                        .addComponent(bt_simpan)
                        .addComponent(bt_hapus))
                    .addGap(18, 18, 18)
                    .addComponent(bt_keluar)
                    .addGap(0, 18, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 530, -1));

        jPanel2.setBackground(new java.awt.Color(0, 255, 255));
        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tkd_user.setText("Kode User");
        tkd_user.addActionListener(this::tkd_userActionPerformed);

        tnm_user.setText("Nama User");
        tnm_user.addActionListener(this::tnm_userActionPerformed);

        tpassword.setText("jPasswordField1");
        tpassword.addActionListener(this::tpasswordActionPerformed);

        cblevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cblevel.addActionListener(this::cblevelActionPerformed);

        tlevel.setText("jTextField3");

        photo.setText("jLabel1");

        bt_browser.setText("Browser");
        bt_browser.addActionListener(this::bt_browserActionPerformed);

        jLabel1.setText("Kode User");

        jLabel2.setText("Nama User");

        jLabel3.setText("Password");

        jLabel4.setText("Level");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(52, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tkd_user, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tnm_user, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(58, 58, 58)
                        .addComponent(photo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tpassword, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(bt_browser, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cblevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(tlevel, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(81, 81, 81))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(photo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(tkd_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tnm_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(bt_browser)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cblevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(16, 16, 16)
                .addComponent(tlevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 500, 300));

        jPanel3.setBackground(new java.awt.Color(0, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tcari.setText("Cari");
        tcari.addActionListener(this::tcariActionPerformed);
        tcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tcariKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tcariKeyReleased(evt);
            }
        });

        jLabel5.setText("Cari User");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tcari, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tcari, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 500, 50));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tpasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tpasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tpasswordActionPerformed

    private void cblevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cblevelActionPerformed
       // Tambahkan pengecekan null terlebih dahulu
    if (cblevel.getSelectedItem() != null) {
        if (cblevel.getSelectedItem().equals("1")) {
            tlevel.setText("1");
        } else if (cblevel.getSelectedItem().equals("2")) {
            tlevel.setText("2");
        }
    }
    }//GEN-LAST:event_cblevelActionPerformed

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
       if (JOptionPane.showConfirmDialog(this, "yakin mau dihapus?", "konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { 
    HapusData(); 
    bt_tambah.setEnabled(true); 
    nonaktif(); 
    BersihField(); 
    cblevel.setSelectedItem("=PILIH="); 
} else { 
    JOptionPane.showMessageDialog(this, "Data Batal Dihapus", "Konfirmasi", JOptionPane.INFORMATION_MESSAGE); 
    bt_tambah.setEnabled(true); 
    nonaktif(); 
    BersihField(); 
    cblevel.setSelectedItem("=PILIH="); 
}
    }//GEN-LAST:event_bt_hapusActionPerformed

    private void tnm_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnm_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnm_userActionPerformed

    private void tcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tcariKeyPressed
       
    }//GEN-LAST:event_tcariKeyPressed

    private void tkd_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkd_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tkd_userActionPerformed

    private void bt_browserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_browserActionPerformed
        OpenPicture();
    }//GEN-LAST:event_bt_browserActionPerformed

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahActionPerformed
        if (bt_tambah.getText().equalsIgnoreCase("TAMBAH")) { 
        bt_simpan.setText("SIMPAN"); 
        bt_keluar.setText("BATAL"); 
        BersihField(); 
        NoUser(); 
        aktif(); // Membuka kuncian TextField
        
        bt_hapus.setEnabled(false); 
        tkd_user.setEnabled(false); 
        bt_tambah.setEnabled(false); 
    } else if (bt_tambah.getText().equalsIgnoreCase("EDIT")) { 
        tnm_user.setEnabled(true); 
        tpassword.setEnabled(true); 
        cblevel.setEnabled(true);
        bt_browser.setEnabled(true);
        bt_simpan.setText("UPDATE"); 
        bt_keluar.setText("BATAL"); 
        bt_tambah.setEnabled(false); 
    }
    }//GEN-LAST:event_bt_tambahActionPerformed

    private void bt_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_simpanActionPerformed
        if (bt_simpan.getText().equalsIgnoreCase("SIMPAN")) { 
        if (tnm_user.getText().isEmpty() || String.valueOf(tpassword.getPassword()).isEmpty() || tlevel.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lengkapi semua data user terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        SimpanData(); 
        bt_tambah.setText("Tambah"); 
        bt_keluar.setText("Keluar"); 
        bt_tambah.setEnabled(true);
        BersihField(); 
    } else if (bt_simpan.getText().equalsIgnoreCase("UPDATE")) { 
        EditData(); 
        bt_tambah.setText("Tambah"); 
        bt_keluar.setText("Keluar"); 
        bt_tambah.setEnabled(true);
        BersihField();
    }
    }//GEN-LAST:event_bt_simpanActionPerformed

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_keluarActionPerformed
        if (bt_keluar.getText().equalsIgnoreCase("KELUAR")) { 
        dispose(); 
    } else if (bt_keluar.getText().equalsIgnoreCase("BATAL")) { 
        BersihField(); 
        nonaktif();
        bt_tambah.setEnabled(true); 
        bt_keluar.setText("Keluar"); 
        bt_tambah.setText("Tambah"); 
    }
    }//GEN-LAST:event_bt_keluarActionPerformed

    private void tcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tcariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tcariActionPerformed

    private void tcariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tcariKeyReleased
       // Abaikan jika pencarian kosong
    if (tcari.getText().trim().isEmpty()) {
        BersihField();
        nonaktif();
        return;
    }

    try { 
        String query = "SELECT * FROM tb_user WHERE nm_user LIKE '%" + tcari.getText().trim() + "%' OR id_user LIKE '%" + tcari.getText().trim() + "%'"; 
        psmnt = connec.prepareStatement(query); 
        rs = psmnt.executeQuery(); 
        
        if (rs.next()) { 
            tkd_user.setText(rs.getString("id_user")); 
            tnm_user.setText(rs.getString("nm_user")); 
            tpassword.setText(rs.getString("password")); 
            
            String lvl = rs.getString("level");
            cblevel.setSelectedItem(lvl); 
            tlevel.setText(lvl);
            
            // Tampilkan foto jika ada di Blob database
            byte[] imagedata = rs.getBytes("foto"); 
            if (imagedata != null && imagedata.length > 0) {
                ImageIcon format = new ImageIcon(imagedata); 
                photo.setIcon(format); 
            } else {
                photo.setIcon(null);
            }
            
            // Ubah mode tombol ke Edit
            bt_tambah.setText("EDIT"); 
            bt_tambah.setEnabled(true);
            bt_hapus.setEnabled(true);
            bt_browser.setEnabled(false); 
            cblevel.setEnabled(false); 
        } else { 
            // Jangan tampilkan dialog popup setiap ketik agar tidak mengganggu pencarian
            BersihField();
        }        
    } catch (Exception e) { 
        System.out.println("Error Cari User: " + e.getMessage()); 
    }
    }//GEN-LAST:event_tcariKeyReleased

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
        java.awt.EventQueue.invokeLater(() -> new Form_Master_User().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_browser;
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_simpan;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JComboBox<String> cblevel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel photo;
    private javax.swing.JTextField tcari;
    private javax.swing.JTextField tkd_user;
    private javax.swing.JTextField tlevel;
    private javax.swing.JTextField tnm_user;
    private javax.swing.JPasswordField tpassword;
    // End of variables declaration//GEN-END:variables
}
