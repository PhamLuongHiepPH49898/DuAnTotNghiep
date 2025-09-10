package com.example.datn.Repository;

import com.example.datn.Entity.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {
    List<ThanhToan> findByTrangThai(Integer trangThai);
}
