/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gudang; 
import com.mycompany.gudang.koneksi;
import static com.mysql.cj.conf.PropertyKey.logger;
import java.awt.event.KeyEvent; 
import java.sql.*; 
import java.text.SimpleDateFormat; 
import javax.swing.*; 
import javax.swing.table.DefaultTableModel; 

/**
 *
 * @author xDoRk
 */
public class Form_Penerimaan_Barang extends javax.swing.JFrame { 
    koneksi kon=new koneksi(); 
    private Object [][] datasementara=null; 
    private String[]label={"KODE BARANG","NAMA KATEGORI","NAMA BARANG","JUMLAH"}; 
    private String ubah;

    /**
     * Creates new form Form_Penerimaan_Barang
     */
public Form_Penerimaan_Barang() { 
    initComponents(); 
    kon.setkoneksi(); 
    nonaktif(); 
} 

public String kode; 

public String getNik() { 
    return kode; 
} 

public String KdBrg; 
public String NmKategori; 
public String NmBrg; 

private void Bersih() { 
    tinvoice.setText(""); 
    ttgl_penerimaan.setText(""); 
    tid_user.setText(""); 
    tkd_brg.setText(""); 
    tnm_kategori.setText(""); 
    tnm_brg.setText(""); 
    tjml.setText(""); 
} 

private void aktif() { 
    ttgl_penerimaan.setEnabled(true); 
    tinvoice.setEnabled(true); 
    tjml_awal.setVisible(false); 
    tid_user.setEnabled(true); 
    tkd_brg.setEnabled(true); 
    tnm_brg.setEnabled(true); 
    tnm_kategori.setEnabled(true); 
    tjml.setEnabled(true); 
    bt_update.setEnabled(false); 
    bt_hapus.setEnabled(false); 
    bt_simpan.setEnabled(true); 
    tb_browse.setEnabled(true); 
    bt_insert.setEnabled(true); 
    bt_tambah.setEnabled(false); 
} 

private void nonaktif() { 
    ttgl_penerimaan.setEnabled(false); 
    tinvoice.setEnabled(false); 
    tjml_awal.setVisible(false); 
    tid_user.setEnabled(false); 
    tkd_brg.setEnabled(false); 
    tnm_brg.setEnabled(false); 
    tnm_kategori.setEnabled(false); 
    tjml.setEnabled(false); 
    bt_update.setEnabled(false); 
    bt_hapus.setEnabled(false); 
    bt_simpan.setEnabled(false); 
    bt_insert.setEnabled(false); 
    tb_browse.setEnabled(false); 
} 

private void setTable() { 
    int row = tbl_penerimaan.getSelectedRow(); 
    tkd_brg.setText((String) tbl_penerimaan.getValueAt(row, 0)); 
    tnm_kategori.setText((String) tbl_penerimaan.getValueAt(row, 1)); 
    tnm_brg.setText((String) tbl_penerimaan.getValueAt(row, 2)); 
    tjml_awal.setText((String) tbl_penerimaan.getValueAt(row, 3)); 
} 

private void TampilanTabelSementara() { 
    try { 
        String sql = "SELECT * FROM tb_sementara_penerimaan ORDER BY kd_brg"; 
        kon.rs = kon.st.executeQuery(sql); 
        ResultSetMetaData m = kon.rs.getMetaData(); 
        int kolom = m.getColumnCount(); 
        int baris = 0; 
        
        while (kon.rs.next()) { 
            baris = kon.rs.getRow(); 
        } 
         
        datasementara = new Object[baris][kolom]; 
        int x = 0; 
        kon.rs.beforeFirst(); 
        while (kon.rs.next()) { 
            datasementara[x][0] = kon.rs.getString("kd_brg"); 
            datasementara[x][1] = kon.rs.getString("nm_kategori"); 
            datasementara[x][2] = kon.rs.getString("nm_brg"); 
            datasementara[x][3] = kon.rs.getString("jml"); 
            x++; 
        } 
        tbl_penerimaan.setModel(new DefaultTableModel(datasementara, label)); 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    } 
}  

private void TampilDataBarang() { 
    try { 
        String sql = "SELECT * FROM tb_barang WHERE kd_brg='" + tkd_brg.getText() + "'"; 
        kon.rs = kon.st.executeQuery(sql); 
        if (kon.rs.next()) { 
            tnm_kategori.setText(kon.rs.getString("nm_kategori")); 
            tnm_brg.setText(kon.rs.getString("nm_brg")); 
        } else { 
            JOptionPane.showMessageDialog(null, "Kode Barang " + tkd_brg.getText() + " tidak ditemukan"); 
        } 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    } 
} 

private void SimpanSementara() { 
    try { 
        String sql = "INSERT INTO tb_sementara_penerimaan VALUES('" 
                + tkd_brg.getText() + "', '" 
                + tnm_kategori.getText() + "', '" 
                + tnm_brg.getText() + "', '" 
                + tjml.getText() + "')"; 
        kon.st.executeUpdate(sql); 
        TampilanTabelSementara(); 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    } 
} 

private void EditData() { 
    try { 
        String sql = "UPDATE tb_sementara_penerimaan SET " 
                + "kd_brg='" + tkd_brg.getText() + "', " 
                + "nm_kategori='" + tnm_kategori.getText() + "', " 
                + "nm_brg='" + tnm_brg.getText() + "', " 
                + "jml='" + tjml.getText() + "' " 
                + "WHERE kd_brg='" + tkd_brg.getText() + "'"; 
        kon.st.executeUpdate(sql); 
        JOptionPane.showMessageDialog(null, "Data berhasil diupdate"); 
        TampilanTabelSementara(); 
        tkd_brg.setText(""); 
        tnm_kategori.setText(""); 
        tnm_brg.setText(""); 
        tjml.setText(""); 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    } 
} 

private void HapusIsiSementara() { 
    int row = tbl_penerimaan.getSelectedRow(); 
    try { 
        String sql = "DELETE FROM tb_sementara_penerimaan WHERE kd_brg='" 
                + (String) tbl_penerimaan.getValueAt(row, 0) + "'"; 
        kon.st.executeUpdate(sql); 
        TampilanTabelSementara(); 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    }  
} 

private void setTanggalskr() { 
    java.util.Date skrg = new java.util.Date(); 
    java.text.SimpleDateFormat kal = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
    ttgl_penerimaan.setText(kal.format(skrg)); 
}  

private void SimpanPenerimaan() { 
    try { 
        String sql = "INSERT INTO tb_penerimaan_barang VALUES('" 
                + tinvoice.getText() + "', '" 
                + ttgl_penerimaan.getText() + "', '" 
                + tid_user.getText() + "')"; 
        kon.st.executeUpdate(sql); 

        String detail = "INSERT INTO tb_detail_penerimaan SELECT '" 
                + tinvoice.getText() + "', kd_brg, jml FROM tb_sementara_penerimaan"; 
        kon.st.executeUpdate(detail); 
        Bersih(); 
    } catch (SQLException e) { 
        System.out.println("Koneksi gagal: " + e.toString()); 
    } 
} 

private void HapusTabelSementara() { 
    try { 
        String sql = "DELETE FROM tb_sementara_penerimaan"; 
        kon.st.executeUpdate(sql); 
        TampilanTabelSementara(); 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, e); 
    } 
} 

