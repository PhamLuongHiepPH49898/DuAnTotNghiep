package com.example.datn.Service;

import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.SanBongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class LichDatSanService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;
    @Autowired
    private SanBongRepo sanBongRepo;
    @Autowired
    private KhungGioRepo khungGioRepo;

    public Page<LichDatSan> getLichDatSan(LocalDateTime ngayDat, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.findAllLichDatSan(ngayDat, pageable);
    }


    public void duyet(int id) {
        LichDatSan lichDatSan = lichDatSanRepo.findById(id).orElse(null);
        if (lichDatSan != null) {
            lichDatSan.setTrangThai(1);
            lichDatSanRepo.save(lichDatSan);
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
        }
    }


    public Page<LichDatSan> timKiem(String keyword, LocalDateTime ngaydat, Integer sanBong, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.timKiem(keyword, ngaydat, sanBong, trangThai, pageable);

    }
    public List<LichDatSan> getLichSuDatSanByTaiKhoan(int taiKhoanId) {
        return lichDatSanRepo.findByTaiKhoanId(taiKhoanId);
    }

    public Map<SanBong, List<LichDatSan>> getLichDatSanTheoNgay(LocalDateTime ngayDat, List<SanBong> danhSachSanLoc) {
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
