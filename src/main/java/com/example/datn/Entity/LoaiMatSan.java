package com.example.datn.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "loai_san_bong")
public class LoaiMatSan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_mat_san")
    private int id;
    @Column(name = "ten_loai_mat_san")
    private String ten_loai_mat_san;
    @Column(name = "trang_thai")
    private int trang_thai;

    @OneToMany(mappedBy = "loaiMatSan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;
    public LoaiMatSan() {}

    public LoaiMatSan(int id, String ten_loai_mat_san, int trang_thai) {
        this.id = id;
        this.ten_loai_mat_san = ten_loai_mat_san;
        this.trang_thai = trang_thai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen_loai_mat_san() {
        return ten_loai_mat_san;
    }

    public void setTen_loai_mat_san(String ten_loai_mat_san) {
        this.ten_loai_mat_san = ten_loai_mat_san;
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
