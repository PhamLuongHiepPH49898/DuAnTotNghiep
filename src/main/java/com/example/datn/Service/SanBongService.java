package com.example.datn.Service;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.SanBongRepo;
import com.example.datn.Repository.SanBongSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SanBongService {

    @Autowired
    private final SanBongRepo sanbongRepo;

    public SanBongService(SanBongRepo sanbongRepo) {
        this.sanbongRepo = sanbongRepo;
    }

    // Lấy tất cả
    public List<SanBong> findAll() {
        return sanbongRepo.findByTrangThaiIn(List.of(0,1,2));
    }

    // Lấy theo ID
    public SanBong findById(int id) {
        return sanbongRepo.findById(id).orElse(null);
    }



    public void them(SanBong sanBong) {
        sanbongRepo.save(sanBong);
    }

    public void xoa(int id) {
        SanBong sanBong = sanbongRepo.findById(id).orElse(null);
        if (sanBong != null) {
            sanBong.setTrang_thai(3); // Đặt trạng thái là 3 (đã xóa)
            sanbongRepo.save(sanBong); // Lưu lại vào DB
        }
    }
    public List<SanBong> timKiemTheoTrangThai(List<Integer> dsTrangThai) {
        return sanbongRepo.findByTrangThaiIn(List.of(0, 1, 2));
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


}
