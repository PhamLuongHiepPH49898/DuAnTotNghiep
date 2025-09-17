package com.example.datn.DTO;

public interface DoanhThuSanProjection {
    Integer getIdSan();

    String getTenSan();

    Double getTongDoanhThu();  // <-- Double

    Long getSoLuotDat();
}

