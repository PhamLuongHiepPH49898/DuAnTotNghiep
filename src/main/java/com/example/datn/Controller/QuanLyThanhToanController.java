package com.example.datn.Controller;

import com.example.datn.Entity.ThanhToan;
import com.example.datn.Repository.ThanhToanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuanLyThanhToanController {

    @Autowired
    private ThanhToanRepo thanhToanRepo;

    @GetMapping("/quan-ly-thanh-toan")
    public String hienThiDanhSachThanhToan(
            @RequestParam(name = "trangThai", required = false) String trangThai,
            Model model
    ) {
        Integer trangThaiInt = null;

        // Chuyển trạng thái sang số nếu có
        if (trangThai != null && !trangThai.isEmpty()) {
            try {
                trangThaiInt = Integer.parseInt(trangThai);
            } catch (NumberFormatException e) {
                // Nếu lỗi parse, giữ nguyên null (không lọc)
            }
        }

        // Lấy danh sách thanh toán theo trạng thái (nếu null thì lấy tất cả)
        List<ThanhToan> danhSach = thanhToanRepo.findByTrangThai(trangThaiInt);

        // Gửi dữ liệu ra view
        model.addAttribute("thanhToan", danhSach);
        model.addAttribute("trangThai", trangThai);

        return "QuanLyThanhToan/QuanLyThanhToan";
    }
}
