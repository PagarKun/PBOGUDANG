package gudang;

import com.mycompany.gudang.koneksi;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Form Pengeluaran Barang
 * @author Hype / Pannugroho
 */
public class Form_Pengeluaran_Barang extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(Form_Pengeluaran_Barang.class.getName());

    koneksi kon = new koneksi();
    private Object[][] datasementara = null;
    private String[] label = {"KODE BARANG", "NAMA KATEGORI", "NAMA BARANG", "JUMLAH"};
    private String ubah;

    public String kode;
    public String KdBrg;
    public String NmKategori;
    public String NmBrg;

    public Form_Pengeluaran_Barang() {
        initComponents();
        kon.setkoneksi();
        nonaktif();
    }

    public void setIDUser(String idUser) {
        tid_user.setText(idUser);
    }

    private void Bersih() {
        tid_keluar.setText("");
        ttgl_keluar.setText("");
        tid_user.setText("");
        tkd_brg.setText("");
        tnm_kategori.setText("");
        tnm_brg.setText("");
        tjml.setText("");
        tjml_awal.setText("");
    }

    private void aktif() {
        ttgl_keluar.setEnabled(true);
        tid_keluar.setEnabled(true);
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
        ttgl_keluar.setEnabled(false);
        tid_keluar.setEnabled(false);
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
        if (row != -1) {
            tkd_brg.setText((String) tbl_penerimaan.getValueAt(row, 0));
            tnm_kategori.setText((String) tbl_penerimaan.getValueAt(row, 1));
            tnm_brg.setText((String) tbl_penerimaan.getValueAt(row, 2));
            tjml_awal.setText((String) tbl_penerimaan.getValueAt(row, 3));
        }
    }

    private void NoKeluar() {
        try {
            kon.setkoneksi();
            String sql_no = "SELECT id_keluar FROM tb_pengeluaran_barang WHERE id_keluar LIKE 'ID/KB-%' ORDER BY id_keluar DESC LIMIT 1";
            ResultSet rs = kon.st.executeQuery(sql_no);

            java.util.Date date = new java.util.Date();
            SimpleDateFormat dateformat1 = new SimpleDateFormat("MM.yy");
            String tanggal = dateformat1.format(date);

            if (rs.next()) {
                String lastId = rs.getString("id_keluar");
                String stringAngka = lastId.substring(lastId.lastIndexOf("/") + 1);
                int no = Integer.parseInt(stringAngka) + 1;
                tid_keluar.setText("ID/KB-" + tanggal + "/" + String.format("%04d", no));
            } else {
                tid_keluar.setText("ID/KB-" + tanggal + "/0001");
            }
            rs.close();
        } catch (Exception e) {
            java.util.Date date = new java.util.Date();
            SimpleDateFormat dateformat1 = new SimpleDateFormat("MM.yy");
            String tanggal = dateformat1.format(date);
            tid_keluar.setText("ID/KB-" + tanggal + "/0001");
        }
    }

    private void TampilanTabelSementara() {
        try {
            String sql = "SELECT * FROM tb_sementara_pengeluaran ORDER BY kd_brg";
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

    private void SimpanSementara() {
        try {
            String sql = "INSERT INTO tb_sementara_pengeluaran VALUES('" 
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
            String sql = "UPDATE tb_sementara_pengeluaran SET "
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
        if (row != -1) {
            try {
                String sql = "DELETE FROM tb_sementara_pengeluaran WHERE kd_brg='" + (String) tbl_penerimaan.getValueAt(row, 0) + "'";
                kon.st.executeUpdate(sql);
                TampilanTabelSementara();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void setTanggalskr() {
        java.util.Date skrg = new java.util.Date();
        SimpleDateFormat kal = new SimpleDateFormat("yyyy-MM-dd");
        ttgl_keluar.setText(kal.format(skrg));
    }

    private void SimpanPenerimaan() {
        try {
            String sql = "INSERT INTO tb_pengeluaran_barang VALUES('" 
                    + tid_keluar.getText() + "', '" 
                    + ttgl_keluar.getText() + "', '" 
                    + tid_user.getText() + "')";
            kon.st.executeUpdate(sql);

            String detail = "INSERT INTO tb_detail_pengeluaran SELECT '" 
                    + tid_keluar.getText() + "', kd_brg, jml FROM tb_sementara_pengeluaran";
            kon.st.executeUpdate(detail);
            Bersih();
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.toString());
        }
    }

    private void HapusTabelSementara() {
        try {
            String sql = "DELETE FROM tb_sementara_pengeluaran";
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
            if (kon.rs.next()) {
                jml = kon.rs.getInt("stok");
            }

            int inputJml = tjml.getText().trim().isEmpty() ? 0 : Integer.parseInt(tjml.getText().trim());

            if (ubah.equals("delete")) {
                int jmlAwal = tjml_awal.getText().isEmpty() ? 0 : Integer.parseInt(tjml_awal.getText());
                stok = jml + jmlAwal;
            } else if (ubah.equals("edit")) {
                int jmlAwal = tjml_awal.getText().isEmpty() ? 0 : Integer.parseInt(tjml_awal.getText());
                stok = (jml + jmlAwal) - inputJml;
            } else if (ubah.equals("insert")) {
                if (inputJml > jml) {
                    JOptionPane.showMessageDialog(this, "Stok barang tidak mencukupi! Stok tersedia: " + jml, "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                stok = jml - inputJml;
            }

            String sql_update = "UPDATE tb_barang SET stok='" + stok + "' WHERE kd_brg='" + tkd_brg.getText() + "'";
            kon.st.executeUpdate(sql_update);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Update Stok: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        tkd_brg = new javax.swing.JTextField();
        tnm_kategori = new javax.swing.JTextField();
        tnm_brg = new javax.swing.JTextField();
        tjml = new javax.swing.JTextField();
        tb_browse = new javax.swing.JButton();
        tjml_awal = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        bt_tambah = new javax.swing.JButton();
        bt_insert = new javax.swing.JButton();
        bt_update = new javax.swing.JButton();
        bt_simpan = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        tid_keluar = new javax.swing.JTextField();
        ttgl_keluar = new javax.swing.JTextField();
        tid_user = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_penerimaan = new javax.swing.JTable();

        jLabel9.setText("jLabel9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); 
        jLabel1.setForeground(new java.awt.Color(255, 51, 255));
        jLabel1.setText("======= FORM PENGELUARAN BARANG =======");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, 20));

        jPanel5.setBackground(new java.awt.Color(0, 51, 255));

        tb_browse.setText("Cari");
        tb_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tb_browseActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Kode Barang");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Kategori");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nama Barang");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Jumlah Barang");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(tkd_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tb_browse))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(tjml, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tjml_awal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tkd_brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tb_browse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tjml, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tjml_awal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 30, 290, 140));

        jPanel2.setBackground(new java.awt.Color(0, 102, 255));

        bt_tambah.setBackground(new java.awt.Color(255, 204, 102));
        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tambahActionPerformed(evt);
            }
        });

        bt_insert.setBackground(new java.awt.Color(51, 204, 0));
        bt_insert.setText("Insert");
        bt_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_insertActionPerformed(evt);
            }
        });

        bt_update.setBackground(new java.awt.Color(255, 51, 51));
        bt_update.setText("Update");
        bt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_updateActionPerformed(evt);
            }
        });

        bt_simpan.setBackground(new java.awt.Color(204, 0, 204));
        bt_simpan.setText("Simpan");
        bt_simpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_simpanActionPerformed(evt);
            }
        });

        bt_hapus.setBackground(new java.awt.Color(0, 255, 51));
        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_hapusActionPerformed(evt);
            }
        });

        bt_keluar.setBackground(new java.awt.Color(255, 0, 0));
        bt_keluar.setText("Tutup");
        bt_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_keluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_simpan, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 20, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bt_tambah)
                    .addComponent(bt_insert)
                    .addComponent(bt_update)
                    .addComponent(bt_simpan)
                    .addComponent(bt_hapus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bt_keluar)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 600, 70));

        jPanel1.setBackground(new java.awt.Color(0, 51, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("ID Keluar Barang");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tgl. Keluar");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("ID. User");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tid_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ttgl_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(tid_user, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tid_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ttgl_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tid_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 280, 140));

        tbl_penerimaan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "KODE BARANG", "NAMA KATEGORI", "NAMA BARANG", "JUMLAH"
            }
        ));
        tbl_penerimaan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_penerimaanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_penerimaan);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 600, 220));

        pack();
    }// </editor-fold>                          

    private void tb_browseActionPerformed(java.awt.event.ActionEvent evt) {                                          
        boolean closable = true;
        Form_Stok_Barang_MinimalPM databarang = new Form_Stok_Barang_MinimalPM(this, closable);
        databarang.barang1 = this;
        databarang.setVisible(true);

        if (KdBrg != null && !KdBrg.isEmpty()) {
            tkd_brg.setText(KdBrg);
            tnm_kategori.setText(NmKategori);
            tnm_brg.setText(NmBrg);
            tjml.requestFocus();
        }
    }                                         

    private void tbl_penerimaanMouseClicked(java.awt.event.MouseEvent evt) {                                             
        aktif();
        tkd_brg.setEnabled(true);
        bt_update.setEnabled(true);
        bt_hapus.setEnabled(true);
        bt_tambah.setEnabled(false);
        bt_insert.setEnabled(false);
        setTable();
    }                                            

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {                                          
        aktif();
        setTanggalskr();
        NoKeluar();
        bt_simpan.setEnabled(false);
    }                                         

    private void bt_insertActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (tkd_brg.getText().isEmpty() || tjml.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih barang dan isi jumlah terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String sql = "SELECT * FROM tb_sementara_pengeluaran WHERE kd_brg='" + tkd_brg.getText() + "'";
            kon.rs = kon.st.executeQuery(sql);

            if (kon.rs.next()) {
                JOptionPane.showMessageDialog(this, "Barang sudah ada di daftar sementara!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                tkd_brg.setText("");
                tnm_kategori.setText("");
                tnm_brg.setText("");
                tjml.setText("");
            } else {
                ubah = "insert";
                updateStokBarang(ubah);
                SimpanSementara();

                tkd_brg.setText("");
                tnm_kategori.setText("");
                tnm_brg.setText("");
                tjml.setText("");

                bt_simpan.setEnabled(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Insert: " + e.getMessage());
        }
    }                                         

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {                                          
        if (tjml.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Isi jumlah barang terlebih dahulu!");
            return;
        }
        if (tjml_awal.getText().trim().isEmpty()) {
            tjml_awal.setText("0");
        }

        ubah = "edit";
        updateStokBarang(ubah);
        EditData();

        tkd_brg.setEnabled(true);
        bt_update.setEnabled(false);
        bt_hapus.setEnabled(false);
        bt_insert.setEnabled(true);
    }                                         

    private void bt_simpanActionPerformed(java.awt.event.ActionEvent evt) {                                          
        SimpanPenerimaan();
        JOptionPane.showMessageDialog(this, "Transaksi Pengeluaran Berhasil Disimpan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        TampilanTabelSementara();
        HapusTabelSementara();
        nonaktif();
        Bersih();
        bt_tambah.setEnabled(true);
    }                                         

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {                                         
        ubah = "delete";
        updateStokBarang(ubah);
        HapusIsiSementara();

        bt_simpan.setEnabled(true);
        bt_update.setEnabled(false);
        bt_hapus.setEnabled(false);
        bt_insert.setEnabled(true);

        tkd_brg.setText("");
        tnm_kategori.setText("");
        tnm_brg.setText("");
        tjml.setText("");
    }                                        

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        HapusTabelSementara();
        dispose();
    }                                         

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Form_Pengeluaran_Barang().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_insert;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_simpan;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JButton bt_update;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton tb_browse;
    private javax.swing.JTable tbl_penerimaan;
    private javax.swing.JTextField tid_keluar;
    private javax.swing.JTextField tid_user;
    private javax.swing.JTextField tjml;
    private javax.swing.JTextField tjml_awal;
    private javax.swing.JTextField tkd_brg;
    private javax.swing.JTextField tnm_brg;
    private javax.swing.JTextField tnm_kategori;
    private javax.swing.JTextField ttgl_keluar;
    // End of variables declaration                     
}