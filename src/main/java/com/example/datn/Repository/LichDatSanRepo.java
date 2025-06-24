
package com.example.datn.Repository;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :idTaiKhoan")
    List<LichDatSan> findByTaiKhoanId(Integer idTaiKhoan);
    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :id ORDER BY l.ngayTao DESC")
    List<LichDatSan> layLichSuDatSan(@Param("id") Long id);
    Page<LichDatSan> findByTaiKhoanId(Long idTaiKhoan, Pageable pageable);

    @Query("SELECT l FROM LichDatSan l " +
            "WHERE l.taiKhoan.id = :idTaiKhoan " +
            "AND LOWER(l.sanBong.ten_san_bong) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<LichDatSan> timKiemTheoTenSan(@Param("idTaiKhoan") Long idTaiKhoan,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :id AND l.ngayDat = :ngayDat")
    List<LichDatSan> findByTaiKhoanIdAndNgayDat(@Param("id") Long idTaiKhoan, @Param("ngayDat") LocalDate ngayDat);

}
