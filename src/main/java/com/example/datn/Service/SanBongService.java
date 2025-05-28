package com.example.datn.Service;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.SanBongRepo;
import com.example.datn.Repository.SanBongSpecification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanBongService {
    private final SanBongRepo sanbongRepo;

    public SanBongService(SanBongRepo sanbongRepo) {
        this.sanbongRepo = sanbongRepo;
    }

    public List<SanBong> findAll() {
        return sanbongRepo.findAll();
    }

    public SanBong findById(int id) {
        return sanbongRepo.findById(id).orElse(null);
    }

    public List<SanBong> timKiemSan(String keyword, Long loaiSan, Long monTheThao) {
        return sanbongRepo.timKiemSan(keyword, loaiSan, monTheThao);
    }

}
