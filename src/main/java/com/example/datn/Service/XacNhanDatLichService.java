package com.example.datn.Service;

import com.example.datn.DTO.ChiTietDatLichDTO;
import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
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

    public List<Integer> kiemTraHopLeTamThoi(XacNhanDatLichDTO xacNhan) {
        List<Integer> lichTamHopLe = new ArrayList<>();

        try {
            List<ChiTietDatLichDTO> danhSach = xacNhan.getChiTietDatLichList();

            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(xacNhan.getEmail());
            if (optionalTaiKhoan.isEmpty()) {
                System.err.println("❌ Không tìm thấy tài khoản với email: " + xacNhan.getEmail());
                return lichTamHopLe;
            }

            for (ChiTietDatLichDTO chiTiet : danhSach) {
                System.out.println("🔍 [CHECK] Ngày = " + chiTiet.getNgayDat()
                        + ", ID Giá = " + chiTiet.getIdGiaTheoKhungGio());

                LichDatSan lichSan = lichDatSanRepository.findListLichTrongByNgaySanKhungGio(
                        chiTiet.getNgayDat(), chiTiet.getIdGiaTheoKhungGio()
                );

                if (lichSan == null) {
                    System.err.println("⚠️ Không tìm thấy lịch trống phù hợp.");
                    continue;
                }

                if (lichSan.getTrangThai() != 3 || lichSan.getTaiKhoan() != null) {
                    System.err.println("🚫 Lịch đã được đặt hoặc không còn trống.");
                    continue;
                }

                Optional<GiaTheoKhungGio> giaOpt = giaTheoKhungGioRepository.findById(chiTiet.getIdGiaTheoKhungGio());
                if (giaOpt.isEmpty()) {
                    System.err.println("🚫 ID giá theo khung giờ không hợp lệ: " + chiTiet.getIdGiaTheoKhungGio());
                    continue;
                }

                GiaTheoKhungGio gia = giaOpt.get();

                if (!gia.getSanBong().getTen_san_bong().trim().equalsIgnoreCase(chiTiet.getTenSan().trim())) {
                    System.err.println("🚫 Tên sân không khớp với ID giá theo khung giờ.");
                    continue;
                }

                KhungGio khungGio = gia.getKhungGio();
                String start = khungGio.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm"));
                String end = khungGio.getGioKetThuc().format(DateTimeFormatter.ofPattern("HH:mm"));
                String khungGioStr = start + "-" + end;
                String thoiGianClean = chiTiet.getThoiGian().replaceAll("\\s+", "");

                if (!khungGioStr.equalsIgnoreCase(thoiGianClean)) {
                    System.err.println("🚫 Khung giờ không khớp.");
                    continue;
                }

                BigDecimal giaHeThong = gia.getGiaThue();
                BigDecimal giaClient = chiTiet.getGia();

                if (giaClient == null || giaClient.compareTo(giaHeThong) != 0 || giaClient.compareTo(BigDecimal.ZERO) <= 0) {
                    System.err.println("🚫 Giá không hợp lệ hoặc đã bị thay đổi.");
                    continue;
                }

                // ✅ Tất cả điều kiện hợp lệ → thêm vào danh sách hợp lệ
                lichTamHopLe.add(lichSan.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lichTamHopLe;
    }


    public List<Integer> luuDatLich(XacNhanDatLichDTO xacNhan) {
        List<Integer> idLichCapNhat = new ArrayList<>();

        try {
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

                LichDatSan lichSan = lichDatSanRepository.findListLichTrongByNgaySanKhungGio(
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

                Optional<GiaTheoKhungGio> giaOpt = giaTheoKhungGioRepository.findById(chiTiet.getIdGiaTheoKhungGio());
                if (giaOpt.isEmpty()) {
                    System.err.println("🚫 ID giá theo khung giờ không hợp lệ: " + chiTiet.getIdGiaTheoKhungGio());
                    continue;
                }

                GiaTheoKhungGio gia = giaOpt.get();

                if (!gia.getSanBong().getTen_san_bong().trim().equalsIgnoreCase(chiTiet.getTenSan().trim())) {
                    System.err.println("🚫 Tên sân không khớp với ID giá theo khung giờ.");
                    continue;
                }

                KhungGio khungGio = gia.getKhungGio();
                String start = khungGio.getGioBatDau().format(DateTimeFormatter.ofPattern("HH:mm"));
                String end = khungGio.getGioKetThuc().format(DateTimeFormatter.ofPattern("HH:mm"));
                String khungGioStr = start + "-" + end;
                String thoiGianClean = chiTiet.getThoiGian().replaceAll("\\s+", "");

                if (!khungGioStr.equalsIgnoreCase(thoiGianClean)) {
                    System.err.println("🚫 Khung giờ không khớp với ID giá theo khung giờ.");
                    continue;
                }

                BigDecimal giaHeThong = gia.getGiaThue();
                BigDecimal giaClient = chiTiet.getGia();

                if (giaClient == null || giaClient.compareTo(giaHeThong) != 0 || giaClient.compareTo(BigDecimal.ZERO) <= 0) {
                    System.err.println("🚫 Giá gửi lên không hợp lệ hoặc bị sửa đổi: " + giaClient);
                    continue;
                }

                // Gán thông tin đặt lịch
                lichSan.setTrangThai(0);
                lichSan.setGiaApDung(giaHeThong);
                lichSan.setGhiChu("");
                lichSan.setTaiKhoan(taiKhoan);
                lichSan.setSanBong(lichSan.getGiaTheoKhungGio().getSanBong());

                lichDatSanRepository.save(lichSan);
                idLichCapNhat.add(lichSan.getId());

                System.out.println("✅ Đã cập nhật lịch ID: " + lichSan.getId());
            }

        } catch (Exception e) {
            e.printStackTrace(); // 👈 bạn cũng có thể dùng logger.error("Lỗi xác nhận lịch", e);
            // có thể throw lại nếu muốn controller bắt
            // throw new RuntimeException("Lỗi khi xác nhận lịch", e);
        }

        return idLichCapNhat;
    }


}

