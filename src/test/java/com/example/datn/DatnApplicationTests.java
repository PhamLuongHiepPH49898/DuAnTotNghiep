package com.example.datn;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class DatnApplicationTests {

    @Test
    void contextLoads() {
    }



        public static void main(String[] args) {
            String rawPassword = "123";
            String encodedFromDB = "$2a$10$G4LrWl00vjFcLycSEJ1vqeNg62GEYLNhRHY6PuUXC/.CA9dsc7wi2";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            boolean result = encoder.matches(rawPassword, encodedFromDB);

            System.out.println("Mật khẩu khớp? " + result);

    }


}
