/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gudang;
import com.mycompany.gudang.koneksi;
import java.sql.*; 
import java.text.SimpleDateFormat; 
import java.util.HashMap; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel; 
import net.sf.jasperreports.engine.JasperFillManager; 
import net.sf.jasperreports.engine.JasperPrint; 
import net.sf.jasperreports.view.JasperViewer; 
import gudang.Form_Stok_Barang_Minimal; 
/**
 *
 * @author alban
 */
public class Form_Transaksi_Inventory extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Form_Transaksi_Inventory.class.getName());
    koneksi kon=new koneksi(); 
    private Object [][] datasementara=null; 
    private String[]label={"KODE BARANG","NAMA KATEGORI","NAMA BARANG","JML ORDER"};
    /**
     * Creates new form Form_Transaksi_Inventory
     */
    public Form_Transaksi_Inventory() {
        initComponents();
        kon.setkoneksi(); 
        nonaktif();
    }
    public void setIDUser(String idUser) {
        tid_user.setText(idUser);
    }
    
    public String kode; 
    public String getNik() 
           { 
                return kode; 
           } 
     
    public String KdBrg; 
    public String NmKategori;
    public String NmBrg; 
     
    private void Bersih(){ 
        ttgl_order.setText(""); 
        tid_user.setText(""); 
        tkd_brg.setText(""); 
        tnm_kategori.setText(""); 
        tnm_brg.setText(""); 
        tjml_order.setText(""); 
    } 
     
    private void aktif(){ 
        tid_order.setEnabled(true); 
        ttgl_order.setEnabled(true); 
        tid_user.setEnabled(true); 
        tkd_brg.setEnabled(true); 
        tnm_kategori.setEnabled(true); 
        tnm_brg.setEnabled(true); 
        tjml_order.setEnabled(true); 
        bt_insert.setEnabled(true); 
        bt_update.setEnabled(true); 
        bt_hapus.setEnabled(true); 
        bt_print.setEnabled(true); 
        bt_batal.setEnabled(true); 
        bt_browse.setEnabled(true); 
    } 
     
    private void nonaktif(){ 
        tid_order.setEnabled(false); 
        ttgl_order.setEnabled(false); 
        tid_user.setEnabled(false); 
        tkd_brg.setEnabled(false); 
        tnm_kategori.setEnabled(false); 
        tnm_brg.setEnabled(false); 
        tjml_order.setEnabled(false); 
        bt_insert.setEnabled(false); 
        bt_update.setEnabled(false); 
        bt_hapus.setEnabled(false); 
        bt_print.setEnabled(false); 
        bt_batal.setEnabled(false); 
        bt_browse.setEnabled(false); 
    }
    
     private void NoOrder() 
    { 
        try  
        { 
            String sql_no = "select *from tb_order_inventory order by id_order desc"; 
            kon.setkoneksi(); 
            ResultSet rs = kon.st.executeQuery(sql_no); 
            if (rs.next()) 
            { 
                String NoPas = rs.getString("id_order").substring(18); 
                String AN = "" + (Integer.parseInt(NoPas)+1); 
                String No1 = ""; 
                 
            java.util.Date date = new java.util.Date(); 
            SimpleDateFormat dateformat2=new SimpleDateFormat ("dd.MM.yyyy");
            
            String tanggal2=dateformat2.format(date); 
                if(AN.length()==1) 
                {No1 = "00";} 
                else if(AN.length()==2) 
                {No1 = "0";} 
                else if(AN.length()==3) 
                {No1 = "";} 
                 
                tid_order.setText("ID/OR/"+tanggal2+"/"+No1 + AN); 
            }  
                else  
            { 
                java.util.Date date = new java.util.Date();         
            SimpleDateFormat dateformat2=new SimpleDateFormat ("dd.MM.yyyy"); 
            String tanggal2=dateformat2.format(date); 
                               tid_order.setText("ID/OR/"+tanggal2+"/"+"001"); 
            } 
        } 
            catch (Exception e) 
        { 
            JOptionPane.showMessageDialog(null, e); 
        } 
    } 
      
    private void setTable(){ 
        int row=tbl_order.getSelectedRow(); 
        tkd_brg.setText((String)tbl_order.getValueAt(row,0)); 
        tnm_kategori.setText((String)tbl_order.getValueAt(row,1)); 
        tnm_brg.setText((String)tbl_order.getValueAt(row,2));
    
       tjml_order.setText((String)tbl_order.getValueAt(row,3)); 
    } 
      
    private void EditData(){ 
        try{ 
            String sql="Update tb_sementara_orderinventory set kd_brg='"+tkd_brg.getText()+"'," 
                    + "nm_kategori='"+tnm_kategori.getText()+"'," 
                    + "nm_brg='"+tnm_brg.getText()+"'," 
                    + "jml_order='"+tjml_order.getText()+"'" 
                    + "where kd_brg='"+tkd_brg.getText()+"'"; 
            kon.st.executeUpdate(sql); 
            JOptionPane.showMessageDialog(null,"Data berhasil diupdate"); 
            TampilanTabelSementara(); 
            tkd_brg.setText(""); 
            tnm_kategori.setText(""); 
            tnm_brg.setText(""); 
            tjml_order.setText(""); 
           } 
            catch(SQLException e){ 
            JOptionPane.showMessageDialog(null,e); 
           } 
    } 
     
