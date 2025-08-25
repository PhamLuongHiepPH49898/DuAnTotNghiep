package com.example.datn.Service;

import com.example.datn.Entity.HoanTien;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.HoanTienRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HoanTienService {
    @Autowired
    private HoanTienRepo hoanTienRepo;

    public Page<HoanTien> getAllHoanTien(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoanTienRepo.findAll(pageable);
    }

    public void duyet(int id) {
        HoanTien hoanTien = hoanTienRepo.findById(id).orElse(null);
        if (hoanTien != null) {
            hoanTien.setTrangThai(1);
            hoanTien.setNgayCapNhat(LocalDateTime.now());
            hoanTienRepo.save(hoanTien);
        }
    }

    public void tuChoi(int id) {
        HoanTien hoanTien = hoanTienRepo.findById(id).orElse(null);
        if (hoanTien != null) {
            hoanTien.setTrangThai(2);
            hoanTien.setNgayCapNhat(LocalDateTime.now());
            hoanTienRepo.save(hoanTien);
        }
    }

    public Page<HoanTien> timKiem(String tenNguoiDat, String soDienThoai, Integer sanBongId, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return hoanTienRepo.timKiem(tenNguoiDat, soDienThoai, sanBongId, trangThai, pageable);
    }
}
