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

@Service
public class XacNhanDatLichService {

    @Autowired
    private LichDatSanRepo lichDatSanRepository;

    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepository;

    @Autowired
    private TaiKhoanRepo taiKhoanRepository;

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

        if (dto.getChiTietDatLichList() == null || dto.getChiTietDatLichList().isEmpty()) {
            throw new IllegalArgumentException("Danh sách chi tiết đặt lịch không được rỗng");
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
            lich.setTrangThai(0);

            LichDatSan lichDaLuu = lichDatSanRepository.save(lich);
            danhSachIdDaLuu.add(lichDaLuu.getId()); // đổi thành getIdLichDatSan nếu bạn dùng tên khác
        }

        return danhSachIdDaLuu;
    }
}

