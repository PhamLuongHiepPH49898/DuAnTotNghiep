package com.example.datn.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncodePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Mật khẩu gốc người dùng nhập
        String rawPassword = "123";

        // Mã hóa mật khẩu (ví dụ lưu vào DB)
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);

        // Kiểm tra mật khẩu nhập vào so với mật khẩu mã hóa (lấy từ DB)
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);

        System.out.println("Matches: " + matches);
    }

}
