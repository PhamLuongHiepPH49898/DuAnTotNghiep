package com.example.datn.Service;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Repository.SanBongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GiaTheoKhungGioService {
    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepo;
    @Autowired
    private SanBongRepo sanBongRepo;
    @Autowired
    private KhungGioRepo khungGioRepo;

    public List<GiaTheoKhungGio> getGiaTheoKhungGio() {
        return giaTheoKhungGioRepo.findAllByTrangThaiOrderByTenSanBong(List.of(0));
    }

    public void xoa(int id) {
        GiaTheoKhungGio giaTheoKhungGio = giaTheoKhungGioRepo.findById(id).orElse(null);
        if (giaTheoKhungGio != null) {
            giaTheoKhungGio.setTrangThai(3);
            giaTheoKhungGioRepo.save(giaTheoKhungGio);
        }
    }

    public void sua(int id, BigDecimal giaThueMoi) {
        GiaTheoKhungGio giaTheoKhungGio = giaTheoKhungGioRepo.findById(id).orElse(null);
        if (giaTheoKhungGio != null) {
            giaTheoKhungGio.setGiaThue(giaThueMoi);
            giaTheoKhungGioRepo.save(giaTheoKhungGio);
        }
    }

    public void them(BigDecimal giaThue, int idSanBong, int idKhungGio) {

        Optional<GiaTheoKhungGio> giaTonTai = giaTheoKhungGioRepo.findBySanBongAndKhungGio(idSanBong, idKhungGio);
        if (giaTonTai.isPresent()) {
            throw new IllegalArgumentException("Giờ này đã được áp dụng cho sân bóng này!");
        }

        SanBong sanBong = sanBongRepo.findById(idSanBong).orElse(null);
        KhungGio khungGio = khungGioRepo.findById(idKhungGio).orElse(null);
        if (sanBong != null && khungGio != null) {
            GiaTheoKhungGio gia = new GiaTheoKhungGio();
            gia.setGiaThue(giaThue);
            gia.setSanBong(sanBong);
            gia.setKhungGio(khungGio);
            gia.setTrangThai(0);
            giaTheoKhungGioRepo.save(gia);
        } else {
            throw new IllegalArgumentException("Không tìm thấy sân bóng hoặc khung giờ.");
        }
    }

    public List<GiaTheoKhungGio> timKiem(Integer idSanBong, Integer idKhungGio) {
        if (idSanBong != null && idKhungGio != null) {
            return giaTheoKhungGioRepo.findBySanAndKhungGio(idSanBong, idKhungGio);
        } else if (idSanBong != null) {
            return giaTheoKhungGioRepo.findBySan(idSanBong);
        } else if (idKhungGio != null) {
            return giaTheoKhungGioRepo.findByKhungGio(idKhungGio);
        } else {
            return giaTheoKhungGioRepo.findAllByTrangThaiOrderByTenSanBong(List.of(0));
        }
    }


}
