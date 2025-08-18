package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SanBongRepo extends JpaRepository<SanBong, Integer>, JpaSpecificationExecutor<SanBong> {
    @Query("SELECT s FROM SanBong s WHERE s.trang_thai IN :trangThaiList")
    List<SanBong> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);

    @Query("SELECT s FROM SanBong s WHERE s.trang_thai IN :trangThaiList ORDER BY s.id_san_bong DESC")
    Page<SanBong> findSanBongsWithTrangThaiPaging(@Param("trangThaiList") List<Integer> trangThaiList, Pageable pageable);

    @Query("SELECT s FROM SanBong s WHERE "
           + "(:keyword IS NULL OR :keyword = '' OR LOWER(s.ten_san_bong) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
           + "AND (:loaiSan IS NULL OR s.loaiSan.id = :loaiSan) "
           + "AND (:monTheThao IS NULL OR s.loaiMonTheThao.id = :monTheThao)")
    List<SanBong> timKiemSan(@Param("keyword") String keyword,
                             @Param("loaiSan") Integer loaiSan,
                             @Param("monTheThao") Integer monTheThao);

    @Query("SELECT s FROM SanBong s WHERE "
           + "(:keyword IS NULL OR :keyword = '' OR LOWER(s.ten_san_bong) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
           + "AND (:loaiSan IS NULL OR s.loaiSan.id = :loaiSan) "
           + "AND (:monTheThao IS NULL OR s.loaiMonTheThao.id = :monTheThao)"
           + "AND s.trang_thai <> 3"
           + "ORDER BY s.id_san_bong DESC")
    Page<SanBong> timKiemSanPaging(@Param("keyword") String keyword,
                                   @Param("loaiSan") Integer loaiSan,
                                   @Param("monTheThao") Integer monTheThao, Pageable pageable);
    @Query("SELECT s FROM SanBong s WHERE s.trang_thai = 0")
    List<SanBong> findSanBongById();
}
