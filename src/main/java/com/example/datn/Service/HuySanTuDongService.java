package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HuySanTuDongService {

    private final LichDatSanRepo lichDatSanRepo;

    @Scheduled(fixedRate = 60000) // Mỗi 60s kiểm tra
    public void huySanChuaThanhToanQua5Phut() {
        LocalDateTime qua5Phut = LocalDateTime.now().minusMinutes(5);
        List<LichDatSan> danhSach = lichDatSanRepo.findByDaThanhToanFalseAndTrangThai(0);

        for (LichDatSan lich : danhSach) {
            if (lich.getNgayTao().isBefore(qua5Phut)) {
                lich.setTrangThai(3); // Hủy sân
                lichDatSanRepo.save(lich);
                System.out.println("❌ Hủy sân ID: " + lich.getId());
            }
        }
    }
}