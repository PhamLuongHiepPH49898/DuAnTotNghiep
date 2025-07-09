package com.example.datn.Service;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.SanBongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SanBongService {

    @Autowired
    private final SanBongRepo sanbongRepo;

    public SanBongService(SanBongRepo sanbongRepo) {
        this.sanbongRepo = sanbongRepo;
    }


    public List<SanBong> findAll() {
        return sanbongRepo.findByTrangThaiIn(List.of(0,1,2));
    }

    // Lấy theo ID
    public SanBong findById(int id) {
        return sanbongRepo.findById(id).orElse(null);
    }


    public List<SanBong> timKiemSan(String keyword, Integer loaiSan, Integer monTheThao) {

        return sanbongRepo.timKiemSan(keyword, loaiSan, monTheThao);

    }


    public void them(SanBong sanBong) {
        sanbongRepo.save(sanBong);
    }

    public void xoa(int id) {
        SanBong sanBong = sanbongRepo.findById(id).orElse(null);
        if (sanBong != null) {
            sanBong.setTrang_thai(3); // Đặt trạng thái là 3 (đã xóa)
            sanbongRepo.save(sanBong);
        }
    }
    public Page<SanBong> timKiemTheoPage( String keyword, Integer loaiSan, Integer monTheThao, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sanbongRepo.timKiemSanPaging(keyword, loaiSan, monTheThao, pageable);
    }

    public void sua(SanBong sanBong) {
        System.out.println("Tên sân: " + sanBong.getTen_san_bong());
        sanbongRepo.save(sanBong);
    }

    public List<SanBong> getSanBong(){
        return sanbongRepo.findByTrangThaiIn(List.of(0,1,2));
    }

    public SanBong getByID(int id) {
        return sanbongRepo.findById(id).orElse(null);
    }

    public Page<SanBong> getSanBongPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return sanbongRepo.findSanBongsWithTrangThaiPaging(List.of(0,1,2), pageable);
    }

    public List<SanBong> timKiemSan(String tenSan, Integer loaiSanId, Integer matSanId, Integer monTheThaoId) {
        return sanbongRepo.findByDieuKien(tenSan, loaiSanId, matSanId, monTheThaoId);
    }


}
