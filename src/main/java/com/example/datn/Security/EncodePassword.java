package com.example.datn.Security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class EncodePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123"; // Mật khẩu bạn nhập khi đăng nhập
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Matches: " + encoder.matches(rawPassword, "$2a$10$NYNtAAsp.Ntrl1pBI0yPFu1FwqoiBWB.Dj9tESCF2/MDF1k3lqN.K")); // Mật khẩu từ DB
    }

}

