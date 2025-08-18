
package com.example.datn.Service;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ThongTinNguoiDungService {

    @Autowired
    private LichDatSanRepo lichDatSanRepo;
    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepo;

    // Lấy tất cả lịch sử đặt sân (phân trang)
    public Page<LichDatSan> layLichSuDatSan(Long idTaiKhoan, Pageable pageable) {
        return lichDatSanRepo.findByTaiKhoanId(idTaiKhoan, pageable);
    }

    // Tìm kiếm theo tên sân (có phân trang)
    public List<LichDatSan> timLichDatHomNay(Long idTaiKhoan) {
        LocalDate today = LocalDate.now();
        return lichDatSanRepo.findByTaiKhoanIdAndNgayDat(idTaiKhoan, today);
    }
    public void huyLichDat(Integer id, String ghiChu) {
        LichDatSan lich = lichDatSanRepo.findById(id).orElse(null);
        if (lich == null) {
            return;
        }

        // Kiểm tra các trường quan trọng để tránh NullPointerException
        if (lich.getNgayDat() == null
                || lich.getGiaTheoKhungGio() == null
                || lich.getGiaTheoKhungGio().getKhungGio() == null
                || lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau() == null) {
            throw new IllegalStateException("Dữ liệu lịch đặt không hợp lệ, thiếu ngày hoặc giờ bắt đầu.");
        }

        // Tạo LocalDateTime cho giờ bắt đầu đặt sân
        LocalDateTime gioBatDau = LocalDateTime.of(
                lich.getNgayDat(),
                lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau()
        );

        // Tính khoảng thời gian còn lại (giờ) từ hiện tại tới giờ bắt đầu
        long hoursBefore = Duration.between(LocalDateTime.now(), gioBatDau).toHours();

        if (hoursBefore < 2) {
            throw new IllegalStateException("Không thể hủy sát giờ (trước 2 tiếng).");
        }

        // Xác định mức hoàn tiền (nếu có)
        double tiLeHoan = 0.0;
        if (hoursBefore > 24) {
            tiLeHoan = 1.0; // hoàn 100%
        } else if (hoursBefore >= 12) {
            tiLeHoan = 0.5; // hoàn 50%
        } else {
            tiLeHoan = 0.0; // không hoàn
        }

        // TODO: Xử lý hoàn tiền (nếu có hệ thống thanh toán)
        // viService.hoanTien(lich.getTaiKhoan(), lich.getGiaApDung() * tiLeHoan);

        // Cập nhật trạng thái lịch cũ thành đã hủy
        lich.setTrangThai(2);
    lich.setGhiChu(ghiChu); // nếu dùng ghi chú
        lichDatSanRepo.save(lich);

        // Tạo lại slot trống để khách khác đặt
        LichDatSan lichMoi = new LichDatSan();
        lichMoi.setNgayDat(lich.getNgayDat());
        lichMoi.setGiaTheoKhungGio(lich.getGiaTheoKhungGio());
        lichMoi.setTrangThai(3); // trạng thái trống
        lichMoi.setGhiChu("Tạo lại sau khi hủy");
        lichMoi.setNgayTao(LocalDateTime.now());
        lichMoi.setGiaApDung(null);
        lichMoi.setTaiKhoan(null);
        lichDatSanRepo.save(lichMoi);
    }
}
