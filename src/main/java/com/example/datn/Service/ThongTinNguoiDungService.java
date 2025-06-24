
package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ThongTinNguoiDungService {

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    // Lấy tất cả lịch sử đặt sân (phân trang)
    public Page<LichDatSan> layLichSuDatSan(Long idTaiKhoan, Pageable pageable) {
        return lichDatSanRepo.findByTaiKhoanId(idTaiKhoan, pageable);
    }

    // Tìm kiếm theo tên sân (có phân trang)
    public Page<LichDatSan> timKiemLichSuDatSanTheoTenSan(Long idTaiKhoan, String keyword, Pageable pageable) {
        return lichDatSanRepo.timKiemTheoTenSan(idTaiKhoan, keyword, pageable);
    }
    public List<LichDatSan> timLichDatHomNay(Long idTaiKhoan) {
        LocalDate today = LocalDate.now();
        return lichDatSanRepo.findByTaiKhoanIdAndNgayDat(idTaiKhoan, today);
    }


}
