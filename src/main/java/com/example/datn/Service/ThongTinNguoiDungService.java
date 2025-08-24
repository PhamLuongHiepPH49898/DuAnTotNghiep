
package com.example.datn.Service;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public void huyLichDat(Integer idLich/*,String ghiChu*/) {
        LichDatSan lich = lichDatSanRepo.findById(idLich).orElse(null);
        if (lich != null) {

            lich.setTrangThai(2);
/*
            lich.setGhiChu(ghiChu);
*/
            lichDatSanRepo.save(lich);

            LichDatSan lichMoi = new LichDatSan();
            lichMoi.setNgayDat(lich.getNgayDat());
            lichMoi.setGiaTheoKhungGio(lich.getGiaTheoKhungGio());
            lichMoi.setTrangThai(3); // trống
            lichMoi.setGhiChu("Tạo lại sau khi hủy");
            lichMoi.setNgayTao(LocalDateTime.now());
            lichMoi.setGiaApDung(null);
            lichMoi.setTaiKhoan(null);
            lichDatSanRepo.save(lichMoi);
        }
    }

    public LichDatSan layLichTheoId(Integer idLich) {
        return lichDatSanRepo.findById(idLich).orElse(null);
    }
    public void suaLichDat(LichDatSan lichMoi) {
        LichDatSan lichCu = lichDatSanRepo.findById(lichMoi.getId()).orElse(null);
        if (lichCu != null && lichCu.getTrangThai() == 0) {
            lichCu.setGhiChu(lichMoi.getGhiChu());
            lichCu.setNgayDat(lichMoi.getNgayDat());

            // Lấy đối tượng GiaTheoKhungGio mới từ ID (được gửi từ form)
            Integer idKhungGioMoi = lichMoi.getGiaTheoKhungGio().getIdGiaTheoKhungGio();
            GiaTheoKhungGio khungGioMoi = giaTheoKhungGioRepo.findById(idKhungGioMoi).orElse(null);

            if (khungGioMoi != null) {
                lichCu.setGiaTheoKhungGio(khungGioMoi);
                lichCu.setGiaApDung(khungGioMoi.getGiaThue());
            }

            lichDatSanRepo.save(lichCu);
        }
    }


}
