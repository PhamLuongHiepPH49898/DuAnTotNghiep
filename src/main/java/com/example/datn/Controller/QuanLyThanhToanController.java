package com.example.datn.Controller;

import com.example.datn.Entity.PhuongThucThanhToan;
import com.example.datn.Entity.TaiKhoanNganHang;
import com.example.datn.Repository.PhuongThucThanhToanRepo;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quan-ly-thanh-toan")
public class QuanLyThanhToanController {

    @Autowired
    private TaiKhoanNganHangRepository repo;

    @Autowired
    private PhuongThucThanhToanRepo phuongThucRepo;

    @GetMapping
    public String hienThi(Model model) {
        var danhSachTaiKhoan = repo.findAll();
        var danhSachPhuongThuc = phuongThucRepo.findAll();

        System.out.println("==> dsPhuongThuc: " + danhSachPhuongThuc.size());
        danhSachPhuongThuc.forEach(p -> System.out.println(p.getTenPhuongThucThanhToan()));

        System.out.println("==> dsTaiKhoan: " + danhSachTaiKhoan.size());
        danhSachTaiKhoan.forEach(tk -> System.out.println(tk.getSoTaiKhoan()));
        model.addAttribute("dsTaiKhoan", repo.findAll());
        model.addAttribute("dsPhuongThuc", phuongThucRepo.findAll());
        model.addAttribute("taiKhoanMoi", new TaiKhoanNganHang());
        return "ThanhToan/QuanLyThanhToan";
    }

    @PostMapping("/them")
    public String themTaiKhoan(@ModelAttribute TaiKhoanNganHang taiKhoan) {
        System.out.println("==> thêm: " + taiKhoan);
        repo.save(taiKhoan);
        return "redirect:/quan-ly-thanh-toan";
    }
    @GetMapping("/xoa/{id}")
    public String xoaTaiKhoan(@PathVariable Integer id) {
        repo.deleteById(id);
        return "redirect:/quan-ly-thanh-toan";
    }
}

