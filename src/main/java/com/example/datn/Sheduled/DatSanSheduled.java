package com.example.datn.Sheduled;

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
public class DatSanSheduled {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepo;

    // @Scheduled(cron = "*/30 * * * * ?") // test
    @Scheduled(cron = "0 0 0 * * ?") // chạy lúc 00:00 mỗi ngày

    public void taoLichDatSanTruoc() {
        LocalDate ngay = LocalDate.now();

        List<GiaTheoKhungGio> danhSachGiaTheoKhungGio = giaTheoKhungGioRepo
                .findGiaTheoKhungGioByTrangThaiAndSanHoatDong(List.of(0));

        for (int i = 0; i < 30; i++) {
            LocalDate targetDate = ngay.plusDays(i);
            for (GiaTheoKhungGio gia : danhSachGiaTheoKhungGio) {
                List<LichDatSan> lichs = lichDatSanRepo.findByNgaySanKhungGio(targetDate, gia.getIdGiaTheoKhungGio());//

                if (lichs.isEmpty()) {
                    LichDatSan lichMoi = new LichDatSan();
                    lichMoi.setNgayDat(targetDate);
                    lichMoi.setGiaTheoKhungGio(gia);
                    lichMoi.setTrangThai(3); // lịch trống
                    lichMoi.setGhiChu("Tạo tự động");
                    lichMoi.setGiaApDung(null);
                    lichMoi.setNgayTao(LocalDate.now());
                    lichDatSanRepo.save(lichMoi);
                    System.out.println(" Đã tạo lịch ngày " + targetDate + " - ID Giá: " + gia.getIdGiaTheoKhungGio());
                } else {
                    System.out.println(" Đã có lịch cho ngày " + targetDate + " - ID Giá " + gia.getIdGiaTheoKhungGio()
                            + " → Bỏ qua tạo mới.");
                }
            }
        }
    }

    public void taoLichChoGia(GiaTheoKhungGio gia) {
        LocalDate ngay = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate targetDate = ngay.plusDays(i);
            if (lichDatSanRepo.findByNgaySanKhungGio(targetDate, gia.getIdGiaTheoKhungGio()).isEmpty()) {
                LichDatSan lichMoi = new LichDatSan();
                lichMoi.setNgayDat(targetDate);
                lichMoi.setGiaTheoKhungGio(gia);
                lichMoi.setTrangThai(3);
                lichMoi.setGhiChu("Tạo tự động");
                lichMoi.setGiaApDung(null);
                lichMoi.setNgayTao(LocalDate.now());
                lichDatSanRepo.save(lichMoi);
            }
        }
    }

}