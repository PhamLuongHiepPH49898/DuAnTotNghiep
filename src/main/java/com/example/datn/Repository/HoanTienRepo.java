package com.example.datn.Repository;

import com.example.datn.Entity.HoanTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;


public interface HoanTienRepo extends JpaRepository<HoanTien, Integer> {

    @Query("SELECT h FROM HoanTien  h  WHERE (:start IS NULL OR h.ngayHuy BETWEEN :start AND :end)  ORDER BY h.id DESC")
    Page<HoanTien> findAll(@Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end
            , Pageable pageable);

    @Query("SELECT h FROM HoanTien h " +
           "WHERE (:tenNguoiDat IS NULL OR h.lichDatSan.taiKhoan.ho_ten LIKE %:tenNguoiDat%) " +
           "AND (:start IS NULL OR h.ngayHuy BETWEEN :start AND :end) " +
           "AND (:soDienThoai IS NULL OR h.lichDatSan.taiKhoan.so_dien_thoai LIKE %:soDienThoai%) " +
           "AND (:sanBongId IS NULL OR h.lichDatSan.giaTheoKhungGio.sanBong.id_san_bong = :sanBongId) " +
           "AND (:trangThai IS NULL OR h.trangThai = :trangThai) " +
           " ORDER BY h.id DESC")
    Page<HoanTien> timKiem(@Param("tenNguoiDat") String tenNguoiDat,
                           @Param("start") LocalDateTime start,
                           @Param("end") LocalDateTime end,
                           @Param("soDienThoai") String soDienThoai,
                           @Param("sanBongId") Integer sanBongId,
                           @Param("trangThai") Integer trangThai, Pageable pageable);

    HoanTien findByLichDatSanId(Integer lichDatSanId);

}