private void updateStokBarang(String ubah) { 
    int jml = 0, stok = 0; 
    String sql = "SELECT stok FROM tb_barang WHERE kd_brg='" + tkd_brg.getText() + "'"; 
    try { 
        kon.rs = kon.st.executeQuery(sql); 
        while (kon.rs.next()) { 
            jml = Integer.parseInt(kon.rs.getString(1)); 
        } 
        
        if (ubah.equals("insert")) { 
            stok = jml + Integer.parseInt(tjml.getText()); 
        } else if (ubah.equals("edit")) { 
            stok = (jml - Integer.parseInt(tjml_awal.getText())) + Integer.parseInt(tjml.getText()); 
        } else if (ubah.equals("delete")) { 
            stok = jml - Integer.parseInt(tjml_awal.getText()); 
        } 

        String sql_update = "UPDATE tb_barang SET stok='" + stok + "' WHERE kd_brg='" + tkd_brg.getText() + "'"; 
        try { 
            kon.st.executeUpdate(sql_update); 
        } catch (Exception e) { 
            JOptionPane.showMessageDialog(null, e.getMessage()); 
        } 
    } catch (Exception e) { 
        JOptionPane.showMessageDialog(null, e.getMessage()); 
    } 
} 

private void NoInvoice() { 
    try { 
        // Mengambil invoice paling akhir secara presisi
        String sql_no = "SELECT RIGHT(invoice, 4) AS no FROM tb_penerimaan_barang ORDER BY invoice DESC LIMIT 1"; 
        kon.setkoneksi(); 
        ResultSet rs = kon.st.executeQuery(sql_no); 
        
        java.util.Date date = new java.util.Date(); 
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MM.yy"); 
        String tanggal = dateformat1.format(date); 

        if (rs.next()) { 
            int no = Integer.parseInt(rs.getString("no")) + 1; 
            tinvoice.setText("AHI/INV-" + tanggal + "/" + String.format("%04d", no));
        } else { 
            tinvoice.setText("AHI/INV-" + tanggal + "/0001"); 
        } 
        rs.close();
    } catch (Exception e) { 
        JOptionPane.showMessageDialog(null, "Error Penomoran Invoice: " + e.getMessage()); 
    } 
}

