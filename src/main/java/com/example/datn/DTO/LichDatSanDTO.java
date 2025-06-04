package com.example.datn.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class LichDatSanDTO {
    private Integer id_san_bong;
    private Integer idKhungGio;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayDat;
    private String tenNguoiDat;
    private String email;
    private String soDienThoai;

    public Integer getId_san_bong() {
        return id_san_bong;
    }

    public void setId_san_bong(Integer id_san_bong) {
        this.id_san_bong = id_san_bong;
    }

    public Integer getIdKhungGio() {
        return idKhungGio;
    }

    public void setIdKhungGio(Integer idKhungGio) {
        this.idKhungGio = idKhungGio;
    }

    public LocalDate getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDate ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTenNguoiDat() {
        return tenNguoiDat;
    }

    public void setTenNguoiDat(String tenNguoiDat) {
        this.tenNguoiDat = tenNguoiDat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
