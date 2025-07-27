package com.example.datn.Service;

import com.example.datn.Entity.DanhGia;
import com.example.datn.Repository.DanhGiaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DanhGiaService {
    @Autowired
    private DanhGiaRepo danhGiaRepo;


    public List<DanhGia> layDanhGiaTheoSan(Integer sanId) {
        return danhGiaRepo.findBySanBong_IdSanBong(sanId); // ✅ sửa ở đây
    }

    public void luuDanhGia(DanhGia danhGia) {
        danhGia.setNgayDanhGia(LocalDate.now());
        danhGiaRepo.save(danhGia);
    }
}
