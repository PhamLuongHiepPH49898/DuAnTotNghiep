package com.example.datn.Repository;

import com.example.datn.Entity.KhungGio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KhungGioRepo extends JpaRepository<KhungGio, Integer> {
    @Query("SELECT k FROM KhungGio k where k.trangThai = 0 ORDER BY k.gioBatDau")
    List<KhungGio> findAllOrderByGioBatDau();
}
