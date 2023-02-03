/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.spp;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author user
 */
@Entity
@Table(name = "petugas", catalog = "projectspp", schema = "")
@NamedQueries({
    @NamedQuery(name = "Petugas.findAll", query = "SELECT p FROM Petugas p")
    , @NamedQuery(name = "Petugas.findByIdPetugas", query = "SELECT p FROM Petugas p WHERE p.idPetugas = :idPetugas")
    , @NamedQuery(name = "Petugas.findByNama", query = "SELECT p FROM Petugas p WHERE p.nama = :nama")
    , @NamedQuery(name = "Petugas.findByLevel", query = "SELECT p FROM Petugas p WHERE p.level = :level")
    , @NamedQuery(name = "Petugas.findByUsername", query = "SELECT p FROM Petugas p WHERE p.username = :username")
    , @NamedQuery(name = "Petugas.findByPassword", query = "SELECT p FROM Petugas p WHERE p.password = :password")})
public class Petugas implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_petugas")
    private Integer idPetugas;
    @Basic(optional = false)
    @Column(name = "Nama")
    private String nama;
    @Basic(optional = false)
    @Column(name = "Level")
    private String level;
    @Basic(optional = false)
    @Column(name = "Username")
    private String username;
    @Basic(optional = false)
    @Column(name = "Password")
    private String password;

    public Petugas() {
    }

    public Petugas(Integer idPetugas) {
        this.idPetugas = idPetugas;
    }

    public Petugas(Integer idPetugas, String nama, String level, String username, String password) {
        this.idPetugas = idPetugas;
        this.nama = nama;
        this.level = level;
        this.username = username;
        this.password = password;
    }

    public Integer getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(Integer idPetugas) {
        Integer oldIdPetugas = this.idPetugas;
        this.idPetugas = idPetugas;
        changeSupport.firePropertyChange("idPetugas", oldIdPetugas, idPetugas);
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        String oldNama = this.nama;
        this.nama = nama;
        changeSupport.firePropertyChange("nama", oldNama, nama);
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        String oldLevel = this.level;
        this.level = level;
        changeSupport.firePropertyChange("level", oldLevel, level);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        String oldUsername = this.username;
        this.username = username;
        changeSupport.firePropertyChange("username", oldUsername, username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        String oldPassword = this.password;
        this.password = password;
        changeSupport.firePropertyChange("password", oldPassword, password);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPetugas != null ? idPetugas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Petugas)) {
            return false;
        }
        Petugas other = (Petugas) object;
        if ((this.idPetugas == null && other.idPetugas != null) || (this.idPetugas != null && !this.idPetugas.equals(other.idPetugas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "project.spp.Petugas[ idPetugas=" + idPetugas + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
