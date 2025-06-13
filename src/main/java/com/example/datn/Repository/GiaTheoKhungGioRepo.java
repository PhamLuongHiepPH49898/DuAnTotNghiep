package com.example.datn.Repository;

import com.example.datn.Entity.GiaTheoKhungGio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GiaTheoKhungGioRepo extends JpaRepository<GiaTheoKhungGio, Integer> {
    @Query("SELECT s FROM GiaTheoKhungGio s WHERE s.trangThai IN :trangThaiList")
    List<GiaTheoKhungGio> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);
}
