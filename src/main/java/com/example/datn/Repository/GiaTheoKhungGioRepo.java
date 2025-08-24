package com.example.datn.Repository;

import com.example.datn.Entity.GiaTheoKhungGio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GiaTheoKhungGioRepo extends JpaRepository<GiaTheoKhungGio, Integer> {
    @Query("SELECT s FROM GiaTheoKhungGio s WHERE s.trangThai IN :trangThaiList")
    List<GiaTheoKhungGio> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);

    @Query("SELECT g FROM GiaTheoKhungGio g " +
           "JOIN g.sanBong sb " +
           "JOIN g.khungGio kg " +
           "WHERE g.trangThai IN :trangThaiList " +
           "ORDER BY sb.id_san_bong ASC, kg.gioBatDau ASC")
    Page<GiaTheoKhungGio> findAllByTrangThaiOrderByTenSanBong(@Param("trangThaiList") List<Integer> trangThaiList, Pageable pageable);

    // kiểm tra sân bóng có thời gian trùng ko
    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.sanBong.id_san_bong = :idSanBong AND g.khungGio.id = :idKhungGio AND g.trangThai <> 3")
    Optional<GiaTheoKhungGio> findBySanBongAndKhungGio(@Param("idSanBong") int idSanBong, @Param("idKhungGio") int idKhungGio);

    // tim kiem
    @Query("SELECT g FROM GiaTheoKhungGio g " +
           "JOIN g.sanBong sb " +
           "JOIN g.khungGio kg " +
           "WHERE (:sanBong IS NULL OR g.sanBong.id_san_bong = :sanBong) AND " +
           "(:khungGio IS NULL OR g.khungGio.id = :khungGio) AND " +
           "g.trangThai <> 3 " +
           "ORDER BY sb.id_san_bong ASC, kg.gioBatDau ASC")
    Page<GiaTheoKhungGio> findBySanAndKhungGio(
            @Param("sanBong") Integer sanBong,
            @Param("khungGio") Integer khungGio,
            Pageable pageable);
    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.trangThai IN (:trangThaiList) AND g.sanBong.trang_thai = 0")
    List<GiaTheoKhungGio> findGiaTheoKhungGioByTrangThaiAndSanHoatDong(@Param("trangThaiList") List<Integer> trangThaiList);

    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.trangThai = 0")
    List<GiaTheoKhungGio> findGiaByTrangThai(@Param("trangThai") int trangThai);



}
