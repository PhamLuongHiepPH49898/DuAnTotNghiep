package com.example.datn.Repository;

import com.example.datn.Entity.GiaTheoKhungGio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GiaTheoKhungGioRepo extends JpaRepository<GiaTheoKhungGio, Integer> {
    @Query("SELECT s FROM GiaTheoKhungGio s WHERE s.trangThai IN :trangThaiList")
    List<GiaTheoKhungGio> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);

    @Query("SELECT s FROM GiaTheoKhungGio s " +
           "JOIN s.sanBong sb " +
           "JOIN s.khungGio kg " +
           "WHERE s.trangThai IN :trangThaiList " +
           "ORDER BY sb.ten_san_bong ASC, kg.gioBatDau ASC")
    List<GiaTheoKhungGio> findAllByTrangThaiOrderByTenSanBong(@Param("trangThaiList") List<Integer> trangThaiList);

    // kiểm tra sân bóng có thời gian trùng ko
    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.sanBong.id_san_bong = :idSanBong AND g.khungGio.id = :idKhungGio AND g.trangThai <> 3")
    Optional<GiaTheoKhungGio> findBySanBongAndKhungGio(@Param("idSanBong") int idSanBong, @Param("idKhungGio") int idKhungGio);

    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.sanBong.id_san_bong = :idSanBong AND g.khungGio.id = :idKhungGio AND g.trangThai <> 3")
    List<GiaTheoKhungGio> findBySanAndKhungGio(@Param("idSanBong") int idSanBong, @Param("idKhungGio") int idKhungGio);

    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.sanBong.id_san_bong = :idSanBong AND g.trangThai <> 3")
    List<GiaTheoKhungGio> findBySan(@Param("idSanBong") int idSanBong);

    @Query("SELECT g FROM GiaTheoKhungGio g WHERE g.khungGio.id = :idKhungGio AND g.trangThai <> 3")
    List<GiaTheoKhungGio> findByKhungGio(@Param("idKhungGio") int idKhungGio);




}
