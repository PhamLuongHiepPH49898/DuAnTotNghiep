package com.example.datn.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "loai_san")
public class LoaiSan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_loai_san")
    private int id;
    @Column(name = "ten_loai_san", columnDefinition = "NVARCHAR(255)")
    private String ten_loai_san;
    @Column(name = "trang_thai")
    private int trang_thai;

    @OneToMany(mappedBy = "loaiSan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;

    public LoaiSan() {
    }

    public LoaiSan(int id, String ten_loai_san, int trang_thai) {
        this.id = id;
        this.ten_loai_san = ten_loai_san;
        this.trang_thai = trang_thai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen_loai_san() {
        return ten_loai_san;
    }

    public void setTen_loai_san(String ten_loai_san) {
        this.ten_loai_san = ten_loai_san;
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
