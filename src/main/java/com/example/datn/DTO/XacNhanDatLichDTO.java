package com.example.datn.DTO;

import java.util.List;

public class XacNhanDatLichDTO {
    private String hoTen;
    private String soDienThoai;
    private String email;


    private List<ChiTietDatLichDTO> chiTietDatLichList;

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ChiTietDatLichDTO> getChiTietDatLichList() {
        return chiTietDatLichList;
    }

    public void setChiTietDatLichList(List<ChiTietDatLichDTO> chiTietDatLichList) {
        this.chiTietDatLichList = chiTietDatLichList;
    }
}
