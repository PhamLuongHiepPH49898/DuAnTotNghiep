package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import org.springframework.data.repository.query.Param;


import java.util.List;


public interface SanBongRepo extends JpaRepository<SanBong, Integer>, JpaSpecificationExecutor<SanBong> {
    @Query("SELECT s FROM SanBong s WHERE s.trang_thai IN :trangThaiList")
    List<SanBong> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);

    List<SanBong> findByTaiKhoan_Id(int taiKhoanId);

    @Query("SELECT s FROM SanBong s WHERE "
           + "(:keyword IS NULL OR :keyword = '' OR LOWER(s.ten_san_bong) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
           + "AND (:loaiSan IS NULL OR s.loaiSan.id = :loaiSan) "
           + "AND (:monTheThao IS NULL OR s.loaiMonTheThao.id = :monTheThao)")
    List<SanBong> timKiemSan(String keyword, Long loaiSan, Long monTheThao);

}
