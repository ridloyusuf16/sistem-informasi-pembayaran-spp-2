/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import project.spp.Koneksi.db_con;
import project.spp.User.Admin;
import project.spp.User.Petugas;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Yusuf Ridlo
 */
public class dashboard extends javax.swing.JFrame {
    Petugas petugas = new Petugas();
    Admin admin = new Admin();
    private String nis;
    private String idpetugas;
    private String idspp;
    private String id_pemb;
    private String pengguna;
    private String iduser;
    private String namauser;
    private String ket;
    private String IdSiswa;
    private int dragxmouse;
    private int dragymouse;
    
    
    
    
    public void setPengguna(String p){
        pengguna = p;
    }
    
    public void setIdUser(String iduser){
        this.iduser = iduser;
    }
    
    public void setNamaUser(String namauser){
        this.namauser = namauser;
    }
    
    private void kosongsiswa(){
        txtnis.setText(null);
        txtnamasiswa.setText(null);
        cmbjk.setSelectedItem("-- Pilih --");
        txtalamat.setText(null); 
        cmbkelas.setSelectedItem("-- Pilih --");
        cmbtahun.setSelectedItem("-- Pilih --");
        
    }
    
    private void kosongPetugas(){
        txtnamapetugas.setText(null);
        cmblevel.setSelectedItem("-- Pilih --");
        txtuser.setText(null);  
        txtpass.setText(null);         
    }
    
    private void kosongSPP(){
        txttahunspp.setText(null);
        txtnominal.setText(null);      
    }
    
