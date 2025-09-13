package com.example.datn.Service;

import com.example.datn.Entity.TaiKhoanNganHang;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaiKhoanNganHangService {

    @Autowired private TaiKhoanNganHangRepository repo;

    public TaiKhoanNganHang getByIdOrDefault(Integer accId) {
        if (accId != null) {
            return repo.findById(accId)
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản id=" + accId));
        }
        return repo.findFirstByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("Chưa cấu hình tài khoản ngân hàng mặc định (active=true)"));
    }
}

