package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
public class LichDatSanService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    public Page<LichDatSan> getLichDatSan(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.findAllLichDatSan(pageable);
    }

    public Page<LichDatSan> timKiemTheoNgayTao(LocalDateTime start, LocalDateTime end, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.findByNgayTaoBetween(start, end, pageable);
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
        }
    }


    public Page<LichDatSan> timKiem(String keyword, LocalDate ngaydat, Integer sanBong, Integer trangThai, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return lichDatSanRepo.timKiem(keyword, ngaydat, sanBong, trangThai, pageable);

    }

}
