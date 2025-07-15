package com.example.datn.Scheduler;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.KhungGio;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.ThongBaoRepo;
import com.example.datn.Service.ThongBaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NhacLichScheduler {

    private final LichDatSanRepo lichDatSanRepo;
    private final ThongBaoRepo thongBaoRepo;
    private final ThongBaoService thongBaoService;
    // @Scheduled(cron = "0 0 0 * * ?") // chạy lúc 00:00 mỗi ngày
    @Scheduled(fixedRate = 60000) // Mỗi phút để test
    public void guiThongBaoTruocMotNgay() {
        LocalDate ngayMai = LocalDate.now().plusDays(1);

        // Chỉ lấy lịch đã được duyệt và có người đặt
        List<LichDatSan> lichDatSanList = lichDatSanRepo.findByNgayDatAndTaiKhoanIsNotNullAndTrangThai(ngayMai, 1);

        for (LichDatSan lich : lichDatSanList) {
            boolean daGui = thongBaoRepo.existsByLichDatSan_IdAndTieuDe(
                    lich.getId(),
                    "Bạn có lịch đá sân vào ngày mai"
            );

            if (!daGui) {
                KhungGio khungGio = lich.getGiaTheoKhungGio().getKhungGio();
                thongBaoService.taoThongBaoNhacLich(lich, khungGio);
                System.out.println("[DEBUG] Đã gửi nhắc lịch cho lịch ID: " + lich.getId());
            }
        }
    }
    @Scheduled(fixedRate = 60000) // chạy mỗi phút để test
    public void guiThongBaoTruocHaiGio() {
        LocalDate today = LocalDate.now();
        List<LichDatSan> lichDatSans = lichDatSanRepo.findByNgayDatAndTaiKhoanIsNotNullAndTrangThai(today, 1);

        LocalDateTime now = LocalDateTime.now();

        for (LichDatSan lich : lichDatSans) {
            KhungGio khungGio = lich.getGiaTheoKhungGio().getKhungGio();

            // Tính giờ bắt đầu của trận đấu
            LocalDateTime thoiGianBatDau = lich.getNgayDat().atTime(khungGio.getGioBatDau());

            // Nếu còn đúng khoảng 2h (120 phút) và chưa gửi thông báo
            long minutesDiff = java.time.Duration.between(now, thoiGianBatDau).toMinutes();

            if (minutesDiff <= 120 && minutesDiff > 110) { //
                boolean daGui = thongBaoRepo.existsByLichDatSan_IdAndTieuDe(
                        lich.getId(),
                        "Nhắc lịch đá sân - còn 2 giờ"
                );

                if (!daGui) {
                    thongBaoService.taoThongBaoTruoc2H(lich, khungGio);
                    System.out.println("[DEBUG] Đã gửi thông báo trước 2h cho lịch ID: " + lich.getId());
                }
            }
        }
    }


}

