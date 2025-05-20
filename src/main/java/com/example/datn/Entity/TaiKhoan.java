package com.example.datn.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tai_khoan")
    private int id;
    @Column(name = "ho_ten")
    private String ho_ten;
    @Column(name = "email")
    private String email;
    @Column(name = "mat_khau")
    private String mat_khau;
    @Column(name = "so_dien_thoai")
    private String so_dien_thoai;
    @Column(name = "vai_tro")
    private String vai_tro;
    @Column(name = "trang_thai")
    private int trang_thai;

    @OneToMany(mappedBy = "taiKhoan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;

    public TaiKhoan() {
    }

    public TaiKhoan(int id, String ho_ten, String email, String mat_khau, String so_dien_thoai, String vai_tro, int trang_thai) {
        this.id = id;
        this.ho_ten = ho_ten;
        this.email = email;
        this.mat_khau = mat_khau;
        this.so_dien_thoai = so_dien_thoai;
        this.vai_tro = vai_tro;
        this.trang_thai = trang_thai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMat_khau() {
        return mat_khau;
    }

    public void setMat_khau(String mat_khau) {
        this.mat_khau = mat_khau;
    }

    public String getSo_dien_thoai() {
        return so_dien_thoai;
    }

    public void setSo_dien_thoai(String so_dien_thoai) {
        this.so_dien_thoai = so_dien_thoai;
    }

    public String getVai_tro() {
        return vai_tro;
    }

    public void setVai_tro(String vai_tro) {
        this.vai_tro = vai_tro;
    }

    public int getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(int trang_thai) {
        this.trang_thai = trang_thai;
    }

    public List<SanBong> getDsSanBong() {
        return dsSanBong;
    }

    public void setDsSanBong(List<SanBong> dsSanBong) {
        this.dsSanBong = dsSanBong;
    }
}
