package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepo extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByEmail(String email);

    @Query("SELECT t FROM TaiKhoan t WHERE t.so_dien_thoai = :sdt")
    Optional<TaiKhoan> findBySdt(@Param("sdt") String sdt);

    @Query("SELECT t FROM TaiKhoan t WHERE t.vai_tro = :vaiTro")
    List<TaiKhoan> findByVaiTro(@Param("vaiTro") String vaiTro);
}
