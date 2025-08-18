package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepo extends JpaRepository<TaiKhoan, Integer> {

    // Tìm theo email (dùng cho đăng nhập)
    Optional<TaiKhoan> findByEmail(String email);

    // Lấy danh sách theo vai trò (NGUOI_DUNG / QUAN_TRI)
    @Query("SELECT t FROM TaiKhoan t WHERE t.vai_tro = :vai_tro")
    List<TaiKhoan> findByVaiTro(@Param("vai_tro") String vaiTro);


    // Lọc theo số điện thoại + vai trò
    @Query("SELECT t FROM TaiKhoan t WHERE t.vai_tro = :vai_tro AND t.so_dien_thoai LIKE %:sdt%")
    List<TaiKhoan> findBySdtAndVaiTro(@Param("sdt") String sdt,
                                      @Param("vai_tro") String vaiTro);
}
