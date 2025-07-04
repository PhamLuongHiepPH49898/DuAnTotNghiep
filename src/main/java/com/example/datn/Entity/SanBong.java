package com.example.datn.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "san_bong")
public class SanBong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_san_bong")
    private int id_san_bong;
    @Column(name = "ten_san_bong")
    private String ten_san_bong;
    @Column(name = "dia_chi")
    private String dia_chi;
    @Column(name = "mo_ta")
    private String mo_ta;
    @Column(name = "hinh_anh")
    private String hinh_anh;
    @Column(name = "trang_thai")
    private int trang_thai;
    @ManyToOne
    @JoinColumn(name = "id_loai_san")
    private LoaiSan loaiSan;

    @ManyToOne
    @JoinColumn(name = "id_loai_mat_san")
    private LoaiMatSan loaiMatSan;

    @ManyToOne
    @JoinColumn(name = "id_loai_mon_the_thao")
    private LoaiMonTheThao loaiMonTheThao;

    @ManyToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

    public SanBong() {
    }

    public SanBong(int id_san_bong, String ten_san_bong, String dia_chi, Double gia, String mo_ta, String hinh_anh, int trang_thai, LoaiSan loaiSan, LoaiMatSan loaiMatSan, LoaiMonTheThao loaiMonTheThao, TaiKhoan taiKhoan) {
        this.id_san_bong = id_san_bong;
        this.ten_san_bong = ten_san_bong;
        this.dia_chi = dia_chi;
        this.mo_ta = mo_ta;
        this.hinh_anh = hinh_anh;
        this.trang_thai = trang_thai;
        this.loaiSan = loaiSan;
        this.loaiMatSan = loaiMatSan;
        this.loaiMonTheThao = loaiMonTheThao;
        this.taiKhoan = taiKhoan;
    }

    public int getId_san_bong() {
        return id_san_bong;
    }

    public void setId_san_bong(int id_san_bong) {
        this.id_san_bong = id_san_bong;
    }

    public String getTen_san_bong() {
        return ten_san_bong;
    }

    public void setTen_san_bong(String ten_san_bong) {
        this.ten_san_bong = ten_san_bong;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }


    public String getMo_ta() {
        return mo_ta;
    }

    public void setMo_ta(String mo_ta) {
        this.mo_ta = mo_ta;
    }

    public String getHinh_anh() {
        return hinh_anh;
    }

    public void setHinh_anh(String hinh_anh) {
        this.hinh_anh = hinh_anh;
    }

    public int getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(int trang_thai) {
        this.trang_thai = trang_thai;
    }

    public LoaiSan getLoaiSan() {
        return loaiSan;
    }

    public void setLoaiSan(LoaiSan loaiSan) {
        this.loaiSan = loaiSan;
    }

    public LoaiMatSan getLoaiMatSan() {
        return loaiMatSan;
    }

    public void setLoaiMatSan(LoaiMatSan loaiMatSan) {
        this.loaiMatSan = loaiMatSan;
    }

    public LoaiMonTheThao getLoaiMonTheThao() {
        return loaiMonTheThao;
    }

    public void setLoaiMonTheThao(LoaiMonTheThao loaiMonTheThao) {
        this.loaiMonTheThao = loaiMonTheThao;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    @Override
    public String toString() {
        return "SanBong{" +
                "id_san_bong=" + id_san_bong +
                ", ten_san_bong='" + ten_san_bong + '\'' +
                ", dia_chi='" + dia_chi + '\'' +
                ", mo_ta='" + mo_ta + '\'' +
                ", hinh_anh='" + hinh_anh + '\'' +
                ", trang_thai=" + trang_thai +
                ", loaiSan=" + loaiSan +
                ", loaiMatSan=" + loaiMatSan +
                ", loaiMonTheThao=" + (loaiMonTheThao != null ? loaiMonTheThao.getTen_mon_the_thao() : null) +
                ", taiKhoan=" + taiKhoan +
                '}';
    }
}
