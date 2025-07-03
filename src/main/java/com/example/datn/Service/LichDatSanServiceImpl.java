package com.example.datn.Service;

import com.example.datn.DTO.LichDatSanForm;
import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;      // ✅ đúng package
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LichDatSanServiceImpl implements LichDatSanService2 {

    private final LichDatSanRepo repo;
    private final GiaTheoKhungGioRepo giaRepo;

    @Value("${booking.change-cutoff-hours:2}")
    private int cutoffHours;

    /* ---- kiểm tra quá hạn ---- */
    private void assertInTime(LichDatSan lich, String action) {
        LocalDateTime start = LocalDateTime.of(
                lich.getNgayDat(),
                lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau());
        if (Duration.between(LocalDateTime.now(), start).toHours() < cutoffHours) {
            throw new IllegalStateException("Quá hạn " + action + " (" + cutoffHours + " giờ)");
        }
    }

    /* ===== API ===== */

    @Override
    public LichDatSan findByIdForUser(Integer id, Integer userId) {
        return repo.findByIdAndTaiKhoanId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy lịch"));
    }

    @Override
    public List<LichDatSan> getBookingsOfUser(Integer userId) {
        return repo.findAllByUser(userId);
    }

    @Override
    public LichDatSan updateBooking(Integer id, LichDatSanForm form, Integer userId) {

        LichDatSan lich = findByIdForUser(id, userId);
        assertInTime(lich, "chỉnh sửa");

        /* --- bảng giá mới --- */
        GiaTheoKhungGio g = giaRepo.getReferenceById(form.getIdGiaTheoKhungGio());

        /* --- kiểm tra trùng khung giờ --- */
        Integer   sanId     = g.getSanBong().getId_san_bong();
        LocalDate ngay      = form.getNgayDat();
        LocalTime startTime = g.getKhungGio().getGioBatDau();
        LocalTime endTime   = g.getKhungGio().getGioKetThuc();

        boolean conflict = repo.countConflict(
                sanId, ngay, startTime, endTime, id) > 0;
        if (conflict)
            throw new IllegalArgumentException("Khung giờ đã có người đặt");

        /* --- cập nhật --- */
        lich.setNgayDat(ngay);
        lich.setGiaTheoKhungGio(g);
        lich.setGhiChu(form.getGhiChu());

        return lich;
    }

    @Override
    public void cancelBooking(Integer id, Integer userId) {
        LichDatSan lich = findByIdForUser(id, userId);
        assertInTime(lich, "hủy");
        lich.setTrangThai(2);          // 2 = đã hủy
    }
}
