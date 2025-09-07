
package com.example.datn.Repository;

import com.example.datn.Entity.LichDatSan;
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
           "WHERE l.ngayDat = :ngayDat " +
           "AND l.trangThai <> 3")
    Page<LichDatSan> findAllLichDatSan(@Param("ngayDat") LocalDate ngayDat, Pageable pageable);

    @Query("SELECT l FROM LichDatSan l " +
           "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(l.taiKhoan.ho_ten) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:ngayDat IS NULL OR l.ngayDat = :ngayDat) " +
           "AND (:sanBong IS NULL OR l.giaTheoKhungGio.sanBong.id_san_bong = :sanBong) " +
           "AND (:trangThai IS NULL OR l.trangThai = :trangThai)" +
           "AND l.trangThai <> 3")
    Page<LichDatSan> timKiem(@Param("keyword") String keyword,
                             @Param("ngayDat") LocalDate ngayDat,
                             @Param("sanBong") Integer sanBong,
                             @Param("trangThai") Integer trangThai,
                             Pageable pageable);

    // dùng cho hiển thị lịch đặt sân
    @Query("SELECT l FROM LichDatSan l " +
           "JOIN FETCH l.giaTheoKhungGio g " +
           "JOIN FETCH g.khungGio k " +
           "JOIN FETCH g.sanBong s " +
           "WHERE l.ngayDat = :ngayDat AND " +
           "l.trangThai <> 2 AND " +
           "g.trangThai <> 3 AND " +
           "s.trang_thai <>3" +
           "ORDER BY k.gioBatDau ASC")
    List<LichDatSan> findByNgay(@Param("ngayDat") LocalDate ngayDat);

    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :idTaiKhoan")
    List<LichDatSan> findByTaiKhoanId(Integer idTaiKhoan);

    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :id ORDER BY l.ngayTao DESC")
    List<LichDatSan> layLichSuDatSan(@Param("id") Long id);

    Page<LichDatSan> findByTaiKhoanId(Long idTaiKhoan, Pageable pageable);


    @Query("SELECT l FROM LichDatSan l WHERE l.taiKhoan.id = :id AND l.ngayDat = :ngayDat")
    List<LichDatSan> findByTaiKhoanIdAndNgayDat(@Param("id") Long idTaiKhoan, @Param("ngayDat") LocalDate ngayDat);


    @Query("SELECT s FROM LichDatSan s WHERE s.trangThai IN :trangThaiList")
    List<LichDatSan> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);


    // // Dùng cho tạo lịch tự động
    @Query("SELECT l FROM LichDatSan l WHERE l.ngayDat = :ngayDat AND l.giaTheoKhungGio.idGiaTheoKhungGio = :idGiaTheoKhungGio ")
    List<LichDatSan> findByNgaySanKhungGio(@Param("ngayDat") LocalDate ngayDat,
                                     @Param("idGiaTheoKhungGio") Integer idGiaTheoKhungGio);
    // Dùng cho đặt sân
    @Query("SELECT l FROM LichDatSan l WHERE l.ngayDat = :ngayDat AND l.giaTheoKhungGio.idGiaTheoKhungGio = :idGiaTheoKhungGio AND l.trangThai = 3 ")
    LichDatSan findListLichTrongByNgaySanKhungGio(@Param("ngayDat") LocalDate ngayDat,
                                     @Param("idGiaTheoKhungGio") Integer idGiaTheoKhungGio);



    @Query("SELECT l FROM LichDatSan l WHERE l.trangThai = 3 AND l.ngayDat = :ngay")
    List<LichDatSan> findLichConTrongTheoNgay(@Param("ngay") LocalDate ngay);
    List<LichDatSan> findByNgayDat(LocalDate ngayDat);


}
