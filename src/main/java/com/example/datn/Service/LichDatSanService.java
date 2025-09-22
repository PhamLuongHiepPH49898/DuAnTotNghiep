package com.example.datn.Service;

import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.SanBongRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class LichDatSanService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;
    @Autowired
    private TaiKhoanRepo taiKhoanRepo;
    @Autowired
    private KhungGioRepo khungGioRepo;
    @Autowired
    private ThongBaoService thongBaoService;

    public Page<LichDatSan> getLichDatSan(LocalDate ngayDat, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.findAllLichDatSan(ngayDat, pageable);
    }

    public List<LichDatSan> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of(); // Trả về list rỗng nếu không có id
        }
        return lichDatSanRepo.findAllById(ids);
    }

    public void duyet(int id) {
        LichDatSan lichDatSan = lichDatSanRepo.findById(id).orElse(null);
        if (lichDatSan != null) {
            lichDatSan.setTrangThai(1);
            lichDatSanRepo.save(lichDatSan);
            //duyet -> gui thongbao
            try {
                KhungGio khungGio = lichDatSan.getGiaTheoKhungGio().getKhungGio();
                thongBaoService.taoThongBaoXacNhan(khungGio, lichDatSan);
            } catch (Exception e) {
                System.err.println("[WARN] Gửi thông báo thất bại" + e.getMessage());
            }
        }
    }

    public void huy(int id, String ghiChu) {
        LichDatSan lichDatSan = lichDatSanRepo.findById(id).orElse(null);
        if (lichDatSan != null) {

            lichDatSan.setTrangThai(2);
            lichDatSan.setGhiChu(ghiChu);
            lichDatSanRepo.save(lichDatSan);

            LichDatSan lichMoi = new LichDatSan();
            lichMoi.setNgayDat(lichDatSan.getNgayDat());
            lichMoi.setGiaTheoKhungGio(lichDatSan.getGiaTheoKhungGio());
            lichMoi.setTrangThai(3); // trống
            lichMoi.setGhiChu("Tạo lại sau khi hủy");
            lichMoi.setNgayTao(LocalDateTime.now());
            lichMoi.setGiaApDung(null);
            lichMoi.setTaiKhoan(null);
            lichDatSanRepo.save(lichMoi);

        }try {
            KhungGio khungGio = lichDatSan.getGiaTheoKhungGio().getKhungGio();
            thongBaoService.taoThongBaoHuy(lichDatSan, khungGio);
        } catch (Exception e) {
            System.err.println("[WARN] Gửi thông báo HUỶ thất bại: " + e.getMessage());
        }
    }
    public void huyPhiaUser(int id, String ghiChu) {
        LichDatSan lichDatSan = lichDatSanRepo.findById(id).orElse(null);
        if (lichDatSan != null) {

            lichDatSan.setTrangThai(2);
            lichDatSan.setGhiChu(ghiChu);
            lichDatSanRepo.save(lichDatSan);

            LichDatSan lichMoi = new LichDatSan();
            lichMoi.setNgayDat(lichDatSan.getNgayDat());
            lichMoi.setGiaTheoKhungGio(lichDatSan.getGiaTheoKhungGio());
            lichMoi.setTrangThai(3); // trống
            lichMoi.setGhiChu("Tạo lại sau khi hủy");
            lichMoi.setNgayTao(LocalDateTime.now());
            lichMoi.setGiaApDung(null);
            lichMoi.setTaiKhoan(null);
            lichDatSanRepo.save(lichMoi);

            try {
                KhungGio khungGio = lichDatSan.getGiaTheoKhungGio().getKhungGio();

                // Gửi thông báo cho người dùng
                thongBaoService.taoThongBaoHuyNguoiDung(
                        lichDatSan,
                        khungGio,
                        ghiChu != null ? ghiChu : "Không có"
                );

                // 🔑 Gửi thông báo cho tất cả Admin


                List<TaiKhoan> admins = taiKhoanRepo.findByVaiTro("QUAN_TRI");
                System.out.println(">>> TaiKhoan trong lich: " + lichDatSan.getTaiKhoan());
                System.out.println(">>> Số admin tìm thấy: " + admins.size());
                for (TaiKhoan admin : admins) {
                    thongBaoService.taoThongBaoHuyNguoiDungChoAdmin(
                            lichDatSan,
                            khungGio,
                            ghiChu != null ? ghiChu : "Không có",
                            admin
                    );
                }
            } catch (Exception e) {
                System.err.println("[WARN] Gửi thông báo HUỶ thất bại: " + e.getMessage());
            }
        }
    }


    public Page<LichDatSan> timKiem(String keyword, LocalDate ngaydat, Integer sanBong, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.timKiem(keyword, ngaydat, sanBong, trangThai, pageable);

    }
    public List<LichDatSan> getLichSuDatSanByTaiKhoan(int taiKhoanId) {
        return lichDatSanRepo.findByTaiKhoanId(taiKhoanId);
    }

    public Map<SanBong, List<LichDatSan>> getLichDatSanTheoNgay(LocalDate ngayDat, List<SanBong> danhSachSanLoc) {
        List<LichDatSan> lichDatList = lichDatSanRepo.findByNgay(ngayDat);
        Map<SanBong, List<LichDatSan>> result = new LinkedHashMap<>();

        for (SanBong san : danhSachSanLoc) {
            List<LichDatSan> lichTheoSan = lichDatList.stream()
                    .filter(l -> l.getGiaTheoKhungGio().getSanBong().getId_san_bong() == san.getId_san_bong())
                    .toList();
            result.put(san, lichTheoSan);
        }

        return result;
    }

    public List<KhungGio> getAllKhungGio() {
        return khungGioRepo.findAllOrderByGioBatDau();
    }


}
