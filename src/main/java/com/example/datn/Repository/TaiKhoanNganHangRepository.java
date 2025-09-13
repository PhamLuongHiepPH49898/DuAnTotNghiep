package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoanNganHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanNganHangRepository extends JpaRepository<TaiKhoanNganHang, Integer> {
    Optional<TaiKhoanNganHang> findFirstByActiveTrue();
}


