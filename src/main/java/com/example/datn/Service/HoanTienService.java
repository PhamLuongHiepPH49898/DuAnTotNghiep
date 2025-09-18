package com.example.datn.Service;

import com.example.datn.Entity.HoanTien;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.HoanTienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class HoanTienService {
    @Autowired
    private HoanTienRepo hoanTienRepo;

    public Page<HoanTien> getAllHoanTien(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoanTienRepo.findAll(pageable);
    }

    public void duyet(int id) {
        HoanTien hoanTien = hoanTienRepo.findById(id).orElse(null);
        if (hoanTien != null) {
            hoanTien.setTrangThai(1);
            hoanTien.setNgayCapNhat(LocalDateTime.now());
            hoanTienRepo.save(hoanTien);
        }
    }

    public void tuChoi(int id) {
        HoanTien hoanTien = hoanTienRepo.findById(id).orElse(null);
        if (hoanTien != null) {
            hoanTien.setTrangThai(2);
            hoanTien.setNgayCapNhat(LocalDateTime.now());
            hoanTienRepo.save(hoanTien);
        }
    }

    public Page<HoanTien> timKiem(String tenNguoiDat, String soDienThoai, Integer sanBongId, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoanTienRepo.timKiem(tenNguoiDat, soDienThoai, sanBongId, trangThai, pageable);
    }

    public HoanTien taoHoanTien(LichDatSan lich, String lyDoHuy) {
        BigDecimal soTienThanhToan = BigDecimal.valueOf(lich.getGiaApDung());
        BigDecimal phanTram = tinhPhanTramHoanTien(lich);
        BigDecimal soTienHoan = soTienThanhToan.multiply(phanTram)
                .setScale(0, RoundingMode.HALF_UP); // làm tròn tiền

        HoanTien hoanTien = new HoanTien();
        hoanTien.setLichDatSan(lich);
        hoanTien.setSoTienDaThanhToan(soTienThanhToan);
        hoanTien.setPhanTramHoan(phanTram);
        hoanTien.setSoTienHoan(soTienHoan);
        hoanTien.setNgayHuy(LocalDateTime.now());
        hoanTien.setLyDo(lyDoHuy);
        hoanTien.setTrangThai(0); // chờ admin xử lý

        return hoanTienRepo.save(hoanTien);
    }

    public HoanTien taoHoanTienAdmin(LichDatSan lich, String lyDo) {
        BigDecimal soTienThanhToan = BigDecimal.valueOf(lich.getThanhToan().getSoTien());

        HoanTien hoanTien = new HoanTien();
        hoanTien.setLichDatSan(lich);
        hoanTien.setSoTienDaThanhToan(soTienThanhToan);
        hoanTien.setPhanTramHoan(BigDecimal.ONE); // luôn 100%
        hoanTien.setSoTienHoan(soTienThanhToan);
        hoanTien.setTrangThai(0); // chờ xử lý
        hoanTien.setLyDo(lyDo);
        hoanTien.setNgayHuy(LocalDateTime.now());

        return hoanTienRepo.save(hoanTien);
    }

    private BigDecimal tinhPhanTramHoanTien(LichDatSan lich) {
        LocalDateTime gioBatDau = LocalDateTime.of(
                lich.getNgayDat(),
                lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau()
        );
        long hoursDiff = ChronoUnit.HOURS.between(LocalDateTime.now(), gioBatDau);

        if (hoursDiff > 24) return BigDecimal.valueOf(1.0).setScale(2, RoundingMode.HALF_UP);  // 100%
        if (hoursDiff >= 12) return BigDecimal.valueOf(0.8).setScale(2, RoundingMode.HALF_UP); // 80%
        if (hoursDiff >= 4) return BigDecimal.valueOf(0.5).setScale(2, RoundingMode.HALF_UP); // 50%
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

}
