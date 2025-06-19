package com.example.datn.Repository;

import com.example.datn.Entity.ThanhToan;
import com.example.datn.DTO.ThongKeDoanhThuDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThanhToanRepo extends JpaRepository<ThanhToan, Integer> {

    @Query("SELECT s.ten_san_bong, FUNCTION('MONTH', t.ngayThanhToan), SUM(t.soTien) " +
            "FROM ThanhToan t " +
            "JOIN t.lichDatSan lds " +
            "JOIN lds.giaTheoKhungGio gtk " +
            "JOIN gtk.sanBong s " +
            "WHERE FUNCTION('YEAR', t.ngayThanhToan) = :nam " +
            "AND FUNCTION('MONTH', t.ngayThanhToan) = :thang " +
            "GROUP BY s.ten_san_bong, FUNCTION('MONTH', t.ngayThanhToan)")
    List<Object[]> thongKeTheoSanVaThangVaNam(@Param("thang") int thang, @Param("nam") int nam);


}
