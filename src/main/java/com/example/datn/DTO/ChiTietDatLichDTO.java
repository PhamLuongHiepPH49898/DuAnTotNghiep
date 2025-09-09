package com.example.datn.DTO;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ChiTietDatLichDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayDat;
    private String tenSan;
    private String thoiGian;
    private Double gia;
    private Integer idGiaTheoKhungGio;

    public Integer getIdGiaTheoKhungGio() {
        return idGiaTheoKhungGio;
    }

    public void setIdGiaTheoKhungGio(Integer idGiaTheoKhungGio) {
        this.idGiaTheoKhungGio = idGiaTheoKhungGio;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }
}
