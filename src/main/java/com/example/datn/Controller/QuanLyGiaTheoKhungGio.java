package com.example.datn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuanLyGiaTheoKhungGio {

    @GetMapping("/quan-ly-gia-theo-khung-gio")
    public String quanLyGiaTheoKhungGio() {
        return "QuanLyGia/QuanLyGiaTheoKhungGio";
    }
}
