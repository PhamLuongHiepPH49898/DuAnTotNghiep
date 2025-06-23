package com.example.datn.Service;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThongTinNguoiDungService {
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    public Page<LichDatSan> layLichSuDatSan(Long idTaiKhoan, Pageable pageable) {
        return lichDatSanRepo.findByTaiKhoanId(idTaiKhoan, pageable);
    }}
