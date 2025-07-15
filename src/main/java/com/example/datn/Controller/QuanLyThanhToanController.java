package com.example.datn.Controller;

import com.example.datn.Entity.PhuongThucThanhToan;
import com.example.datn.Entity.TaiKhoanNganHang;
import com.example.datn.Repository.PhuongThucThanhToanRepo;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import com.example.datn.Service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quan-ly-thanh-toan")
public class QuanLyThanhToanController {

    @Autowired
    private TaiKhoanNganHangRepository taiKhoanRepo;

    @Autowired
    private PhuongThucThanhToanRepo phuongThucRepo;

    @Autowired
    private BankService bankService;

    @GetMapping
    public String hienThiTrangThanhToan(Model model) {
        model.addAttribute("dsTaiKhoan", taiKhoanRepo.findAll());
        model.addAttribute("dsPhuongThuc", phuongThucRepo.findAll());
        model.addAttribute("banks", bankService.getAllBanks());  // lấy ngân hàng từ API
        return "ThanhToan/QuanLyThanhToan";
    }

    @PostMapping("/them")
    public String themTaiKhoan(@ModelAttribute TaiKhoanNganHang taiKhoan) {
        if (taiKhoan.getBankCode() == null || taiKhoan.getBankCode().isEmpty() ||
                taiKhoan.getTenNganHang() == null || taiKhoan.getTenNganHang().isEmpty()) {
            throw new IllegalArgumentException("Ngân hàng không được để trống!");
        }
        taiKhoanRepo.save(taiKhoan);
        return "redirect:/quan-ly-thanh-toan";
    }

    @GetMapping("/xoa/{id}")
    public String xoaTaiKhoan(@PathVariable Integer id) {
        taiKhoanRepo.deleteById(id);
        return "redirect:/quan-ly-thanh-toan";
    }
    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Integer id, Model model) {
        Optional<TaiKhoanNganHang> optionalTaiKhoan = taiKhoanRepo.findById(id);
        if (optionalTaiKhoan.isPresent()) {
            model.addAttribute("taiKhoan", optionalTaiKhoan.get());
            model.addAttribute("dsPhuongThuc", phuongThucRepo.findAll());
            return "ThanhToan/SuaTaiKhoan";  // Tên template Thymeleaf (bạn tự tạo file này)
        } else {
            return "redirect:/quan-ly-thanh-toan";
        }
    }
    @PostMapping("/cap-nhat")
    public String capNhatTaiKhoan(@ModelAttribute TaiKhoanNganHang taiKhoan) {
        if (taiKhoan.getBankCode() == null || taiKhoan.getBankCode().isEmpty() ||
                taiKhoan.getTenNganHang() == null || taiKhoan.getTenNganHang().isEmpty()) {
            throw new IllegalArgumentException("Ngân hàng không được để trống!");
        }
        taiKhoanRepo.save(taiKhoan);
        return "redirect:/quan-ly-thanh-toan";
    }
}
