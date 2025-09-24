package com.example.datn.Controller;

import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuanLyNguoiDungController {

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/quan-ly-nguoi-dung")
    public String dsUser(@RequestParam(name = "sdt", required = false) String sdt, Model model) {

        List<TaiKhoan> danhSach;

        if (sdt != null && !sdt.isEmpty()) {
            // Tìm theo số điện thoại + vai trò NGUOI_DUNG
            danhSach = taiKhoanRepo.findBySdtAndVaiTro(sdt, "NGUOI_DUNG");
        } else {
            // Lấy toàn bộ user có vai trò NGUOI_DUNG
            danhSach = taiKhoanRepo.findByVaiTro("NGUOI_DUNG");
        }

        model.addAttribute("danhSachNguoiDung", danhSach);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyNguoiDung/QuanLyNguoiDung";
    }
}
