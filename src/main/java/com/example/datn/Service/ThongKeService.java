package com.example.datn.Service;

import com.example.datn.DTO.DoanhThuSanProjection;
import com.example.datn.Repository.ThongKeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

// ThongKeService.java
@Service
@RequiredArgsConstructor
public class ThongKeService {

    private final ThongKeRepository repo;

    public record KetQua(List<DoanhThuSanProjection> list, Double tongTatCa, int month, int year) {}

    public KetQua thongKeTheoThang(Integer month, Integer year) {
        var now = java.time.LocalDate.now(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
        int m = (month == null || month < 1 || month > 12) ? now.getMonthValue() : month;
        int y = (year == null) ? now.getYear() : year;

        var list = repo.thongKeDoanhThuTheoSan(m, y);

        double total = list.stream()
                .map(DoanhThuSanProjection::getTongDoanhThu)
                .filter(java.util.Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        return new KetQua(list, total, m, y);
    }
}
