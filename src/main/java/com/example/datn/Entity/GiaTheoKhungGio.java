package com.example.datn.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "gia_theo_khung_gio")
public class GiaTheoKhungGio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gia_theo_khung_gio")
    private int idGiaTheoKhungGio;

    @Column(name = "gia_thue", nullable = false)
    private BigDecimal giaThue;

    @Column(name = "trang_thai", nullable = false)
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "id_san_bong")
    private SanBong sanBong;

    @ManyToOne
    @JoinColumn(name = "id_khung_gio")
    private KhungGio khungGio;

    public int getIdGiaTheoKhungGio() {
        return idGiaTheoKhungGio;
    }

    public void setIdGiaTheoKhungGio(int idGiaTheoKhungGio) {
        this.idGiaTheoKhungGio = idGiaTheoKhungGio;
    }

    public BigDecimal getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(BigDecimal giaThue) {
        this.giaThue = giaThue;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public SanBong getSanBong() {
        return sanBong;
    }

    public void setSanBong(SanBong sanBong) {
        this.sanBong = sanBong;
    }

    public KhungGio getKhungGio() {
        return khungGio;
    }

    public void setKhungGio(KhungGio khungGio) {
        this.khungGio = khungGio;
    }
}


