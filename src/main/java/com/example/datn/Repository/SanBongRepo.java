package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SanBongRepo extends JpaRepository<SanBong, Integer>, JpaSpecificationExecutor<SanBong> {
    @Query("SELECT s FROM SanBong s WHERE s.trang_thai IN :trangThaiList")
    List<SanBong> findByTrangThaiIn(@Param("trangThaiList") List<Integer> trangThaiList);

}
