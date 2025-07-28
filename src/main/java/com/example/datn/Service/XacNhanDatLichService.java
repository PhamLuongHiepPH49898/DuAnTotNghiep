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

    @Autowired
    private LichDatSanRepo lichDatSanRepo;  // Nhớ import repo này

    public void capNhatTrangThaiThanhToan(int idLichDatSan, boolean daThanhToan) {
        Optional<LichDatSan> optional = lichDatSanRepo.findById(idLichDatSan);
        if (optional.isPresent()) {
            LichDatSan lich = optional.get();
            lich.setTrangThai(daThanhToan ? 1 : 0); // 1: đã thanh toán, 0: chưa thanh toán
            lichDatSanRepo.save(lich);
        } else {
            throw new RuntimeException("Không tìm thấy lịch đặt sân với ID: " + idLichDatSan);
        }
    }

    public List<Integer> luuDatLich(XacNhanDatLichDTO dto) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(dto.getEmail())
                .orElseGet(() -> {
                    TaiKhoan newTK = new TaiKhoan();
                    newTK.setEmail(dto.getEmail());
                    newTK.setHo_ten(dto.getHoTen());
                    newTK.setSo_dien_thoai(dto.getSoDienThoai());
                    newTK.setMat_khau("123");
                    newTK.setVai_tro("NGUOI_DUNG");
                    return taiKhoanRepository.save(newTK);
                });


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

        List<Integer> danhSachIdDaLuu = new ArrayList<>();

        for (ChiTietDatLichDTO chiTiet : dto.getChiTietDatLichList()) {
            Integer idGia = chiTiet.getIdGiaTheoKhungGio();
            if (idGia == null) {
                throw new IllegalArgumentException("ID giá theo khung giờ không được rỗng");
            }

            GiaTheoKhungGio gia = giaTheoKhungGioRepository.findById(idGia)
                    .orElseThrow(() -> new RuntimeException("Giá theo khung giờ không tồn tại: " + idGia));

            LichDatSan lich = new LichDatSan();
            lich.setNgayDat(chiTiet.getNgayDat());
            lich.setTaiKhoan(taiKhoan);
            lich.setGiaTheoKhungGio(gia);
            lich.setGiaApDung(gia.getGiaThue());
            lich.setTrangThai(0); // chờ xác nhận
            lich.setSanBong(gia.getSanBong()); // ✅ GÁN THÊM DÒNG NÀY

            LichDatSan lichDaLuu = lichDatSanRepository.save(lich);
            danhSachIdDaLuu.add(lichDaLuu.getId());
        }

        return danhSachIdDaLuu;
    }

}

