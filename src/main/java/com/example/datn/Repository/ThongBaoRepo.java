package com.example.datn.Repository;

import com.example.datn.Entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThongBaoRepo extends JpaRepository<ThongBao, Integer> {
    @Query("SELECT t FROM ThongBao t WHERE t.taiKhoan.id = :idTaiKhoan ORDER BY t.trangThai ASC, t.ngayTao DESC")
    List<ThongBao> findThongBaoUuTienChuaDoc(@Param("idTaiKhoan") Integer idTaiKhoan);
    @Modifying
    @Query("UPDATE ThongBao t SET t.trangThai = 1 WHERE t.idThongBao = :idThongBao")
    void danhDauDaDoc(@Param("idThongBao") Integer idThongBao);

    // Đếm thông báo theo tài khoản & trạng thái
    int countByTaiKhoanIdAndTrangThai(Integer taiKhoanId, int trangThai);


}