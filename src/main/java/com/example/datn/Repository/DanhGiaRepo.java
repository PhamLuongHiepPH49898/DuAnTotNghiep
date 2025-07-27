package com.example.datn.Repository;

import com.example.datn.Entity.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepo extends JpaRepository<DanhGia, Integer> {
    List<DanhGia> findBySanBong_IdSanBong(Integer idSanBong);
}
