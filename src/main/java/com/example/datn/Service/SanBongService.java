package com.example.datn.Service;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.SanBongRepo;
import com.example.datn.Repository.SanBongSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanBongService {

    @Autowired
    private final SanBongRepo sanbongRepo;

    public SanBongService(SanBongRepo sanbongRepo) {
        this.sanbongRepo = sanbongRepo;
    }

    // Lấy tất cả
    public List<SanBong> findAll() {
        return sanbongRepo.findAll();
    }

    // Lấy theo ID
    public SanBong findById(int id) {
        return sanbongRepo.findById(id).orElse(null);
    }

    // Tìm kiếm không phân trang
    public List<SanBong> timKiemSan(String keyword, Long loaiSan, Long monTheThao) {
        return sanbongRepo.timKiemSan(keyword, loaiSan, monTheThao);
    }

    // Tìm kiếm có phân trang
    public Page<SanBong> timKiemPhanTrang(String keyword, Long loaiSan, Long monTheThao, Pageable pageable) {
        return sanbongRepo.timKiemPhanTrang(keyword, loaiSan, monTheThao, pageable);
    }
    public List<SanBong> getAll() {
        return sanbongRepo.findAll();
    }
    public Optional<SanBong> getById(int id) {
        return sanbongRepo.findById(id);
    }

    public SanBong create(SanBong sanBong) {
        return sanbongRepo.save(sanBong);
    }

    public SanBong update(int id, SanBong newSanBong) {
        return sanbongRepo.findById(id).map(s -> {
            s.setTen_san_bong(newSanBong.getTen_san_bong());
            s.setDia_chi(newSanBong.getDia_chi());
            s.setGia(newSanBong.getGia());
            s.setMo_ta(newSanBong.getMo_ta());
            s.setHinh_anh(newSanBong.getHinh_anh());
            s.setTrang_thai(newSanBong.getTrang_thai());
            s.setLoaiSan(newSanBong.getLoaiSan());
            s.setLoaiMatSan(newSanBong.getLoaiMatSan());
            s.setLoaiMonTheThao(newSanBong.getLoaiMonTheThao());
            s.setTaiKhoan(newSanBong.getTaiKhoan());
            return sanbongRepo.save(s);
        }).orElse(null);
    }

    public void delete(int id) {
        sanbongRepo.deleteById(id);
    }
}
