package com.example.datn.DTO;

import java.math.BigDecimal;

public class LichDatSanDTO {
    private Integer idLichDatSan;
    private String tenSan;
    private BigDecimal giaApDung;
    private Integer trangThai;

    public Integer getIdLichDatSan() {
        return idLichDatSan;
    }

    public void setIdLichDatSan(Integer idLichDatSan) {
        this.idLichDatSan = idLichDatSan;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public BigDecimal getGiaApDung() {
        return giaApDung;
    }

    public void setGiaApDung(BigDecimal giaApDung) {
        this.giaApDung = giaApDung;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
