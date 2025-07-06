package com.example.datn.Service;

import com.example.datn.DTO.ChiTietDatLichDTO;
import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class XacNhanDatLichService {
    @Autowired
    private LichDatSanRepo lichDatSanRepository;
    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepository;
    @Autowired
    private TaiKhoanRepo taiKhoanRepository;

    public List<Integer> luuDatLich(XacNhanDatLichDTO xacNhan) {
        List<Integer> idLichCapNhat = new ArrayList<>();
        List<ChiTietDatLichDTO> danhSach = xacNhan.getChiTietDatLichList();

        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(xacNhan.getEmail());
        if (optionalTaiKhoan.isEmpty()) {
            System.err.println("❌ Không tìm thấy tài khoản với email: " + xacNhan.getEmail());
            return idLichCapNhat;
        }
        TaiKhoan taiKhoan = optionalTaiKhoan.get();

        for (ChiTietDatLichDTO chiTiet : danhSach) {
            System.out.println("🔍 Đang tìm lịch cho: Ngày = " + chiTiet.getNgayDat()
                    + ", ID Giá = " + chiTiet.getIdGiaTheoKhungGio());

            LichDatSan lichSan = lichDatSanRepository.findByNgaySanKhungGio(
                    chiTiet.getNgayDat(), chiTiet.getIdGiaTheoKhungGio()
            );

            if (lichSan == null) {
                System.err.println("⚠️ Không tìm thấy lịch trống phù hợp.");
                continue;
            }

            if (lichSan.getTrangThai() != 3 || lichSan.getTaiKhoan() != null) {
                System.err.println("🚫 Lịch đã được đặt bởi người khác hoặc không còn trống.");
                continue;
            }

            // Gán thông tin đặt lịch
            lichSan.setTrangThai(0); // Đã đặt
            lichSan.setGiaApDung(chiTiet.getGia());
            lichSan.setGhiChu("");
            lichSan.setTaiKhoan(taiKhoan);
            lichSan.setSanBong(lichSan.getGiaTheoKhungGio().getSanBong());

            lichDatSanRepository.save(lichSan);
            idLichCapNhat.add(lichSan.getId());

            System.out.println("✅ Đã cập nhật lịch ID: " + lichSan.getId());
        }

        return idLichCapNhat;
    }

}
