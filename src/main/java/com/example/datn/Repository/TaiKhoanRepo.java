package com.example.datn.Repository;

import com.example.datn.Entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaiKhoanRepo extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findByEmail(String email);
}
