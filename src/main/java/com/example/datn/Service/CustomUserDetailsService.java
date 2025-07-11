package com.example.datn.Service;

import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final TaiKhoanRepo taiKhoanRepo;

    public CustomUserDetailsService(TaiKhoanRepo taiKhoanRepo) {
        this.taiKhoanRepo = taiKhoanRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepo.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy tài khoản với email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + email);
                });

        // Kiểm tra trạng thái tài khoản (0 = hoạt động, khác 0 là bị chặn hoặc vô hiệu hóa)
        if (taiKhoan.getTrang_thai() != 0) {
            logger.error("Tài khoản không hoạt động: {}", email);
            throw new UsernameNotFoundException("Tài khoản không hoạt động: " + email);
        }

        logger.info("Tài khoản tìm thấy: {}", taiKhoan.getEmail());
        logger.debug("Mật khẩu từ DB: {}", taiKhoan.getMat_khau());
        logger.debug("Vai trò: ROLE_{}", taiKhoan.getVai_tro());

        return new CustomUserDetails(taiKhoan);
    }


}
