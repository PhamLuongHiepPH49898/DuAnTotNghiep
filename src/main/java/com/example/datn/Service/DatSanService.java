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
    //check lich chua đc tao
    public List<String> getAllSlotKeysTonTai() {
        List<LichDatSan> lichTonTai = lichDatSanRepository.findAll();
        List<String> slotKeys = new ArrayList<>();

        for (LichDatSan lich : lichTonTai) {
            String ngay = lich.getNgayDat().toString(); // yyyy-MM-dd
            String tenSan = lich.getGiaTheoKhungGio().getSanBong().getTen_san_bong();
            String thoiGian = lich.getGiaTheoKhungGio().getKhungGio().getGioBatDau() + "-" +
                    lich.getGiaTheoKhungGio().getKhungGio().getGioKetThuc();

            String key = ngay + "_" + tenSan + "_" + thoiGian;
            slotKeys.add(key);
        }

        return slotKeys;
    }


    public List<String> getAllSlotKeys() {
        List<LichDatSan> danhSach = lichDatSanRepository.findByTrangThaiIn(List.of(0,1));// fix để khi hủy lịch thì lịch đó có thể đặt tiếp
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

