package com.example.datn.Repository;

import com.example.datn.Entity.HoanTien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface HoanTienRepo extends JpaRepository<HoanTien, Integer> {

    @Query("SELECT h FROM HoanTien h  ORDER BY h.id DESC")
    Page<HoanTien> findAll(Pageable pageable);

    @Query("SELECT h FROM HoanTien h " +
           "WHERE (:tenNguoiDat IS NULL OR h.lichDatSan.taiKhoan.ho_ten LIKE %:tenNguoiDat%) " +
           "AND (:soDienThoai IS NULL OR h.lichDatSan.taiKhoan.so_dien_thoai LIKE %:soDienThoai%) " +
           "AND (:sanBongId IS NULL OR h.lichDatSan.giaTheoKhungGio.sanBong.id_san_bong = :sanBongId) " +
           "AND (:trangThai IS NULL OR h.trangThai = :trangThai) " +
           " ORDER BY h.id DESC")
    Page<HoanTien> timKiem(@Param("tenNguoiDat") String tenNguoiDat,
                          @Param("soDienThoai") String soDienThoai,
                          @Param("sanBongId") Integer sanBongId,
                          @Param("trangThai") Integer trangThai, Pageable pageable);


}
