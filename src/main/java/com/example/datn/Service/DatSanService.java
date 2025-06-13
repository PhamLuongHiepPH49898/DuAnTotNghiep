package com.example.datn.Service;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.KhungGio;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.SanBongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Autowired
    private LichDatSanRepo lichDatSanRepository;

    public List<SanBong> layDanhSachSan() {
        return sanBongRepository.findByTrangThaiIn(List.of(0,1,2));
    }

    public List<KhungGio> layDanhSachKhungGio() {
        return khungGioRepository.findAll();
    }

    public List<GiaTheoKhungGio> layDanhGiaTheoKhungGio() {
        return giaTheoKhungGioRepository.findByTrangThaiIn(List.of(0,1,2));
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
    public List<String> getAllSlotKeys() {
        List<LichDatSan> danhSach = lichDatSanRepository.findAll();
        List<String> keys = new ArrayList<>();

        for (LichDatSan lich : danhSach) {
            String date = lich.getNgayDat().toString(); // yyyy-MM-dd
            String court = lich.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
            String time = lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau() + "-" +
                    lich.getGiaTheoKhungGio().getKhungGio().getGioKetThuc();

            keys.add(date + "_" + court + "_" + time);
        }

        return keys;
    }
    public SanBong laySanTheoId(int id) {
        return sanBongRepository.findById(id).orElse(null);
    }



}

