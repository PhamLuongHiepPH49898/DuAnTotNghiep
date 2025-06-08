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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatSanService {
    @Autowired
    private SanBongRepo sanBongRepository;

    @Autowired
    private KhungGioRepo khungGioRepository;

    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepository;

    public List<SanBong> layDanhSachSan() {
        return sanBongRepository.findAll();
    }

    public List<KhungGio> layDanhSachKhungGio() {
        return khungGioRepository.findAll();
    }

    public List<GiaTheoKhungGio> layDanhGiaTheoKhungGio() {
        return giaTheoKhungGioRepository.findAll();
    }

    public Map<String, BigDecimal> layBangGia() {
        List<GiaTheoKhungGio> danhSachGia = giaTheoKhungGioRepository.findAll();
        Map<String, BigDecimal> bangGia = new HashMap<>();
        for (GiaTheoKhungGio gia : danhSachGia) {
            String key = gia.getSanBong().getId_san_bong() + "_" + gia.getKhungGio().getId();
            System.out.println("KEY: " + key + ", Giá: " + gia.getGiaThue());
            bangGia.put(key, gia.getGiaThue());
        }
        return bangGia;
    }

}

