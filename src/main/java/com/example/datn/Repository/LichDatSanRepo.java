package com.example.datn.Repository;

import com.example.datn.Entity.LichDatSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LichDatSanRepo extends JpaRepository<LichDatSan, Integer> {


    @Query("SELECT l FROM LichDatSan l " +
           "JOIN FETCH l.taiKhoan tk " +
           "JOIN FETCH l.giaTheoKhungGio g " +
           "JOIN FETCH g.khungGio kg " +
           "ORDER BY l.ngayTao DESC")
    List<LichDatSan> findAllLichDatSan();

    @Query("SELECT l FROM LichDatSan l " +
           "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(l.taiKhoan.ho_ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:ngayDat IS NULL OR l.ngayDat = :ngayDat) " +
           "AND (:sanBong IS NULL OR l.giaTheoKhungGio.sanBong.id_san_bong = :sanBong) " +
           "AND (:trangThai IS NULL OR l.trangThai = :trangThai)")
    List<LichDatSan> timKiem(@Param("keyword") String keyword,
                             @Param("ngayDat") LocalDate ngayDat,
                             @Param("sanBong") Integer sanBong,
                             @Param("trangThai") Integer trangThai);

    List<LichDatSan> findByNgayTaoBetween(LocalDateTime start, LocalDateTime end);

}
