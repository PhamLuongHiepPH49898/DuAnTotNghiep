package com.example.datn.Service;

import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TaiKhoanService {
    private final TaiKhoanRepo taiKhoanRepo;

    public TaiKhoanService(TaiKhoanRepo taiKhoanRepo) {
        this.taiKhoanRepo = taiKhoanRepo;
    }
    public String getTenNguoiDungDangNhap() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            String email = auth.getName();
            return taiKhoanRepo.findByEmail(email)
                    .map(tk -> tk.getHo_ten() != null ? tk.getHo_ten() : email)
                    .orElse(email);
        }
        return null;
    }
    public String getHoTenDangNhap() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getHoTen();
        }
        return null;
    }
}
