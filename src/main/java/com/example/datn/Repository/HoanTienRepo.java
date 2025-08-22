package com.example.datn.Repository;

import com.example.datn.Entity.HoanTien;
import com.example.datn.Entity.SanBong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HoanTienRepo extends JpaRepository<HoanTien, Integer> {
    @Query("SELECT h FROM HoanTien h  ORDER BY h.id DESC")
    Page<HoanTien> findAll(Pageable pageable);

}
