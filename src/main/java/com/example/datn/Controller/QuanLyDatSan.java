package com.example.datn.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuanLyDatSan {

    @GetMapping("/quan-ly-dat-san")
    public String quanLyDatSan() {
        return "QuanLyDatSan/QuanLyDatSan";
    }

}
