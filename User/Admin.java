/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp.User;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import project.spp.Koneksi.db_con;

/**
 *
 * @author user
 */
public class Admin {
    
    String bulan[] = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    
    public void tambahDataSiswa(String nis, String nama, String jk, String alamat, int kelas, int tahun){
        int a = Integer.parseInt(nis);
        try{
            
            int id_spp = getIdSPP(tahun);
            String sql;
            sql = "INSERT INTO siswa VALUES ('0', '"+ a +"', '"+ nama +"', '"+ jk +"', '"+ alamat +"', '"+ kelas +"', '"+ id_spp +"')";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            
            int id_siswa = getIdSiswa(a, kelas);
            
            for(int i=0; i<12; i++){
                tambahDataPembayaran(a, bulan[i], id_spp, id_siswa);
            }
                JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Gagal");
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
       public void tambahDataSiswaTAB(String nis, String nama, String jk, String alamat, int kelas, int tahun){
        int a = Integer.parseInt(nis);
        try{
            
            int id_spp = getIdSPP(tahun);
            String sql;
            sql = "INSERT INTO siswa VALUES ('0', '"+ a +"', '"+ nama +"', '"+ jk +"', '"+ alamat +"', '"+ kelas +"', '"+ id_spp +"')";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            
            int id_siswa = getIdSiswa(a, kelas);
            
            for(int i=0; i<12; i++){
                tambahDataPembayaran(a, bulan[i], id_spp, id_siswa);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Gagal");
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void editDataSiswa(int IdSiswa, String nis, String nama, String jk, String alamat, String kelas, String tahun){
        int a = Integer.parseInt(nis);
        int b = Integer.parseInt(kelas);
        int c = Integer.parseInt(tahun);
        
        int id_spp = getIdSPP(c);
        try {
            String sql ="UPDATE siswa SET NIS = '"+ a +"', Nama = '"+ nama +"', JK = '"+ jk +"', Alamat= '"+ alamat +"', Kelas = '"+ b +"', id_spp = '"+id_spp+"' WHERE id_siswa = '"+ IdSiswa +"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil di edit");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Perubahan Data Gagal"+e.getMessage());
        }
    }
    
    public void hapusDataSiswa(String IdSiswa){
        int a = Integer.parseInt(IdSiswa);
        
        try {
            String sql ="DELETE FROM siswa WHERE id_siswa='"+ a +"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            hapusDataPembayaran(a);
            JOptionPane.showMessageDialog(null, "Berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public int getIdSiswa(int nis, int kelas){
        int IdSiswa = 0;
        try {

            String sql = "SELECT * FROM siswa WHERE NIS = '"+nis+"' AND Kelas = '"+kelas+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                IdSiswa = Integer.parseInt(res.getString("id_siswa"));
            }else{
                JOptionPane.showMessageDialog(null, "Fungsi gagal");
            }


        } catch (Exception e) {
        }
        return IdSiswa;
    }
    
    public void tambahDataPembayaran(int nis, String bulan, int id_spp, int id_siswa){
        try{
            String sql;
            sql = "INSERT INTO pembayaran (nis, bulan_dibayar, id_spp, id_siswa) VALUES ('"+nis+"', '"+bulan+"', '"+id_spp+"', '"+id_siswa+"')";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Pembayaran Gagal Ditambah");
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void hapusDataPembayaran(int IdSiswa){
        
        try {
            String sql ="DELETE FROM pembayaran WHERE id_siswa='"+ IdSiswa +"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }    
    }
    
    public void tambahDataPetugas(String nama, String level, String user, String pass){
        try{
            String sql = "INSERT INTO petugas VALUES ('0', '"+ nama +"','"+ level +"', '"+ user +"', '"+ pass +"')";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage()); 
        }
    }
    
    public void editDataPetugas(String id, String nama, String level, String user, String pass){
        
        int a = Integer.parseInt(id);
        
        try {
            String sql = "UPDATE petugas SET Nama = '"+ nama +"', Level = '"+ level +"', Username = '"+ user +"', Password = '"+ pass +"' WHERE id_petugas = '"+ a +"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil di edit");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Perubahan Data Gagal. "+e.getMessage());
        }   
    }
    
    public void hapusDataPetugas(String id){
        try {
            String sql ="DELETE FROM petugas WHERE id_petugas='"+id+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void tambahDataSPP(String tahun, String nominal){
        int a = Integer.parseInt(tahun);
        int b = Integer.parseInt(nominal);

         try{
            String sql = "INSERT INTO spp VALUES ('0', '"+a+"', '"+b+"')";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public void editDataSPP(String id,  String tahun, String nominal){
        int a = Integer.parseInt(id);
        int b = Integer.parseInt(nominal);
        int c = Integer.parseInt(tahun);
        try {
            String sql ="UPDATE spp SET Tahun = '"+c+"', Nominal = '"+b+"' WHERE id_spp = '"+a+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil di edit");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Perubahan Data Gagal"+e.getMessage());
        }
    }
    
    public void hapusDataSPP(String id){
        try {
            String sql ="DELETE FROM spp WHERE id_spp ='"+id+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Berhasil di hapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    public int getIdSPP(int t){
        int id = 0;
        try {

            String sql = "SELECT * FROM spp WHERE Tahun = '"+t+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                id = Integer.parseInt(res.getString("id_spp"));
            }else{
                JOptionPane.showMessageDialog(null, "Data SPP untuk tahun " +t+ " belum dibuat!\nSilahkan akses menu Data SPP untuk menambahkan data");
            }


        } catch (Exception e) {
        }
        
        return id;
    }
    
    public int getTahunSPP(String id){
        int tahun = 0;
        try {

            String sql = "SELECT * FROM spp WHERE id_spp = '"+id+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                tahun = Integer.parseInt(res.getString("Tahun"));
            }else{
                JOptionPane.showMessageDialog(null, "Data SPP untuk ID " +id+ " belum dibuat!\nSilahkan akses menu Data SPP untuk menambahkan data");
            }


        } catch (Exception e) {
        }
        
        return tahun;
    }
    
    public int getMaxThnAjaran(){
        int MaxTahunAjaran = 0;
        try {

            String sql = "SELECT MAX(spp.tahun) AS MaxTahun FROM siswa AS s INNER JOIN spp AS spp WHERE s.id_spp = spp.id_spp";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                MaxTahunAjaran = Integer.parseInt(res.getString("MaxTahun"));
            }else{
                JOptionPane.showMessageDialog(null, "Fungsi gagal");
            }


        } catch (Exception e) {
        }
        
        return MaxTahunAjaran;
    }
    
    public int getMaxThnSPP(){
        int MaxTahunSPP = 0;
        try {

            String sql = "SELECT MAX(tahun) AS MaxTahun FROM spp";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                MaxTahunSPP = Integer.parseInt(res.getString("MaxTahun"));
            }else{
                JOptionPane.showMessageDialog(null, "Fungsi gagal");
            }


        } catch (Exception e) {
        }
        
        return MaxTahunSPP;
    }
    
    public ResultSet tahunAjaranBaru(){
        ResultSet rs = null;
    
        try {
            
            String sql = "SELECT NIS, Nama, JK, Alamat, MAX(kelas) AS kelas, id_spp FROM siswa GROUP BY NIS";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            rs = res;
        } catch (Exception e) {
        }
        
        return rs;
    }
}
