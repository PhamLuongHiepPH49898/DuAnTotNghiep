package com.example.datn.Service;

import com.example.datn.Entity.DanhGia;
import com.example.datn.Repository.DanhGiaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepo danhGiaRepo;

    // Lấy danh sách đánh giá theo id_san_bong
    public List<DanhGia> layDanhGiaTheoSan(Integer id_san_bong) {
        return danhGiaRepo.findById_san_bong(id_san_bong);
    }

    // Kiểm tra người dùng đã đánh giá sân chưa
    public boolean kiemTraDanhGiaTonTai(Integer id_tai_khoan, Integer id_san_bong) {
        return danhGiaRepo.existsById_tai_khoanAndId_san_bong(id_tai_khoan, id_san_bong);
    }

    // Tính trung bình sao của sân
    public Double tinhTrungBinhSao(Integer id_san_bong) {
        Optional<Double> tbSao = danhGiaRepo.tinhTrungBinhSao(id_san_bong);
        return tbSao.orElse(0.0);
    }

    // Lưu đánh giá mới
    public DanhGia luuDanhGia(DanhGia danhGia) {
        return danhGiaRepo.save(danhGia);
    }
}
