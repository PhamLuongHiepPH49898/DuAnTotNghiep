package com.example.datn.Service;
import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@Service
public class SheduledDatSan {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepo;

    @Scheduled(cron = "*/50 * * * * ?") // chạy mỗi 30 giây // chạy 1h sáng mỗi ngày
    public void taoLichDatSanTruoc() {
        LocalDate ngay = LocalDate.now();

        List<GiaTheoKhungGio> danhSachGiaTheoKhungGio = giaTheoKhungGioRepo.findAll();

        for (int i = 0; i < 7; i++) {
            LocalDate targetDate = ngay.plusDays(i);
            for (GiaTheoKhungGio gia : danhSachGiaTheoKhungGio) {
                if (lichDatSanRepo.findByNgaySanKhungGio(targetDate, gia.getIdGiaTheoKhungGio()) == null) {
                    LichDatSan lichMoi = new LichDatSan();
                    lichMoi.setNgayDat(targetDate);
                    lichMoi.setGiaTheoKhungGio(gia);
                    lichMoi.setTrangThai(3); // lịch trống
                    lichMoi.setGhiChu("Tạo tự động");
                    lichMoi.setGiaApDung(gia.getGiaThue());
                    lichMoi.setNgayTao(LocalDate.now());
                    lichDatSanRepo.save(lichMoi);
                }
            }
        }
    }
}