private void TampilanTabelSementara() 
    { 
        try 
        {
            String sql="Select *From tb_sementara_orderinventory order by kd_brg"; 
            kon.rs=kon.st.executeQuery(sql); 
            ResultSetMetaData m=kon.rs.getMetaData(); 
            int kolom=m.getColumnCount(); 
            int baris=0; 
            while(kon.rs.next()) 
            { 
                baris=kon.rs.getRow(); 
            } 
             
            datasementara=new Object[baris][kolom]; 
            int x=0; 
            kon.rs.beforeFirst(); 
            while(kon.rs.next()) 
            { 
                datasementara[x][0]=kon.rs.getString("kd_brg"); 
                datasementara[x][1]=kon.rs.getString("nm_kategori"); 
                datasementara[x][2]=kon.rs.getString("nm_brg"); 
                datasementara[x][3]=kon.rs.getString("jml_order"); 
                x++; 
            } 
            tbl_order.setModel(new DefaultTableModel(datasementara,label)); 
            } 
            catch(SQLException e) 
            { 
            JOptionPane.showMessageDialog(null, e); 
            } 
    }  

private void TampilDataBarang() 
    { 
        try{ 
            String sql="select *from tb_barang where kd_brg='" +tkd_brg.getText()+"'"; 
            kon.rs=kon.st.executeQuery(sql); 
            if(kon.rs.next()) 
            { 
                tnm_kategori.setText(kon.rs.getString("nm_kategori")); 
                tnm_brg.setText(kon.rs.getString("nm_brg")); 
            } 
            else 
            { 
                JOptionPane.showMessageDialog(null, "Kode Barang"+tkd_brg.getText()+"tidak ditemukan"); 
            } 
        } 
        catch(SQLException e) 
        { 
            JOptionPane.showMessageDialog(null, e); 
        } 
    } 
     
    private void SimpanSementara() 
   { 
        try 
        { 
            String sql="insert into tb_sementara_orderinventory values('"+tkd_brg.getText()+"'," 
                    + "'"+tnm_kategori.getText()+"'," 
                    + "'"+tnm_brg.getText()+"'," 
                    + "'"+tjml_order.getText()+"')"; 
            kon.st.executeUpdate(sql); 
            TampilanTabelSementara(); 
        } 
        catch(SQLException e) 
        { 
            JOptionPane.showMessageDialog(null, e); 
        } 
    } 
     
     private void HapusIsiSementara() 
   { 
        int row=tbl_order.getSelectedRow(); 
            try 
            { 
                String sql="Delete from tb_sementara_orderinventory where kd_brg='"+(String)tbl_order.getValueAt(row,0)+"'"; 
                kon.st.executeUpdate(sql); 
                TampilanTabelSementara(); 
            } 
            catch(SQLException e){ 
            JOptionPane.showMessageDialog(null, e); 
            }  
    } 
      
    private void setTanggalskr() 
    { 
    java.util.Date skrg = new java.util.Date();

    java.text.SimpleDateFormat tgl = new 
java.text.SimpleDateFormat("yyyy-MM-dd"); 
    ttgl_order.setText(tgl.format(skrg)); 
    }  
     
    private void SimpanOrder() 
   { 
        try 
        { 
            String sql="insert into tb_order_inventory values('"+tid_order.getText()+"'," 
                    + "'"+ttgl_order.getText()+"'," 
                    + "'"+tid_user.getText()+"')"; 
            kon.st.executeUpdate(sql); 
             
            String detail="insert tb_detail_orderinventory select '"+tid_order.getText()+"'," 
                 +"kd_brg,jml_order from tb_sementara_orderinventory"; 
            kon.st.executeUpdate(detail); 
            Bersih(); 
        } 
        catch(SQLException e) 
        { 
            System.out.println("koneksi gagal"+e.toString()); 
        } 
    } 
     
    private void HapusTabelSementara() 
    { 
        try
    { 
            String sql="Delete from tb_sementara_orderinventory"; 
            kon.st.executeUpdate(sql); 
            TampilanTabelSementara(); 
        } 
        catch(SQLException e) 
        { 
            JOptionPane.showMessageDialog(null, e); 
        } 
    } 
        
    private void cetakstruk() 
    { 
        try { 
        // Ubah path sesuai struktur folder Maven
        String file = "src/main/java/gudang/PO.jasper"; 
        
        HashMap<String, Object> param = new HashMap<>(); 
        param.put("idorder", tid_order.getText()); 
        
        JasperPrint print = JasperFillManager.fillReport(file, param, kon.setkoneksi()); 
        JasperViewer.viewReport(print, false); 
    } catch (Exception e) { 
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Gagal Mencetak Struk PO: " + e.getMessage()); 
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

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tid_order = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        ttgl_order = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tid_user = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tkd_brg = new javax.swing.JTextField();
        bt_browse = new javax.swing.JButton();
        tnm_kategori = new javax.swing.JTextField();
        tnm_brg = new javax.swing.JTextField();
        tjml_order = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_order = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        bt_tambah = new javax.swing.JButton();
        bt_insert = new javax.swing.JButton();
        bt_print = new javax.swing.JButton();
        bt_update = new javax.swing.JButton();
        bt_hapus = new javax.swing.JButton();
        bt_batal = new javax.swing.JButton();
        bt_keluar = new javax.swing.JButton();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jPanel2.setBackground(new java.awt.Color(153, 255, 153));

        jLabel1.setText("ID Order");

        jLabel2.setText("Tgl. Order");

        jLabel3.setText("ID User");

        tid_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tid_userActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tid_user, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ttgl_order, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tid_order, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tid_order, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ttgl_order)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tid_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(153, 255, 153));

        jLabel4.setText("Kode Barang");

        jLabel5.setText("Kategori");

        jLabel6.setText("Nama Barang");

        jLabel7.setText("Jumlah Order");

        tkd_brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tkd_brgActionPerformed(evt);
            }
        });

        bt_browse.setText("...");
        bt_browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_browseActionPerformed(evt);
            }
        });

        tjml_order.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tjml_orderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tkd_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bt_browse, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tjml_order, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(tkd_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tnm_kategori, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tnm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(tjml_order, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbl_order.setModel(new javax.swing.table.DefaultTableModel(
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
        tbl_order.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_orderMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbl_order);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBackground(new java.awt.Color(153, 255, 153));

        bt_tambah.setText("Tambah");
        bt_tambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_tambahActionPerformed(evt);
            }
        });

        bt_insert.setText("Insert");
        bt_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_insertActionPerformed(evt);
            }
        });

        bt_print.setText("Print");
        bt_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_printActionPerformed(evt);
            }
        });

        bt_update.setText("Update");
        bt_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_updateActionPerformed(evt);
            }
        });

        bt_hapus.setText("Hapus");
        bt_hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_hapusActionPerformed(evt);
            }
        });

        bt_batal.setText("Batal");
        bt_batal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_batalActionPerformed(evt);
            }
        });

        bt_keluar.setText("Tutup");
        bt_keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bt_keluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bt_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bt_print, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bt_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bt_keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bt_keluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_tambah, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_print, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bt_insert, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_update, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bt_batal, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 584, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tid_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tid_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tid_userActionPerformed

    private void tjml_orderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tjml_orderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tjml_orderActionPerformed

    private void bt_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_insertActionPerformed
        // TODO add your handling code here:
        try 
        { 
            String sql="select *from tb_sementara_orderinventory where kd_brg='" +tkd_brg.getText()+ "'"; 
            kon.rs=kon.st.executeQuery(sql);
            if(kon.rs.next()) 
            { 
                JOptionPane.showMessageDialog(this,"Kode Sudah Dipilih...","Informasi", JOptionPane.INFORMATION_MESSAGE); 
                bt_browse.requestFocus(); 
                tkd_brg.setText(""); 
            } 
            else 
            { 
                SimpanSementara(); 
                tkd_brg.requestFocus(); 
                tkd_brg.setText(""); 
                tjml_order.setText(""); 
                tnm_kategori.setText(""); 
                tnm_brg.setText(""); 
                tjml_order.setText(""); 
                if (JOptionPane.showConfirmDialog(this, "Mau Tambah Barang?", "konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { 
                } 
                else 
                { 
                    bt_print.requestFocus(); 
                } 
            } 
        } 
        catch(SQLException e) 
        { 
            JOptionPane.showMessageDialog(null, e); 
        }
    }//GEN-LAST:event_bt_insertActionPerformed

    private void tkd_brgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkd_brgActionPerformed
        // TODO add your handling code here:
        TampilDataBarang();
    }//GEN-LAST:event_tkd_brgActionPerformed

    private void bt_browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_browseActionPerformed
        // TODO add your handling code here:
         boolean closable = true; 
        Form_Stok_Barang_MinimalPM databarang=new Form_Stok_Barang_MinimalPM(null, closable); 
        databarang.barang1 = this; 
        databarang.setVisible(true); 
        databarang.setResizable(true); 
        tkd_brg.setText(KdBrg); 
        tnm_kategori.setText(NmKategori); 
        tnm_brg.setText(NmBrg); 
        tjml_order.setEnabled(true); 
        bt_insert.setEnabled(true);
    }//GEN-LAST:event_bt_browseActionPerformed

    private void tbl_orderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_orderMouseClicked
        // TODO add your handling code here:
        aktif(); 
        tkd_brg.setEnabled(true); 
        bt_update.setEnabled(true); 
        bt_hapus.setEnabled(true); 
        bt_tambah.setEnabled(false); 
        bt_insert.setEnabled(false); 
        setTable(); 
    }//GEN-LAST:event_tbl_orderMouseClicked

    private void bt_tambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_tambahActionPerformed
        // TODO add your handling code here:
        NoOrder(); 
        setTanggalskr(); 
        bt_batal.setEnabled(true); 
        bt_tambah.setEnabled(false); 
        bt_print.setEnabled(true); 
        bt_browse.setEnabled(true); 
        tid_order.setEnabled(true); 
        ttgl_order.setEnabled(true); 
        tid_user.setEnabled(true); 
        tkd_brg.setEnabled(true); 
        tnm_kategori.setEnabled(true); 
        tnm_brg.setEnabled(true);
    }//GEN-LAST:event_bt_tambahActionPerformed

    private void bt_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_printActionPerformed
        // TODO add your handling code here:
        SimpanOrder(); 
    
    // 2. Cetak struk SEBELUM textfield dibersihkan!
    cetakstruk(); 
    
    // 3. Bersihkan tabel sementara dan inputan
    HapusTabelSementara(); 
    nonaktif(); 
    Bersih(); 
    tid_order.setText(""); 
    bt_tambah.setEnabled(true); 
    
    JOptionPane.showMessageDialog(this, "Berhasil disimpan dan dicetak", 
        "Informasi", JOptionPane.INFORMATION_MESSAGE); 
    }//GEN-LAST:event_bt_printActionPerformed

    private void bt_hapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_hapusActionPerformed
        // TODO add your handling code here:
        HapusIsiSementara();
    }//GEN-LAST:event_bt_hapusActionPerformed

    private void bt_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_updateActionPerformed
        // TODO add your handling code here:
        EditData(); 
        tkd_brg.setEnabled(true); 
        bt_update.setEnabled(false); 
        bt_hapus.setEnabled(false); 
        bt_tambah.setEnabled(false); 
        bt_insert.setEnabled(true); 
        bt_keluar.setEnabled(true);
    }//GEN-LAST:event_bt_updateActionPerformed

    private void bt_batalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_batalActionPerformed
        // TODO add your handling code here:
        nonaktif(); 
        HapusTabelSementara(); 
        Bersih(); 
        bt_tambah.setEnabled(true); 
    }//GEN-LAST:event_bt_batalActionPerformed

    private void bt_keluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bt_keluarActionPerformed
        // TODO add your handling code here:
        HapusTabelSementara(); 
        dispose(); 
    }//GEN-LAST:event_bt_keluarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Form_Transaksi_Inventory().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bt_batal;
    private javax.swing.JButton bt_browse;
    private javax.swing.JButton bt_hapus;
    private javax.swing.JButton bt_insert;
    private javax.swing.JButton bt_keluar;
    private javax.swing.JButton bt_print;
    private javax.swing.JButton bt_tambah;
    private javax.swing.JButton bt_update;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_order;
    private javax.swing.JTextField tid_order;
    private javax.swing.JTextField tid_user;
    private javax.swing.JTextField tjml_order;
    private javax.swing.JTextField tkd_brg;
    private javax.swing.JTextField tnm_brg;
    private javax.swing.JTextField tnm_kategori;
    private javax.swing.JTextField ttgl_order;
    // End of variables declaration//GEN-END:variables
}
