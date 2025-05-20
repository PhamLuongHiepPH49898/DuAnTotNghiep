package com.example.datn.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "loai_mon_the_thao")
public class LoaiMonTheThao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mon_the_thao")
    private int id;
    @Column(name = "ten_mon_the_thao")
    private String ten_mon_the_thao;
    @Column(name = "trang_thai")
    private int trang_thai;

    @OneToMany(mappedBy = "loaiMonTheThao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SanBong> dsSanBong;

    public LoaiMonTheThao() {
    }

    public LoaiMonTheThao(int id, String ten_mon_the_thao, int trang_thai) {
        this.id = id;
        this.ten_mon_the_thao = ten_mon_the_thao;
        this.trang_thai = trang_thai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen_mon_the_thao() {
        return ten_mon_the_thao;
    }

    public void setTen_mon_the_thao(String ten_mon_the_thao) {
        this.ten_mon_the_thao = ten_mon_the_thao;
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


    public void add(SanBong sanBong) {
        this.dsSanBong.add(sanBong);
        sanBong.setLoaiMonTheThao(this); // Thiết lập quan hệ hai chiều
    }

    public void delete(SanBong sanBong) {
        this.dsSanBong.remove(sanBong);
        sanBong.setLoaiMonTheThao(null); // Xoá liên kết nếu cần
    }
}