    public void load_cmbkelas(){
        try {
            String sql = "SELECT * FROM kelas";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                cmbkelas.addItem(res.getString("kelas"));
            }
            
            res.last();
            int jumlahdata = res.getRow();
            res.first();
        } catch (Exception e) {
        }
    }
    
    public void load_cmbtahun(){
        try {
            String sql = "SELECT * FROM spp";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            
            while(res.next()){
                cmbtahun.addItem(res.getString("Tahun"));
            }
            
            res.last();
            int jumlahdata = res.getRow();
            res.first();
        } catch (Exception e) {
        }
    }
    
    public void loadTabelSiswa(){
         // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Siswa");
        model.addColumn("NIS");
        model.addColumn("Nama Siswa");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Alamat");
        model.addColumn("Kelas");
        model.addColumn("Tahun");
        tabelsiswa.setModel(model);
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            String sql = "SELECT * FROM siswa INNER JOIN spp ON siswa.id_spp = spp.id_spp ORDER BY NIS ASC";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1), res.getString(2),res.getString(3),res.getString(4),res.getString(5), res.getString(6), res.getString("Tahun")});
            }
            
        } catch (Exception e) {
        }
    }
    
    public void loadTabelSiswaByNIS(String nist){
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Siswa");
        model.addColumn("NIS");
        model.addColumn("Nama");
        model.addColumn("Jenis Kelamin");
        model.addColumn("Alamat");
        model.addColumn("Kelas");
        model.addColumn("Tahun");
        
        //menampilkan data database kedalam tabel
        try {
            String sql = "SELECT * FROM siswa INNER JOIN spp ON siswa.id_spp = spp.id_spp WHERE NIS = '"+nist+"' ORDER BY Kelas ASC";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
                while(res.next()){
                model.addRow(new Object[]{res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5), res.getString(6), res.getString("Tahun")});
                //nis = res.getString("NIS");
                }
            
            tabelsiswabynis.setModel(model);
            loadTabelPembayaranByNIS(nist);
        } catch (Exception e) {
            System.err.println("koneksi gagal "+e.getMessage());
        }
    }
    
    public void loadTabelPembayaranByNIS(String nis_siswa){
        // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Pembayaran");
        model.addColumn("NIS");
        model.addColumn("Bulan Dibayar");
        model.addColumn("Tahun Dibayar");
        model.addColumn("Nominal");
        model.addColumn("Waktu Pembayaran");
        model.addColumn("Keterangan");
        model.addColumn("Nama Petugas");
        model.addColumn("ID Petugas");
        
        
        //menampilkan data database kedalam tabel
        try {

            String sql = "SELECT p.id_pembayaran, p.nis, p.bulan_dibayar, s.tahun, s.nominal, p.tgl_dibayar, p.keterangan, pet.nama, pet.id_petugas FROM pembayaran AS p INNER JOIN spp AS s ON p.id_spp = s.id_spp LEFT JOIN petugas AS pet ON p.id_petugas = pet.id_petugas WHERE p.NIS = '"+nis_siswa+"' ORDER BY p.id_pembayaran ASC";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{res.getString(1),res.getString(2), res.getString(3),res.getString(4),res.getString(5),res.getString(6), res.getString(7), res.getString(8), res.getString(9)});
            }
        tabelpembbynis.setModel(model);  
        } catch (Exception e) {
           System.err.println("Salah "+e.getMessage());
        }
    }
    
    public void loadTabelPetugas(){
         // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID Petugas");
        model.addColumn("Nama");
        model.addColumn("Level");
        model.addColumn("Username");
        model.addColumn("Password");
        tabelpetugas.setModel(model);
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            String sql = "SELECT * FROM petugas";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)});
            }
            
        } catch (Exception e) {
        }

    }
    
    public void loadTabelSPP(){
         // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("ID SPP");
        model.addColumn("Tahun");
        model.addColumn("Nominal");
        tabelspp.setModel(model);
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            String sql = "SELECT * FROM spp";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(3)});
            }
            
        } catch (Exception e) {
        }
    }
    
    public void loadTabelLaporan(String tgldari, String tglsampai){
         // membuat tampilan model tabel
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("NIS");
        model.addColumn("Nama Siswa");
        model.addColumn("Bulan");
        model.addColumn("Tahun");
        model.addColumn("Nominal");
        model.addColumn("Tanggal Dibayar");
        tabellaporan.setModel(model);
        //menampilkan data database kedalam tabel
        try {
            int no=1;
            String sql = "SELECT s.NIS, s.Nama, p.tgl_dibayar, p.bulan_dibayar, spp.tahun, spp.nominal FROM siswa AS s INNER JOIN pembayaran AS p ON s.id_siswa = p.id_siswa INNER JOIN spp AS spp ON p.id_spp = spp.id_spp WHERE p.tgl_dibayar BETWEEN '"+tgldari+"' and '"+tglsampai+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            while(res.next()){
                model.addRow(new Object[]{no++,res.getString(1),res.getString(2),res.getString(4), res.getString(5), res.getString(6), res.getString(3)});
            }
            
        } catch (Exception e) {
        }
    }
    /**
     * Creates new form dashboard
     */
    public dashboard() {
        initComponents();
        pagesiswa.setVisible(false);
        pagepetugas.setVisible(false);
        pagespp.setVisible(false);
        pagetransaksi.setVisible(false);
        pagelaporan.setVisible(false);
        pagehome.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnhome = new javax.swing.JButton();
        btndatasiswa = new javax.swing.JButton();
        btndatapetugas = new javax.swing.JButton();
        btndataspp = new javax.swing.JButton();
        btntransaksi = new javax.swing.JButton();
        btnlaporan = new javax.swing.JButton();
        btnlogout = new javax.swing.JButton();
        btnexit = new javax.swing.JButton();
        menu = new javax.swing.JLabel();
        menu1 = new javax.swing.JLabel();
        menu2 = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        pagehome = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel28 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        pagesiswa = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelsiswa = new javax.swing.JTable();
        txtnis = new javax.swing.JTextField();
        txtnamasiswa = new javax.swing.JTextField();
        cmbjk = new javax.swing.JComboBox<>();
        txtalamat = new javax.swing.JTextField();
        cmbkelas = new javax.swing.JComboBox<>();
        cmbtahun = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnthnajaran = new javax.swing.JButton();
        btnsimpansiswa = new javax.swing.JButton();
        btneditsiswa = new javax.swing.JButton();
        btncancelsiswa = new javax.swing.JButton();
        btnhapussiswa = new javax.swing.JButton();
        pagepetugas = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelpetugas = new javax.swing.JTable();
        txtnamapetugas = new javax.swing.JTextField();
        txtuser = new javax.swing.JTextField();
        txtpass = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        btnsimpanpetugas = new javax.swing.JButton();
        btneditpetugas = new javax.swing.JButton();
        btncancelpetugas = new javax.swing.JButton();
        btnhapuspetugas = new javax.swing.JButton();
        cmblevel = new javax.swing.JComboBox<>();
        pagespp = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelspp = new javax.swing.JTable();
        txttahunspp = new javax.swing.JTextField();
        txtnominal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        btnsimpanspp = new javax.swing.JButton();
        btneditspp = new javax.swing.JButton();
        btncancelspp = new javax.swing.JButton();
        btnhapusspp = new javax.swing.JButton();
        pagetransaksi = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtcarinis = new javax.swing.JTextField();
        btncarinissiswa = new javax.swing.JButton();
        pesancari = new javax.swing.JLabel();
        dtpem = new javax.swing.JLabel();
        scrtbsiswa = new javax.swing.JScrollPane();
        tabelsiswabynis = new javax.swing.JTable();
        scrtbpemb = new javax.swing.JScrollPane();
        tabelpembbynis = new javax.swing.JTable();
        btnprint = new javax.swing.JButton();
        btnbayar = new javax.swing.JButton();
        pagelaporan = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        btncetak = new javax.swing.JButton();
        btnlihat = new javax.swing.JButton();
        scrtblap = new javax.swing.JScrollPane();
        tabellaporan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1250, 700));
        getContentPane().setLayout(null);

        btnhome.setBackground(new java.awt.Color(204, 204, 204));
        btnhome.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnhome.setForeground(new java.awt.Color(0, 102, 0));
        btnhome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/home.png"))); // NOI18N
        btnhome.setText("Home");
        btnhome.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnhome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhomeActionPerformed(evt);
            }
        });
        getContentPane().add(btnhome);
        btnhome.setBounds(20, 240, 140, 50);

        btndatasiswa.setBackground(new java.awt.Color(204, 204, 204));
        btndatasiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btndatasiswa.setForeground(new java.awt.Color(0, 102, 0));
        btndatasiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/siswa.png"))); // NOI18N
        btndatasiswa.setText("Data Siswa");
        btndatasiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btndatasiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndatasiswaActionPerformed(evt);
            }
        });
        getContentPane().add(btndatasiswa);
        btndatasiswa.setBounds(20, 300, 140, 50);

        btndatapetugas.setBackground(new java.awt.Color(204, 204, 204));
        btndatapetugas.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btndatapetugas.setForeground(new java.awt.Color(0, 102, 0));
        btndatapetugas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/petugas.png"))); // NOI18N
        btndatapetugas.setText("Data Petugas");
        btndatapetugas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btndatapetugas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btndatapetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndatapetugasActionPerformed(evt);
            }
        });
        getContentPane().add(btndatapetugas);
        btndatapetugas.setBounds(20, 360, 140, 50);

        btndataspp.setBackground(new java.awt.Color(204, 204, 204));
        btndataspp.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btndataspp.setForeground(new java.awt.Color(0, 102, 0));
        btndataspp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/spp.png"))); // NOI18N
        btndataspp.setText("Data SPP");
        btndataspp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btndataspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btndatasppActionPerformed(evt);
            }
        });
        getContentPane().add(btndataspp);
        btndataspp.setBounds(20, 420, 140, 50);

        btntransaksi.setBackground(new java.awt.Color(204, 204, 204));
        btntransaksi.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btntransaksi.setForeground(new java.awt.Color(0, 102, 0));
        btntransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/transaksi.png"))); // NOI18N
        btntransaksi.setText("Transaksi");
        btntransaksi.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btntransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntransaksiActionPerformed(evt);
            }
        });
        getContentPane().add(btntransaksi);
        btntransaksi.setBounds(20, 480, 140, 50);

        btnlaporan.setBackground(new java.awt.Color(204, 204, 204));
        btnlaporan.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnlaporan.setForeground(new java.awt.Color(0, 102, 0));
        btnlaporan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/laporan.png"))); // NOI18N
        btnlaporan.setText("Laporan");
        btnlaporan.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnlaporan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnlaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlaporanActionPerformed(evt);
            }
        });
        getContentPane().add(btnlaporan);
        btnlaporan.setBounds(20, 540, 140, 50);

        btnlogout.setBackground(new java.awt.Color(204, 204, 204));
        btnlogout.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnlogout.setForeground(new java.awt.Color(0, 102, 51));
        btnlogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/logout.png"))); // NOI18N
        btnlogout.setText("Logout");
        btnlogout.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnlogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlogoutActionPerformed(evt);
            }
        });
        getContentPane().add(btnlogout);
        btnlogout.setBounds(20, 600, 140, 50);

        btnexit.setBackground(new java.awt.Color(153, 153, 153));
        btnexit.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnexit.setForeground(new java.awt.Color(0, 102, 51));
        btnexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/exit.png"))); // NOI18N
        btnexit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnexit.setBorderPainted(false);
        btnexit.setContentAreaFilled(false);
        btnexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnexitActionPerformed(evt);
            }
        });
        getContentPane().add(btnexit);
        btnexit.setBounds(1160, 10, 40, 40);

        menu.setBackground(new java.awt.Color(255, 255, 255));
        menu.setFont(new java.awt.Font("Bernard MT Condensed", 1, 30)); // NOI18N
        menu.setForeground(new java.awt.Color(255, 255, 255));
        menu.setText("Menu");
        getContentPane().add(menu);
        menu.setBounds(50, 180, 70, 30);

        menu1.setBackground(new java.awt.Color(255, 255, 255));
        menu1.setFont(new java.awt.Font("Bernard MT Condensed", 1, 50)); // NOI18N
        menu1.setForeground(new java.awt.Color(255, 255, 255));
        menu1.setText("SMKS \"Dar Al-Raudhah\"");
        getContentPane().add(menu1);
        menu1.setBounds(420, 100, 580, 60);

        menu2.setBackground(new java.awt.Color(255, 255, 255));
        menu2.setFont(new java.awt.Font("Bernard MT Condensed", 1, 40)); // NOI18N
        menu2.setForeground(new java.awt.Color(255, 255, 255));
        menu2.setText("Aplikasi Pembayaran SPP");
        getContentPane().add(menu2);
        menu2.setBounds(450, 30, 450, 60);

        pagehome.setBackground(new java.awt.Color(238, 238, 239));
        pagehome.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel20.setText("- Jika anda seorang Petugas, anda hanya diperbolehkan untuk mengakses menu transaksi.");
        pagehome.add(jLabel20);
        jLabel20.setBounds(70, 230, 950, 60);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 42)); // NOI18N
        jLabel19.setText("Selamat Datang!");
        pagehome.add(jLabel19);
        jLabel19.setBounds(70, 30, 460, 60);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setText("Sedikit tentang aplikasi :");
        pagehome.add(jLabel21);
        jLabel21.setBounds(70, 320, 460, 60);

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel27.setText("  menggunakannya satu tahun sekali, yaitu pada saat tahun ajaran baru akan dimulai.");
        pagehome.add(jLabel27);
        jLabel27.setBounds(70, 200, 950, 60);

        jScrollPane4.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane4.setBorder(null);

        jTextPane1.setBackground(new java.awt.Color(238, 238, 239));
        jTextPane1.setBorder(null);
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextPane1.setText("Aplikasi ini dibuat dengan tujuan agar pengelolaan pembayaran SPP di sebuah lembaga atau sekolah dapat dilakukan dengan lebih cepat dan mudah karena tidak lagi dilakukan secara manual.");
        jScrollPane4.setViewportView(jTextPane1);

        pagehome.add(jScrollPane4);
        jScrollPane4.setBounds(70, 380, 750, 70);

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel28.setText("- Jika anda seorang Admin, anda diperbolehkan untuk mengakses menu data siswa, menu data petugas, menu data spp, ");
        pagehome.add(jLabel28);
        jLabel28.setBounds(70, 160, 950, 60);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setText("Petunjuk Penggunaan Menu :");
        pagehome.add(jLabel26);
        jLabel26.setBounds(70, 110, 460, 60);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel29.setText("  dan menu laporan pembayaran. Anda juga dapat menggunakan menu tahun ajaran baru, tetapi pastikan bahwa anda");
        pagehome.add(jLabel29);
        jLabel29.setBounds(70, 180, 950, 60);

        jLayeredPane1.add(pagehome);
        pagehome.setBounds(0, 0, 1010, 480);

        pagesiswa.setBackground(new java.awt.Color(238, 238, 239));
        pagesiswa.setEnabled(false);
        pagesiswa.setPreferredSize(new java.awt.Dimension(1010, 460));
        pagesiswa.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel1.setText("Data Siswa");
        pagesiswa.add(jLabel1);
        jLabel1.setBounds(440, 10, 150, 40);

        tabelsiswa.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabelsiswa.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelsiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelsiswaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelsiswa);

        pagesiswa.add(jScrollPane1);
        jScrollPane1.setBounds(20, 230, 970, 210);

        txtnis.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtnis.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtnis.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pagesiswa.add(txtnis);
        txtnis.setBounds(170, 100, 200, 30);

        txtnamasiswa.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtnamasiswa.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtnamasiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pagesiswa.add(txtnamasiswa);
        txtnamasiswa.setBounds(170, 140, 200, 30);

        cmbjk.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        cmbjk.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Laki-laki", "Perempuan" }));
        pagesiswa.add(cmbjk);
        cmbjk.setBounds(170, 180, 200, 30);

        txtalamat.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtalamat.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtalamat.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        pagesiswa.add(txtalamat);
        txtalamat.setBounds(540, 100, 200, 30);

        cmbkelas.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        cmbkelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --" }));
        pagesiswa.add(cmbkelas);
        cmbkelas.setBounds(540, 140, 200, 30);

        cmbtahun.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        cmbtahun.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --" }));
        pagesiswa.add(cmbtahun);
        cmbtahun.setBounds(540, 180, 200, 30);

        jLabel2.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 51));
        jLabel2.setText("Nama Lengkap");
        pagesiswa.add(jLabel2);
        jLabel2.setBounds(40, 140, 120, 30);

        jLabel3.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 51));
        jLabel3.setText("Jenis Kelamin");
        pagesiswa.add(jLabel3);
        jLabel3.setBounds(40, 180, 120, 30);

        jLabel4.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 51));
        jLabel4.setText("Alamat");
        pagesiswa.add(jLabel4);
        jLabel4.setBounds(470, 100, 80, 30);

        jLabel5.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 51));
        jLabel5.setText("Kelas");
        pagesiswa.add(jLabel5);
        jLabel5.setBounds(470, 140, 80, 30);

        jLabel6.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 51));
        jLabel6.setText("Tahun");
        pagesiswa.add(jLabel6);
        jLabel6.setBounds(470, 180, 80, 30);

        jLabel7.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 51));
        jLabel7.setText("NIS");
        pagesiswa.add(jLabel7);
        jLabel7.setBounds(40, 100, 120, 30);

        btnthnajaran.setBackground(new java.awt.Color(204, 204, 204));
        btnthnajaran.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnthnajaran.setForeground(new java.awt.Color(0, 102, 0));
        btnthnajaran.setText("Tahun Ajaran Baru");
        btnthnajaran.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnthnajaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthnajaranActionPerformed(evt);
            }
        });
        pagesiswa.add(btnthnajaran);
        btnthnajaran.setBounds(830, 20, 140, 50);

        btnsimpansiswa.setBackground(new java.awt.Color(204, 204, 204));
        btnsimpansiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnsimpansiswa.setForeground(new java.awt.Color(51, 51, 51));
        btnsimpansiswa.setText("Tambah");
        btnsimpansiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnsimpansiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpansiswaActionPerformed(evt);
            }
        });
        pagesiswa.add(btnsimpansiswa);
        btnsimpansiswa.setBounds(790, 110, 80, 40);

        btneditsiswa.setBackground(new java.awt.Color(204, 204, 204));
        btneditsiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btneditsiswa.setForeground(new java.awt.Color(51, 51, 51));
        btneditsiswa.setText("Edit");
        btneditsiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btneditsiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditsiswaActionPerformed(evt);
            }
        });
        pagesiswa.add(btneditsiswa);
        btneditsiswa.setBounds(900, 110, 80, 40);

        btncancelsiswa.setBackground(new java.awt.Color(204, 204, 204));
        btncancelsiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btncancelsiswa.setForeground(new java.awt.Color(51, 51, 51));
        btncancelsiswa.setText("Cancel");
        btncancelsiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btncancelsiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelsiswaActionPerformed(evt);
            }
        });
        pagesiswa.add(btncancelsiswa);
        btncancelsiswa.setBounds(790, 160, 80, 40);

        btnhapussiswa.setBackground(new java.awt.Color(204, 204, 204));
        btnhapussiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnhapussiswa.setForeground(new java.awt.Color(51, 51, 51));
        btnhapussiswa.setText("Hapus");
        btnhapussiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnhapussiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapussiswaActionPerformed(evt);
            }
        });
        pagesiswa.add(btnhapussiswa);
        btnhapussiswa.setBounds(900, 160, 80, 40);

        jLayeredPane1.add(pagesiswa);
        pagesiswa.setBounds(0, 0, 1010, 460);

        pagepetugas.setBackground(new java.awt.Color(238, 238, 239));
        pagepetugas.setEnabled(false);
        pagepetugas.setPreferredSize(new java.awt.Dimension(1010, 460));
        pagepetugas.setLayout(null);

        jLabel8.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel8.setText("Data Petugas");
        pagepetugas.add(jLabel8);
        jLabel8.setBounds(450, 20, 160, 40);

        tabelpetugas.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabelpetugas.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelpetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpetugasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelpetugas);

        pagepetugas.add(jScrollPane2);
        jScrollPane2.setBounds(20, 250, 970, 190);

        txtnamapetugas.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtnamapetugas.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        pagepetugas.add(txtnamapetugas);
        txtnamapetugas.setBounds(170, 100, 200, 30);

        txtuser.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtuser.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        pagepetugas.add(txtuser);
        txtuser.setBounds(540, 100, 200, 30);

        txtpass.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtpass.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        pagepetugas.add(txtpass);
        txtpass.setBounds(540, 160, 200, 30);

        jLabel10.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 51));
        jLabel10.setText("Level");
        pagepetugas.add(jLabel10);
        jLabel10.setBounds(90, 160, 80, 30);

        jLabel12.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 51));
        jLabel12.setText("Username");
        pagepetugas.add(jLabel12);
        jLabel12.setBounds(420, 100, 100, 30);

        jLabel13.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 51));
        jLabel13.setText("Password");
        pagepetugas.add(jLabel13);
        jLabel13.setBounds(420, 160, 100, 30);

        jLabel15.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 51));
        jLabel15.setText("Nama");
        pagepetugas.add(jLabel15);
        jLabel15.setBounds(90, 100, 80, 30);

        btnsimpanpetugas.setBackground(new java.awt.Color(204, 204, 204));
        btnsimpanpetugas.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnsimpanpetugas.setForeground(new java.awt.Color(51, 51, 51));
        btnsimpanpetugas.setText("Simpan");
        btnsimpanpetugas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnsimpanpetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpanpetugasActionPerformed(evt);
            }
        });
        pagepetugas.add(btnsimpanpetugas);
        btnsimpanpetugas.setBounds(790, 90, 80, 40);

        btneditpetugas.setBackground(new java.awt.Color(204, 204, 204));
        btneditpetugas.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btneditpetugas.setForeground(new java.awt.Color(51, 51, 51));
        btneditpetugas.setText("Edit");
        btneditpetugas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btneditpetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditpetugasActionPerformed(evt);
            }
        });
        pagepetugas.add(btneditpetugas);
        btneditpetugas.setBounds(900, 90, 80, 40);

        btncancelpetugas.setBackground(new java.awt.Color(204, 204, 204));
        btncancelpetugas.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btncancelpetugas.setForeground(new java.awt.Color(51, 51, 51));
        btncancelpetugas.setText("Cancel");
        btncancelpetugas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btncancelpetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelpetugasActionPerformed(evt);
            }
        });
        pagepetugas.add(btncancelpetugas);
        btncancelpetugas.setBounds(790, 160, 80, 40);

        btnhapuspetugas.setBackground(new java.awt.Color(204, 204, 204));
        btnhapuspetugas.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnhapuspetugas.setForeground(new java.awt.Color(51, 51, 51));
        btnhapuspetugas.setText("Hapus");
        btnhapuspetugas.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnhapuspetugas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapuspetugasActionPerformed(evt);
            }
        });
        pagepetugas.add(btnhapuspetugas);
        btnhapuspetugas.setBounds(900, 160, 80, 40);

        cmblevel.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        cmblevel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Pilih --", "Admin", "Petugas" }));
        pagepetugas.add(cmblevel);
        cmblevel.setBounds(170, 160, 200, 30);

        jLayeredPane1.add(pagepetugas);
        pagepetugas.setBounds(0, 0, 1010, 460);

        pagespp.setBackground(new java.awt.Color(238, 238, 239));
        pagespp.setEnabled(false);
        pagespp.setPreferredSize(new java.awt.Dimension(1010, 460));
        pagespp.setLayout(null);

        jLabel11.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel11.setText("Data SPP");
        pagespp.add(jLabel11);
        jLabel11.setBounds(460, 20, 100, 40);

        tabelspp.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabelspp.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelspp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelsppMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelspp);

        pagespp.add(jScrollPane3);
        jScrollPane3.setBounds(20, 260, 970, 160);

        txttahunspp.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txttahunspp.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txttahunspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttahunsppActionPerformed(evt);
            }
        });
        pagespp.add(txttahunspp);
        txttahunspp.setBounds(260, 130, 200, 30);

        txtnominal.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtnominal.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        pagespp.add(txtnominal);
        txtnominal.setBounds(260, 180, 200, 30);

        jLabel16.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 51));
        jLabel16.setText("Nominal");
        pagespp.add(jLabel16);
        jLabel16.setBounds(130, 180, 100, 30);

        jLabel18.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 51));
        jLabel18.setText("Tahun");
        pagespp.add(jLabel18);
        jLabel18.setBounds(130, 130, 100, 30);

        btnsimpanspp.setBackground(new java.awt.Color(204, 204, 204));
        btnsimpanspp.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnsimpanspp.setForeground(new java.awt.Color(51, 51, 51));
        btnsimpanspp.setText("Simpan");
        btnsimpanspp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnsimpanspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsimpansppActionPerformed(evt);
            }
        });
        pagespp.add(btnsimpanspp);
        btnsimpanspp.setBounds(580, 120, 80, 40);

        btneditspp.setBackground(new java.awt.Color(204, 204, 204));
        btneditspp.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btneditspp.setForeground(new java.awt.Color(51, 51, 51));
        btneditspp.setText("Edit");
        btneditspp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btneditspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditsppActionPerformed(evt);
            }
        });
        pagespp.add(btneditspp);
        btneditspp.setBounds(720, 120, 80, 40);

        btncancelspp.setBackground(new java.awt.Color(204, 204, 204));
        btncancelspp.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btncancelspp.setForeground(new java.awt.Color(51, 51, 51));
        btncancelspp.setText("Cancel");
        btncancelspp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btncancelspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelsppActionPerformed(evt);
            }
        });
        pagespp.add(btncancelspp);
        btncancelspp.setBounds(580, 170, 80, 40);

        btnhapusspp.setBackground(new java.awt.Color(204, 204, 204));
        btnhapusspp.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnhapusspp.setForeground(new java.awt.Color(51, 51, 51));
        btnhapusspp.setText("Hapus");
        btnhapusspp.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnhapusspp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnhapussppActionPerformed(evt);
            }
        });
        pagespp.add(btnhapusspp);
        btnhapusspp.setBounds(720, 170, 80, 40);

        jLayeredPane1.add(pagespp);
        pagespp.setBounds(0, 0, 1010, 460);

        pagetransaksi.setBackground(new java.awt.Color(238, 238, 239));
        pagetransaksi.setPreferredSize(new java.awt.Dimension(1010, 460));
        pagetransaksi.setLayout(null);

        jLabel14.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 51));
        jLabel14.setText("Masukkan NIS");
        pagetransaksi.add(jLabel14);
        jLabel14.setBounds(60, 80, 170, 20);

        jLabel17.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel17.setText("Transaksi");
        pagetransaksi.add(jLabel17);
        jLabel17.setBounds(480, 30, 140, 29);

        txtcarinis.setFont(new java.awt.Font("Cambria", 0, 14)); // NOI18N
        txtcarinis.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        txtcarinis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcarinisActionPerformed(evt);
            }
        });
        pagetransaksi.add(txtcarinis);
        txtcarinis.setBounds(20, 100, 180, 30);

        btncarinissiswa.setBackground(new java.awt.Color(204, 204, 204));
        btncarinissiswa.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btncarinissiswa.setForeground(new java.awt.Color(51, 51, 51));
        btncarinissiswa.setText("Cari");
        btncarinissiswa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btncarinissiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncarinissiswaActionPerformed(evt);
            }
        });
        pagetransaksi.add(btncarinissiswa);
        btncarinissiswa.setBounds(200, 100, 60, 30);

        pesancari.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        pesancari.setForeground(new java.awt.Color(0, 102, 51));
        pesancari.setText("Data Siswa");
        pagetransaksi.add(pesancari);
        pesancari.setBounds(20, 140, 320, 20);

        dtpem.setFont(new java.awt.Font("Cambria", 1, 16)); // NOI18N
        dtpem.setForeground(new java.awt.Color(0, 102, 51));
        dtpem.setText("Data Pembayaran");
        pagetransaksi.add(dtpem);
        dtpem.setBounds(20, 250, 170, 20);

        tabelsiswabynis.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabelsiswabynis.setModel(new javax.swing.table.DefaultTableModel(
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
        scrtbsiswa.setViewportView(tabelsiswabynis);

        pagetransaksi.add(scrtbsiswa);
        scrtbsiswa.setBounds(20, 160, 970, 80);

        tabelpembbynis.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabelpembbynis.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelpembbynis.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelpembbynisMouseClicked(evt);
            }
        });
        scrtbpemb.setViewportView(tabelpembbynis);

        pagetransaksi.add(scrtbpemb);
        scrtbpemb.setBounds(20, 280, 970, 120);

        btnprint.setBackground(new java.awt.Color(204, 204, 204));
        btnprint.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnprint.setForeground(new java.awt.Color(51, 51, 51));
        btnprint.setText("Print");
        btnprint.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnprint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprintActionPerformed(evt);
            }
        });
        pagetransaksi.add(btnprint);
        btnprint.setBounds(900, 410, 80, 30);

        btnbayar.setBackground(new java.awt.Color(204, 204, 204));
        btnbayar.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnbayar.setForeground(new java.awt.Color(51, 51, 51));
        btnbayar.setText("Bayar");
        btnbayar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnbayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbayarActionPerformed(evt);
            }
        });
        pagetransaksi.add(btnbayar);
        btnbayar.setBounds(770, 410, 90, 30);

        jLayeredPane1.add(pagetransaksi);
        pagetransaksi.setBounds(0, 0, 1010, 460);

        pagelaporan.setBackground(new java.awt.Color(238, 238, 239));
        pagelaporan.setLayout(null);

        jLabel23.setFont(new java.awt.Font("Cambria", 1, 24)); // NOI18N
        jLabel23.setText("Laporan Pembayaran");
        pagelaporan.add(jLabel23);
        jLabel23.setBounds(390, 30, 240, 29);

        jLabel24.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 51));
        jLabel24.setText("sampai");
        pagelaporan.add(jLabel24);
        jLabel24.setBounds(260, 130, 60, 18);

        jLabel25.setFont(new java.awt.Font("Cambria", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 51));
        jLabel25.setText("Masukkan rentang waktu!");
        pagelaporan.add(jLabel25);
        jLabel25.setBounds(50, 90, 170, 18);

        btncetak.setBackground(new java.awt.Color(204, 204, 204));
        btncetak.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btncetak.setForeground(new java.awt.Color(51, 51, 51));
        btncetak.setText("Cetak");
        btncetak.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btncetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncetakActionPerformed(evt);
            }
        });
        pagelaporan.add(btncetak);
        btncetak.setBounds(640, 120, 70, 30);

        btnlihat.setBackground(new java.awt.Color(204, 204, 204));
        btnlihat.setFont(new java.awt.Font("Cambria", 1, 12)); // NOI18N
        btnlihat.setForeground(new java.awt.Color(51, 51, 51));
        btnlihat.setText("Lihat");
        btnlihat.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnlihat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlihatActionPerformed(evt);
            }
        });
        pagelaporan.add(btnlihat);
        btnlihat.setBounds(540, 120, 70, 30);

        tabellaporan.setFont(new java.awt.Font("Cambria", 0, 12)); // NOI18N
        tabellaporan.setModel(new javax.swing.table.DefaultTableModel(
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
        scrtblap.setViewportView(tabellaporan);

        pagelaporan.add(scrtblap);
        scrtblap.setBounds(50, 190, 910, 220);

        jLayeredPane1.add(pagelaporan);
        pagelaporan.setBounds(0, 0, 1010, 460);

        getContentPane().add(jLayeredPane1);
        jLayeredPane1.setBounds(180, 180, 1010, 480);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/project/spp/image/background.png"))); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(1240, 700));
        jLabel9.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jLabel9MouseDragged(evt);
            }
        });
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel9MousePressed(evt);
            }
        });
        getContentPane().add(jLabel9);
        jLabel9.setBounds(0, -40, 1240, 740);

        setSize(new java.awt.Dimension(1221, 700));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btndatasiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndatasiswaActionPerformed
        if(pengguna.equals("Admin")){
            pagesiswa.setVisible(true);
            pagepetugas.setVisible(false);
            pagespp.setVisible(false);
            pagetransaksi.setVisible(false);
            pagehome.setVisible(false);
            pagelaporan.setVisible(false);
            loadTabelSiswa();
            kosongsiswa();
            load_cmbkelas();
            load_cmbtahun(); 
            
        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Admin"); 
        }
        
    }//GEN-LAST:event_btndatasiswaActionPerformed

    private void btndatapetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndatapetugasActionPerformed
        // TODO add your handling code here:
        if(pengguna.equals("Admin")){   
            pagesiswa.setVisible(false);
            pagepetugas.setVisible(true);
            pagespp.setVisible(false);
            pagetransaksi.setVisible(false);
            pagehome.setVisible(false);
            pagelaporan.setVisible(false);
            loadTabelPetugas();
            kosongPetugas();
        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Admin"); 
        }
    }//GEN-LAST:event_btndatapetugasActionPerformed

    private void btndatasppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btndatasppActionPerformed
        // TODO add your handling code here:
        if(pengguna.equals("Admin")){
            pagesiswa.setVisible(false);
            pagepetugas.setVisible(false);
            pagespp.setVisible(true);
            pagetransaksi.setVisible(false);
            pagehome.setVisible(false);
            pagelaporan.setVisible(false);
            loadTabelSPP();
            kosongSPP();
        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Admin"); 
        }
    }//GEN-LAST:event_btndatasppActionPerformed

    private void btntransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntransaksiActionPerformed
        // TODO add your handling code here:
        if(pengguna.equals("Petugas")){    
            pagesiswa.setVisible(false);
            pagepetugas.setVisible(false);
            pagespp.setVisible(false);
            pagetransaksi.setVisible(true);
            pagehome.setVisible(false);
            pagelaporan.setVisible(false);

            pesancari.setVisible(false);
            dtpem.setVisible(false);
            scrtbsiswa.setVisible(false);
            scrtbpemb.setVisible(false);
            btnprint.setVisible(false);
            btnbayar.setVisible(false);
        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Petugas");
        }
        
    }//GEN-LAST:event_btntransaksiActionPerformed

    private void btnlaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlaporanActionPerformed
        // TODO add your handling code here:
        if(pengguna.equals("Admin")){
            pagesiswa.setVisible(false);
            pagepetugas.setVisible(false);
            pagespp.setVisible(false);
            pagetransaksi.setVisible(false);
            pagehome.setVisible(false);
            pagelaporan.setVisible(true);
            
            scrtblap.setVisible(false);

        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Admin"); 
        }
        
    }//GEN-LAST:event_btnlaporanActionPerformed

    private void btnsimpansiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpansiswaActionPerformed
        // TODO add your handling code here:
            
        admin.tambahDataSiswa(txtnis.getText(), txtnamasiswa.getText(), cmbjk.getSelectedItem().toString(), txtalamat.getText(), Integer.parseInt(cmbkelas.getSelectedItem().toString()), Integer.parseInt(cmbtahun.getSelectedItem().toString()));
        loadTabelSiswa();
        kosongsiswa();
        
    }//GEN-LAST:event_btnsimpansiswaActionPerformed

    private void btneditsiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditsiswaActionPerformed
        // TODO add your handling code here:
        admin.editDataSiswa(Integer.parseInt(IdSiswa), txtnis.getText(), txtnamasiswa.getText(), cmbjk.getSelectedItem().toString(), txtalamat.getText(), cmbkelas.getSelectedItem().toString(), cmbtahun.getSelectedItem().toString());
        loadTabelSiswa();
        kosongsiswa();
        load_cmbkelas();
        load_cmbtahun();
    }//GEN-LAST:event_btneditsiswaActionPerformed

    private void btncancelsiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelsiswaActionPerformed
        // TODO add your handling code here:
        kosongsiswa();
    }//GEN-LAST:event_btncancelsiswaActionPerformed

    private void tabelsiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelsiswaMouseClicked
        // TODO add your handling code here:
        // menampilkan data kedalam form pengisian:
        int baris = tabelsiswa.rowAtPoint(evt.getPoint());
        IdSiswa = tabelsiswa.getValueAt(baris, 1).toString();
        String niss = tabelsiswa.getValueAt(baris, 2).toString();
        txtnis.setText(niss);
        String nama = tabelsiswa.getValueAt(baris,3).toString();
        txtnamasiswa.setText(nama);
        String jk = tabelsiswa.getValueAt(baris, 4).toString();
        cmbjk.setSelectedItem(jk);
        String alamat =tabelsiswa.getValueAt(baris, 5).toString();
        txtalamat.setText(alamat);
        String kelas = tabelsiswa.getValueAt(baris, 6).toString();
        cmbkelas.setSelectedItem(kelas);
        String tahun = tabelsiswa.getValueAt(baris, 7).toString();
        cmbtahun.setSelectedItem(tahun);
    }//GEN-LAST:event_tabelsiswaMouseClicked

    private void btnhapussiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapussiswaActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this, 
                       "Yakin ingin menghapus data ini?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION, 
                       JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if(jawab == JOptionPane.YES_OPTION){
            admin.hapusDataSiswa(IdSiswa);
            loadTabelSiswa();
            kosongsiswa();
        }
        
        
    }//GEN-LAST:event_btnhapussiswaActionPerformed

    private void tabelpetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpetugasMouseClicked
        // TODO add your handling code here:
        int baris = tabelpetugas.rowAtPoint(evt.getPoint());
        idpetugas = tabelpetugas.getValueAt(baris,1).toString();
        String nama = tabelpetugas.getValueAt(baris,2).toString();
        txtnamapetugas.setText(nama);
        String level = tabelpetugas.getValueAt(baris, 3).toString();
        cmblevel.setSelectedItem(level);
        String usern = tabelpetugas.getValueAt(baris, 4).toString();
        txtuser.setText(usern);
        String pass = tabelpetugas.getValueAt(baris, 5).toString();
        txtpass.setText(pass);
        
    }//GEN-LAST:event_tabelpetugasMouseClicked

    private void btnsimpanpetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpanpetugasActionPerformed
        // TODO add your handling code here:
        admin.tambahDataPetugas(txtnamapetugas.getText(), cmblevel.getSelectedItem().toString(), txtuser.getText(), txtpass.getText());
        loadTabelPetugas();
        kosongPetugas();
    }//GEN-LAST:event_btnsimpanpetugasActionPerformed

    private void btneditpetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditpetugasActionPerformed
        // TODO add your handling code here:
        admin.editDataPetugas(idpetugas, txtnamapetugas.getText(), cmblevel.getSelectedItem().toString(), txtuser.getText(), txtpass.getText());
        loadTabelPetugas();
        kosongPetugas();
    }//GEN-LAST:event_btneditpetugasActionPerformed

    private void btncancelpetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelpetugasActionPerformed
        // TODO add your handling code here:
        kosongPetugas();
    }//GEN-LAST:event_btncancelpetugasActionPerformed

    private void btnhapuspetugasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapuspetugasActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this, 
                       "Yakin ingin menghapus data ini?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION, 
                       JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if(jawab == JOptionPane.YES_OPTION){
            admin.hapusDataPetugas(idpetugas);
            loadTabelPetugas();
            kosongPetugas();
        }
        
    }//GEN-LAST:event_btnhapuspetugasActionPerformed

    private void tabelsppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelsppMouseClicked
        // TODO add your handling code here:
        int baris = tabelspp.rowAtPoint(evt.getPoint());
        idspp = tabelspp.getValueAt(baris, 1).toString();
        String tahun = tabelspp.getValueAt(baris, 2).toString();
        txttahunspp.setText(tahun);
        String nominal = tabelspp.getValueAt(baris, 3).toString();
        txtnominal.setText(nominal);
    }//GEN-LAST:event_tabelsppMouseClicked

    private void btnsimpansppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsimpansppActionPerformed
        // TODO add your handling code here:
        admin.tambahDataSPP(txttahunspp.getText(), txtnominal.getText());
        loadTabelSPP();
        kosongSPP();
        load_cmbtahun();
        
    }//GEN-LAST:event_btnsimpansppActionPerformed

    private void btneditsppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditsppActionPerformed
        // TODO add your handling code here:
        admin.editDataSPP(idspp, txttahunspp.getText(), txtnominal.getText());
        loadTabelSPP();
        kosongSPP();
        load_cmbtahun();
    }//GEN-LAST:event_btneditsppActionPerformed

    private void btncancelsppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelsppActionPerformed
        // TODO add your handling code here:
        kosongSPP();
    }//GEN-LAST:event_btncancelsppActionPerformed

    private void btnhapussppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhapussppActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this, 
                       "Yakin ingin menghapus data ini?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION, 
                       JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if(jawab == JOptionPane.YES_OPTION){
            admin.hapusDataSPP(idspp);
            loadTabelSPP();
            kosongSPP();
        }

        
    }//GEN-LAST:event_btnhapussppActionPerformed

    private void txttahunsppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttahunsppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttahunsppActionPerformed

    private void txtcarinisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcarinisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcarinisActionPerformed

    private void btncarinissiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncarinissiswaActionPerformed
        // TODO add your handling code here:
        if(petugas.cekNis(txtcarinis.getText())){
            id_pemb = null;
            loadTabelSiswaByNIS(txtcarinis.getText());
            btnprint.setVisible(true);
            btnbayar.setVisible(true);
            pesancari.setVisible(true);
            dtpem.setVisible(true);
            scrtbsiswa.setVisible(true);
            scrtbpemb.setVisible(true);
            txtcarinis.setText(null);
        }else{
            JOptionPane.showMessageDialog(null, "NIS tidak terdaftar");
        }

    }//GEN-LAST:event_btncarinissiswaActionPerformed

    private void btnbayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbayarActionPerformed
        // TODO add your handling code here:
        Date ys = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String tgl = s.format(ys);  

        if(id_pemb == null){
            JOptionPane.showMessageDialog(null, "Klik pada salah satu baris pada tabel data pembayaran untuk melakukan transaksi");
        }else{
            if(petugas.cekPembayaran(id_pemb)){
                JOptionPane.showMessageDialog(null, "Pembayaran sudah dilakukan");
            }else{
                petugas.bayarSPP(tgl, id_pemb, iduser);
                id_pemb = null;
                loadTabelSiswaByNIS(nis);
            }
        }
    }//GEN-LAST:event_btnbayarActionPerformed

    private void tabelpembbynisMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelpembbynisMouseClicked
        // TODO add your handling code here:
        int baris = tabelpembbynis.rowAtPoint(evt.getPoint());
        id_pemb = tabelpembbynis.getValueAt(baris, 0).toString();
        nis = tabelpembbynis.getValueAt(baris, 1).toString();
        idpetugas = tabelpembbynis.getValueAt(baris, 8).toString();
    }//GEN-LAST:event_tabelpembbynisMouseClicked

    private void btnprintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprintActionPerformed
        // TODO add your handling code here:
        String nama = petugas.getNamaSiswa(nis);
        String pet = petugas.getNamaPetugas(idpetugas);
        int i = Integer.parseInt(id_pemb);
        try{
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("id", i);
            parameter.put("nama", nama);
            parameter.put("petugas", pet);
            Class.forName("com.mysql.jdbc.Driver");
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/projectspp", "root", "");
            File file = new File("src/project/spp/Report/report1.jasper");
            JasperReport jr = (JasperReport)JRLoader.loadObject(file);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, cn);
            JasperViewer.viewReport(jp, false);
            JasperViewer.setDefaultLookAndFeelDecorated(true);
        }catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(null, "Data tidak dapat dicetak!" + "\n" + e.getMessage(), "Cetak Data", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btnprintActionPerformed

    private void btnlogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlogoutActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this, 
                       "Yakin ingin logout?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION, 
                       JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if(jawab == JOptionPane.YES_OPTION){
            this.dispose();
            new formlogin().setVisible(true);
        }
        
    }//GEN-LAST:event_btnlogoutActionPerformed

    private void btnexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnexitActionPerformed
        // TODO add your handling code here:
        int jawab = JOptionPane.showOptionDialog(this, 
                       "Yakin ingin keluar?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION, 
                       JOptionPane.QUESTION_MESSAGE, null, null, null);
    
        if(jawab == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_btnexitActionPerformed

    private void btnlihatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlihatActionPerformed
        // TODO add your handling code here:
        String tampilan = "yyyy-MM-dd";
        SimpleDateFormat fm = new SimpleDateFormat(tampilan);
        String tgldari = String.valueOf(fm.format(daritgl.getDate()));
        String tglsampai = String.valueOf(fm.format(sampaitgl.getDate()));
        loadTabelLaporan(tgldari, tglsampai);
        
        scrtblap.setVisible(true);
    }//GEN-LAST:event_btnlihatActionPerformed

    private void btncetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncetakActionPerformed
        // TODO add your handling code here:

        try{
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("tgldari", daritgl.getDate());
        parameter.put("tglsampai", sampaitgl.getDate());
        Class.forName("com.mysql.jdbc.Driver");
        Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/projectspp", "root", "");
        File file = new File("src/project/spp/Report/report4.jasper");
        JasperReport jr = (JasperReport)JRLoader.loadObject(file);
        JasperPrint jp = JasperFillManager.fillReport(jr, parameter, cn);
        JasperViewer.viewReport(jp, false);
        JasperViewer.setDefaultLookAndFeelDecorated(true);
        }catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(null, "Data tidak dapat dicetak!!!" + "\n" + e.getMessage(), "Cetak Data", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btncetakActionPerformed

    private void btnthnajaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthnajaranActionPerformed
        // TODO add your handling code here:
        if(pengguna.equals("Admin")){
            JOptionPane.showMessageDialog(null, "Pastikan menu ini hanya digunakan saat akan menambahkan tahun ajaran \n satu tahun sekali");
            int jawab = JOptionPane.showConfirmDialog(this, 
                       "Fungsi ini akan berpengaruh besar pada perubahan data. Ingin melanjutkan?", 
                       "Konfirmasi",
                       JOptionPane.YES_NO_OPTION);
    
            if(jawab == JOptionPane.YES_OPTION){
                int MaxThnAjaran = admin.getMaxThnAjaran();
                int MaxThnSpp = admin.getMaxThnSPP();

                if(MaxThnAjaran < MaxThnSpp){
                    ResultSet rs = admin.tahunAjaranBaru();
                    try{
                        while(rs.next()){
                            int kls = Integer.parseInt(rs.getString("Kelas"));
                            String IdSPP = rs.getString("id_spp");
                            if(kls == 10 || kls == 11){
                                int kelas = kls + 1;
                                int tahun = admin.getTahunSPP(IdSPP);
                                int NewTahun = tahun + 1;
                                admin.tambahDataSiswaTAB(rs.getString("NIS"), rs.getString("Nama"), rs.getString("JK"), rs.getString("Alamat"), kelas, NewTahun);
                            }
                        }
                        JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                        loadTabelSiswa();
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Data SPP untuk tahun ajaran baru belum dibuat");
                }
            }

        }else{
            JOptionPane.showMessageDialog(this, "Halaman ini hanya dapat diakses oleh Admin"); 
        }
    }//GEN-LAST:event_btnthnajaranActionPerformed

    private void btnhomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnhomeActionPerformed
        // TODO add your handling code here:
        pagesiswa.setVisible(false);
        pagepetugas.setVisible(false);
        pagespp.setVisible(false);
        pagetransaksi.setVisible(false);
        pagelaporan.setVisible(false);
        pagehome.setVisible(true);
    }//GEN-LAST:event_btnhomeActionPerformed

    private void jLabel9MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MousePressed
        dragxmouse = evt.getX();
        dragymouse = evt.getY();
    }//GEN-LAST:event_jLabel9MousePressed

    private void jLabel9MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        
        this.setLocation(x - dragxmouse, y - dragymouse);
        System.out.println(x+","+y);
    }//GEN-LAST:event_jLabel9MouseDragged

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
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnbayar;
    private javax.swing.JButton btncancelpetugas;
    private javax.swing.JButton btncancelsiswa;
    private javax.swing.JButton btncancelspp;
    private javax.swing.JButton btncarinissiswa;
    private javax.swing.JButton btncetak;
    private javax.swing.JButton btndatapetugas;
    private javax.swing.JButton btndatasiswa;
    private javax.swing.JButton btndataspp;
    private javax.swing.JButton btneditpetugas;
    private javax.swing.JButton btneditsiswa;
    private javax.swing.JButton btneditspp;
    private javax.swing.JButton btnexit;
    private javax.swing.JButton btnhapuspetugas;
    private javax.swing.JButton btnhapussiswa;
    private javax.swing.JButton btnhapusspp;
    private javax.swing.JButton btnhome;
    private javax.swing.JButton btnlaporan;
    private javax.swing.JButton btnlihat;
    private javax.swing.JButton btnlogout;
    private javax.swing.JButton btnprint;
    private javax.swing.JButton btnsimpanpetugas;
    private javax.swing.JButton btnsimpansiswa;
    private javax.swing.JButton btnsimpanspp;
    private javax.swing.JButton btnthnajaran;
    private javax.swing.JButton btntransaksi;
    private javax.swing.JComboBox<String> cmbjk;
    private javax.swing.JComboBox<String> cmbkelas;
    private javax.swing.JComboBox<String> cmblevel;
    private javax.swing.JComboBox<String> cmbtahun;
    private javax.swing.JLabel dtpem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JLabel menu;
    private javax.swing.JLabel menu1;
    private javax.swing.JLabel menu2;
    private javax.swing.JPanel pagehome;
    private javax.swing.JPanel pagelaporan;
    private javax.swing.JPanel pagepetugas;
    private javax.swing.JPanel pagesiswa;
    private javax.swing.JPanel pagespp;
    private javax.swing.JPanel pagetransaksi;
    private javax.swing.JLabel pesancari;
    private javax.swing.JScrollPane scrtblap;
    private javax.swing.JScrollPane scrtbpemb;
    private javax.swing.JScrollPane scrtbsiswa;
    private javax.swing.JTable tabellaporan;
    private javax.swing.JTable tabelpembbynis;
    private javax.swing.JTable tabelpetugas;
    private javax.swing.JTable tabelsiswa;
    private javax.swing.JTable tabelsiswabynis;
    private javax.swing.JTable tabelspp;
    private javax.swing.JTextField txtalamat;
    private javax.swing.JTextField txtcarinis;
    private javax.swing.JTextField txtnamapetugas;
    private javax.swing.JTextField txtnamasiswa;
    private javax.swing.JTextField txtnis;
    private javax.swing.JTextField txtnominal;
    private javax.swing.JTextField txtpass;
    private javax.swing.JTextField txttahunspp;
    private javax.swing.JTextField txtuser;
    // End of variables declaration//GEN-END:variables
}