public void setIDUser(String idUser) {
    tid_user.setText(idUser);
}

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField4 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tinvoice = new javax.swing.JTextField();
        ttgl_penerimaan = new javax.swing.JTextField();
        tid_user = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tnm_kategori = new javax.swing.JTextField();
        tnm_brg = new javax.swing.JTextField();
        tjml = new javax.swing.JTextField();
        tkd_brg = new javax.swing.JTextField();
        tb_browse = new javax.swing.JButton();
        tjml_awal = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_penerimaan = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        bt_update = new javax.swing.JButton();
        bt_insert = new javax.swing.JButton();
        bt_tambah = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_simpan = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        jTextField4.setText("jTextField4");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton2.setText("jButton2");

        jLabel10.setText("jLabel10");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 255));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 0));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Invoice");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tgl. Penerimaan");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ID User");

        tinvoice.setEditable(false);

        ttgl_penerimaan.setEditable(false);
        ttgl_penerimaan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ttgl_penerimaanActionPerformed(evt);
            }
        });

        tid_user.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tid_user, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(ttgl_penerimaan)
                    .addComponent(tinvoice))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tinvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ttgl_penerimaan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tid_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 50, -1, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 0));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Kode Barang");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kategori");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Nama Barang");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Jumlah");

        tnm_kategori.setEditable(false);

        tnm_brg.setEditable(false);

        tkd_brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tkd_brgActionPerformed(evt);
            }
        });

        tb_browse.setText("Cari");
        tb_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_browseActionPerformed(evt);
            }
        });

        tjml_awal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tjml_awalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(tkd_brg))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(tjml, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tjml_awal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tb_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tkd_brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tb_browse))
                .addGap(8, 8, 8)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tjml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tjml_awal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(271, 50, -1, -1));

        tbl_penerimaan.setBackground(new java.awt.Color(255, 255, 102));
        tbl_penerimaan.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_penerimaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_penerimaanMouseClicked(evt);
            }
        });
        tbl_penerimaan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbl_penerimaanKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tbl_penerimaan);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 222, 517, 141));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 255));
        jLabel8.setText("=======       FORM PENERIMAAN BARANG       ======");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, 38));

        jPanel4.setBackground(new java.awt.Color(153, 153, 0));

        bt_update.setBackground(new java.awt.Color(255, 153, 51));
        bt_update.setText("Update");
        bt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_updateActionPerformed(evt);
            }
        });

        bt_insert.setBackground(new java.awt.Color(204, 51, 255));
        bt_insert.setText("Insert");
        bt_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_insertActionPerformed(evt);
            }
        });

        bt_tambah.setBackground(new java.awt.Color(102, 255, 102));
        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tambahActionPerformed(evt);
            }
        });

        bt_hapus.setBackground(new java.awt.Color(255, 51, 51));
        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_hapusActionPerformed(evt);
            }
        });

        bt_simpan.setBackground(new java.awt.Color(51, 153, 255));
        bt_simpan.setText("Simpan");
        bt_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_simpanActionPerformed(evt);
            }
        });

        bt_keluar.setBackground(new java.awt.Color(204, 204, 204));
        bt_keluar.setText("Tutup");
        bt_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_keluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(bt_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                        .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(59, 59, 59)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bt_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(bt_simpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bt_tambah, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(bt_update, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 394, -1, -1));

        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ttgl_penerimaanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ttgl_penerimaanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ttgl_penerimaanActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_updateActionPerformed
        ubah="edit"; 
        updateStokBarang(ubah);
        EditData(); 
        tkd_brg.setEnabled(true); 
        bt_update.setEnabled(false); 
        bt_hapus.setEnabled(false); 
        bt_tambah.setEnabled(false); 
        bt_insert.setEnabled(true); 
        bt_keluar.setEnabled(true);
    }//GEN-LAST:event_bt_updateActionPerformed

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
         ubah="delete"; 
        updateStokBarang(ubah); 
        HapusIsiSementara(); 
        bt_simpan.setEnabled(true); 
        bt_update.setEnabled(false); 
        bt_hapus.setEnabled(false); 
        bt_insert.setEnabled(true); 
        bt_keluar.setEnabled(true); 
        tkd_brg.setText(""); 
        tnm_kategori.setText(""); 
        tnm_brg.setText(""); 
        tjml.setText("");
    }//GEN-LAST:event_bt_hapusActionPerformed

    private void tjml_awalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tjml_awalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tjml_awalActionPerformed

    private void tkd_brgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkd_brgActionPerformed
        if (!tkd_brg.getText().trim().isEmpty()) {
            TampilDataBarang();
            tjml.requestFocus();
        }
    }//GEN-LAST:event_tkd_brgActionPerformed

    private void tb_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tb_browseActionPerformed
        boolean closable = true; 
        Form_Stok_Barang_MinimalPM databarang = new Form_Stok_Barang_MinimalPM(this, closable); 
        databarang.barang1 = this; 
        
        // Menampilkan dialog pencarian barang
        databarang.setVisible(true); 
        
        // Baris ini dieksekusi SETELAH dialog ditutup (setelah kamu memilih barang di tabel dialog)
        if (KdBrg != null && !KdBrg.isEmpty()) {
            tkd_brg.setText(KdBrg); 
            tnm_kategori.setText(NmKategori); 
            tnm_brg.setText(NmBrg);
            tjml.requestFocus(); // Kursor langsung berpindah ke input Jumlah
        }
    }//GEN-LAST:event_tb_browseActionPerformed

    private void tbl_penerimaanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_penerimaanMouseClicked
        aktif(); 
        tkd_brg.setEnabled(true); 
        bt_update.setEnabled(true); 
        bt_hapus.setEnabled(true); 
        bt_tambah.setEnabled(false); 
        bt_insert.setEnabled(false); 
        setTable();
    }//GEN-LAST:event_tbl_penerimaanMouseClicked

    private void tbl_penerimaanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbl_penerimaanKeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_BACK_SPACE) 
        { 
            HapusIsiSementara(); 
        } 
    }//GEN-LAST:event_tbl_penerimaanKeyPressed

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahActionPerformed
        aktif(); 
        setTanggalskr(); 
        NoInvoice(); 
        bt_simpan.setEnabled(false);
    }//GEN-LAST:event_bt_tambahActionPerformed

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_keluarActionPerformed
        HapusTabelSementara(); 
        dispose();
    }//GEN-LAST:event_bt_keluarActionPerformed

    private void bt_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_insertActionPerformed
        if (tkd_brg.getText().isEmpty() || tjml.getText().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih barang dan isi jumlah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try { 
        String sql = "SELECT * FROM tb_sementara_penerimaan WHERE kd_brg='" + tkd_brg.getText() + "'"; 
        kon.rs = kon.st.executeQuery(sql); 
        
        if (kon.rs.next()) { 
            JOptionPane.showMessageDialog(this, "Barang sudah ada di daftar sementara. Gunakan tombol Edit/Update!", "Informasi", JOptionPane.INFORMATION_MESSAGE); 
            tkd_brg.setText(""); 
            tnm_kategori.setText(""); 
            tnm_brg.setText(""); 
            tjml.setText(""); 
            tb_browse.requestFocus(); 
        } else { 
            // Update stok barang ke database & simpan ke tabel sementara
            ubah = "insert"; 
            updateStokBarang(ubah); 
            SimpanSementara(); 

            // Bersihkan input item untuk barang berikutnya
            tkd_brg.setText(""); 
            tnm_kategori.setText(""); 
            tnm_brg.setText(""); 
            tjml.setText(""); 

            bt_keluar.setEnabled(true); 
            bt_simpan.setEnabled(true); 

            // Konfirmasi tambah barang lagi
            int jawab = JOptionPane.showConfirmDialog(this, "Barang berhasil ditambahkan! Mau tambah barang lain?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (jawab == JOptionPane.YES_OPTION) { 
                tb_browse.requestFocus(); 
            } else { 
                bt_simpan.requestFocus(); 
            } 
        } 
    } catch (SQLException e) { 
        JOptionPane.showMessageDialog(null, "Error Insert: " + e.getMessage()); 
    }
    
    }//GEN-LAST:event_bt_insertActionPerformed

    
    private void bt_simpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_simpanActionPerformed
        SimpanPenerimaan(); 
        JOptionPane.showMessageDialog(this,"Berhasil disimpan", 
            "Informasi",JOptionPane.INFORMATION_MESSAGE); 
        TampilanTabelSementara(); 
        HapusTabelSementara(); 
        nonaktif(); 
        Bersih(); 
        bt_tambah.setEnabled(true); 
        bt_keluar.setEnabled(true);
    }//GEN-LAST:event_bt_simpanActionPerformed

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
            java.util.logging.Logger.getLogger(Form_Penerimaan_Barang.class.getName())
            .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Form_Penerimaan_Barang().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_insert;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_simpan;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JButton bt_update;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JButton tb_browse;
    private javax.swing.JTable tbl_penerimaan;
    private javax.swing.JTextField tid_user;
    private javax.swing.JTextField tinvoice;
    private javax.swing.JTextField tjml;
    private javax.swing.JTextField tjml_awal;
    private javax.swing.JTextField tkd_brg;
    private javax.swing.JTextField tnm_brg;
    private javax.swing.JTextField tnm_kategori;
    private javax.swing.JTextField ttgl_penerimaan;
    // End of variables declaration//GEN-END:variables
}
