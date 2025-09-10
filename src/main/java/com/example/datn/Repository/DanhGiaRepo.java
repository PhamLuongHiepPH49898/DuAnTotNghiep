package com.example.datn.Repository;

import com.example.datn.Entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DanhGiaRepo extends JpaRepository<DanhGia, Integer> {

    // Lấy danh sách đánh giá theo id_san_bong
    @Query("SELECT d FROM DanhGia d WHERE d.sanBong.id = :id_san_bong")
    List<DanhGia> findById_san_bong(@Param("id_san_bong") Integer id_san_bong);

    // Kiểm tra người dùng đã đánh giá sân này chưa
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
            "FROM DanhGia d WHERE d.taiKhoan.id = :id_tai_khoan AND d.sanBong.id = :id_san_bong")
    boolean existsById_tai_khoanAndId_san_bong(@Param("id_tai_khoan") Integer id_tai_khoan,
                                               @Param("id_san_bong") Integer id_san_bong);

    // Tính trung bình sao của sân
    @Query("SELECT AVG(d.sao) FROM DanhGia d WHERE d.sanBong.id = :id_san_bong AND d.trangThai = 1")
    Optional<Double> tinhTrungBinhSao(@Param("id_san_bong") Integer id_san_bong);
}
