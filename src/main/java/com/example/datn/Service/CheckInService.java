package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CheckInService {

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    //Cho phép check-in trước giờ đá bao nhiêu phút
    private static final long OPEN_BEFORE_MIN = 30;

    // Cho phép trễ sau giờ đá bao nhiêu phút (grace)
    private static final long GRACE_AFTER_START_MIN = 10;

    private static final DateTimeFormatter DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");


    @Transactional
    public String checkIn(Integer id) {
        Optional<LichDatSan> optional = lichDatSanRepo.findById(id);
        if (optional.isEmpty()) {
            return "❌ Không tìm thấy lịch đặt sân.";
        }

        LichDatSan lich = optional.get();

        // Đã check-in trước đó?
        if (lich.getTrangThaiCheckIn() != null) {
            if (lich.getTrangThaiCheckIn() == 1) {
                return "✅ <b>Bạn đã check-in trước đó!</b> <br> vào lúc <b>" + formatDate(lich.getThoiGianCheckIn()) + "</b>.";

            } else if (lich.getTrangThaiCheckIn() == 2) {
                return "⚠️ <b>Lịch đặt này đã được đánh dấu là <i>KHÔNG ĐẾN</i>.</b><br>Vui lòng liên hệ quản lý sân nếu có thắc mắc.";
            }
        }

        // Lấy thời gian bắt đầu sân từ GiaTheoKhungGio -> KhungGio
        LocalTime gioBatDau = lich.getGiaTheoKhungGio()
                .getKhungGio()
                .getGioBatDau();
        LocalTime gioKetThuc = lich.getGiaTheoKhungGio()
                .getKhungGio()
                .getGioKetThuc();
        LocalDateTime thoiDiemBatDau = LocalDateTime.of(lich.getNgayDat(), gioBatDau);
        LocalDateTime thoiDiemKetThuc = LocalDateTime.of(lich.getNgayDat(), gioKetThuc);
        // Tính cửa sổ hợp lệ
        LocalDateTime moCheckInTu = thoiDiemBatDau.minusMinutes(OPEN_BEFORE_MIN);
        LocalDateTime dongCheckInLuc = thoiDiemKetThuc.plusMinutes(GRACE_AFTER_START_MIN);

        LocalDateTime now = LocalDateTime.now();

        // Chưa tới cửa sổ check-in?
        if (now.isBefore(moCheckInTu)) {
            return "⏰ <b>Hiện chưa đến thời gian check-in.</b><br>" +
                    "Bạn có thể thực hiện check-in trong khoảng từ <b>" + formatDate(moCheckInTu) + "</b> đến <b>" + formatDate(dongCheckInLuc) + "</b>.<br>" +
                    "<i>Lưu ý: Vui lòng không check-in trong thời gian trận đấu diễn ra (" + gioBatDau + " - " + gioKetThuc + ").</i>";

        }


        // Quá hạn check-in?
        if (now.isAfter(dongCheckInLuc)) {
            return "⛔ <b>Check-in không thành công.</b><br>" +
                    "Thời gian check-in cho phép đã kết thúc vào lúc <b>" + formatDate(dongCheckInLuc) + "</b>.<br>" +
                    "<i>Vui lòng liên hệ quản lý sân để được hỗ trợ.</i>";

        }


        // Trong cửa sổ — cho phép check-in
        lich.setTrangThaiCheckIn(1); // ĐÃ CHECK-IN
        lich.setThoiGianCheckIn(now);
        lichDatSanRepo.save(lich);

        return "✅ <b>Check-in thành công!</b><br>Bạn đã check-in vào lúc <b>" + formatDate(now) + "</b>.";
    }

    private String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMAT);
    }
    public LichDatSan getLichDatSanById(Integer id) {
        return lichDatSanRepo.findById(id).orElse(null);
    }

}
