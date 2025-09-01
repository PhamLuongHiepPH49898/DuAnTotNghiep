package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoanNganHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiKhoanNganHangRepository extends JpaRepository<TaiKhoanNganHang, Integer> {
    List<TaiKhoanNganHang> findByTenNganHang(String tenNganHang);
    List<TaiKhoanNganHang> findByBankCode(String bankCode);
}


