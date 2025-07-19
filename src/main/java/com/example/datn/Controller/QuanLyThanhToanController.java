package com.example.datn.Controller;

import com.example.datn.Entity.ThanhToan;
import com.example.datn.Repository.ThanhToanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class QuanLyThanhToanController {
    @Autowired
    private ThanhToanRepo thanhToanRepo;

    @GetMapping("/quan-ly-thanh-toan")
    public String hienThiDanhSachThanhToan(Model model) {
        List<ThanhToan> danhSach = thanhToanRepo.findAll();
        model.addAttribute("thanhToan", danhSach);
        return "QuanLyThanhToan/QuanLyThanhToan";
    }
}
