package com.example.datn.Repository;

import com.example.datn.DTO.DoanhThuSanProjection;
import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


// ThongKeRepository.java
@Repository
public interface ThongKeRepository extends JpaRepository<SanBong, Integer> {

    @Query(value = """
        SELECT s.id_san_bong  AS idSan,
               s.ten_san_bong      AS tenSan,
               CAST(COALESCE(SUM(lds.gia_ap_dung), 0) AS float) AS tongDoanhThu, -- Double
               COUNT(lds.id_lich_dat_san)               AS soLuotDat
        FROM san_bong s
        LEFT JOIN lich_dat_san lds
               ON lds.id_san_bong = s.id_san_bong
              AND lds.trang_thai IN (1)               -- chỉnh theo trạng thái hoàn tất của bạn
              AND MONTH(lds.ngay_dat) = :month
              AND YEAR(lds.ngay_dat)  = :year
        GROUP BY s.id_san_bong, s.ten_san_bong
        ORDER BY tongDoanhThu DESC
        """, nativeQuery = true)
    List<DoanhThuSanProjection> thongKeDoanhThuTheoSan(int month, int year);
}
