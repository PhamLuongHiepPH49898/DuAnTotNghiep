package com.example.datn.Repository;

import com.example.datn.Entity.PhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhuongThucThanhToanRepo extends JpaRepository<PhuongThucThanhToan, Integer> {
}
