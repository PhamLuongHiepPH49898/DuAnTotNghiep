package com.example.datn.Repository;

import com.example.datn.Entity.LichDatSan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LichDatSanRepo extends JpaRepository<LichDatSan, Integer> {
    @Query("SELECT s FROM LichDatSan s WHERE s.trangThai IN :trangThaiList")
    List<LichDatSan> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);
    // thêm
    @Query("SELECT l FROM LichDatSan l WHERE l.ngayDat = :ngayDat AND l.giaTheoKhungGio.idGiaTheoKhungGio = :idGia AND l.trangThai = 3 AND l.taiKhoan IS NULL")
    LichDatSan findByNgaySanKhungGio(@Param("ngayDat") LocalDate ngayDat, @Param("idGia") Integer idGia);


}
