package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

@Service
public class LichDatSanService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    public List<LichDatSan> getLichDatSan() {
        return lichDatSanRepo.findAllLichDatSan();
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

    public void tuChoi(int id, String ghiChu) {
        LichDatSan lichDatSan = lichDatSanRepo.findById(id).orElse(null);
        if (lichDatSan != null) {
            lichDatSan.setTrangThai(3);
            lichDatSan.setGhiChu(ghiChu);
            lichDatSanRepo.save(lichDatSan);
        }
    }

    public List<LichDatSan> timKiem(String keyword, LocalDate ngaydat, Integer sanBong, Integer trangThai) {

        return lichDatSanRepo.timKiem(keyword, ngaydat, sanBong, trangThai);

    }

}
