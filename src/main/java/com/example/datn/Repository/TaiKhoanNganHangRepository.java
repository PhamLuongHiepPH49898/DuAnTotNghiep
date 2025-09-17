package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoanNganHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaiKhoanNganHangRepository extends JpaRepository<TaiKhoanNganHang, Integer> {

    Optional<TaiKhoanNganHang> findFirstByActiveTrue();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TaiKhoanNganHang t set t.active = false where t.active = true and t.id <> :id")
    void unsetDefaultExcept(@Param("id") Integer id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update TaiKhoanNganHang t set t.active = false where t.active = true")
    void unsetAllDefault();
}



