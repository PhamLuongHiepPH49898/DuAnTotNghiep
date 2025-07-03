package com.example.datn.Repository;

import com.example.datn.Entity.LichDatSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

    /* danh sách lịch theo user – version admin */
    List<LichDatSan> findByTaiKhoanId(Integer idTaiKhoan);

    /* lịch sử đặt sân (join sân bóng để render bảng) */
    @Query("""
        SELECT l FROM LichDatSan l
        JOIN FETCH l.giaTheoKhungGio g
        JOIN FETCH g.sanBong
        WHERE l.taiKhoan.id = :id
    """)
    List<LichDatSan> layLichSuDatSan(@Param("id") Long id);

    /* ==== Hỗ trợ chức năng Người 4 – Hủy / Sửa ==== */

    /** Lấy 1 lịch đặt nhưng phải thuộc đúng user */
    @Query("""
        SELECT l FROM LichDatSan l
        JOIN FETCH l.giaTheoKhungGio g
        JOIN FETCH g.khungGio
        WHERE l.id = :id AND l.taiKhoan.id = :userId
    """)
    Optional<LichDatSan> findByIdAndTaiKhoanId(@Param("id")     Integer id,
                                               @Param("userId") Integer userId);

    /** Danh sách lịch của user (để hiển thị “Lịch đặt của tôi”) */
    @Query("""
        SELECT l FROM LichDatSan l
        JOIN FETCH l.giaTheoKhungGio g
        JOIN FETCH g.khungGio
        WHERE l.taiKhoan.id = :uid
        ORDER BY l.ngayDat DESC
    """)
    List<LichDatSan> findAllByUser(@Param("uid") Integer userId);

    /** Kiểm tra trùng khung giờ khi Sửa (true = đã trùng) */
    @Query(
            "select count(l) " +
                    "from   LichDatSan l " +
                    "where  l.giaTheoKhungGio.sanBong.id_san_bong = :sanId " +
                    "  and  l.id <> coalesce(:bookingId, -1) " +
                    "  and  l.ngayDat = :ngay " +
                    "  and  l.giaTheoKhungGio.khungGio.gioBatDau < :endTime " +
                    "  and  l.giaTheoKhungGio.khungGio.gioKetThuc > :startTime " +
                    "  and  l.trangThai <> 2"
    )
    long countConflict(@Param("sanId")     Integer   sanId,
                       @Param("ngay")       LocalDate ngay,
                       @Param("startTime")  LocalTime startTime,
                       @Param("endTime")    LocalTime endTime,
                       @Param("bookingId")  Integer   bookingId);
}

