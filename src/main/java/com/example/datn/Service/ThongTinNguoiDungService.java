package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongTinNguoiDungService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    public List<LichDatSan> layLichSuDatSan(Integer idTaiKhoan) {
        return lichDatSanRepo.findByTaiKhoanId(idTaiKhoan);
    }
    public List<LichDatSan> layLichSuDatSan(Long taiKhoanId) {
        return lichDatSanRepo.layLichSuDatSan(taiKhoanId);
    }
}
