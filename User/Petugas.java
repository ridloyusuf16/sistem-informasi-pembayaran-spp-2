/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp.User;

import java.sql.Connection;
import javax.swing.JOptionPane;
import project.spp.Koneksi.db_con;

/**
 *
 * @author user
 */
public class Petugas {
    
    public void bayarSPP(String tgl, String id_pemb, String id_pet){
       try{
            String sql;
            sql = "UPDATE pembayaran SET tgl_dibayar = '"+tgl+"', keterangan = 'Lunas', id_petugas = '"+id_pet+"' WHERE id_pembayaran = '"+id_pemb+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Transaksi pembayaran berhasil dilakukan");
          
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
//    public void batalBayar(String id_pemb){
//        try{
//            String sql;
//            sql = "UPDATE pembayaran SET tgl_dibayar = '', keterangan = '', id_petugas = '' WHERE id_pembayaran = '"+id_pemb+"'";
//            java.sql.Connection conn=(Connection)db_con.configDB();
//            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
//            pst.execute();
//            JOptionPane.showMessageDialog(null, "Transaksi pembayaran dibatalkan");
//          
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, e.getMessage());
//        }
//    }
    
    public String getNamaSiswa(String nis){
        String NisSiswa = null;
        
        try{
            String sql = "SELECT * FROM siswa WHERE NIS = '"+nis+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                NisSiswa = res.getString("Nama");
            }else{
                System.out.println("Tidak Ada");
            }
            
        }catch(Exception e){
            
        }
        return NisSiswa;
    }
    
    public boolean cekPembayaran(String id_pemb){
        boolean status = false;
        
        try{
            String sql = "SELECT * FROM pembayaran WHERE id_pembayaran = '"+id_pemb+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
               if(res.getString("keterangan").equals("Lunas")){
                  status = true; 
                  System.out.println(res.getString("keterangan"));
               }else{
                   status = false;
               }
            }else{
                System.out.println("Tidak Ada");
            }
            
        }catch(Exception e){
            
        }
        return status;
    }
    
    public String getNamaPetugas(String id_pet){
        String nama = null;
        try {
            String sql = "SELECT * FROM petugas WHERE id_petugas = '"+id_pet+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                nama = res.getString("Nama");
            }else{
                
            }
            
        } catch (Exception e) {
        }
        
        return nama;
    }
    
    public boolean cekNis(String nis){
        boolean cek = false;
        try{
            String sql = "SELECT * FROM siswa WHERE NIS = '"+nis+"'";
            java.sql.Connection conn=(Connection)db_con.configDB();
            java.sql.Statement stm=conn.createStatement();
            java.sql.ResultSet res=stm.executeQuery(sql);
            if(res.next()){
                cek = true;
            }else{
                cek = false;
            }
            
        }catch(Exception e){
            
        }
        return cek;
    }
}

