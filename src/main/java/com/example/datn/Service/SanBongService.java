package com.example.datn.Service;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.SanBongRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanBongService {

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
}
