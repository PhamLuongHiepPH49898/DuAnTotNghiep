package com.example.datn.Controller;


import com.example.datn.Entity.TaiKhoanNganHang;
import com.example.datn.Repository.TaiKhoanNganHangRepository;
import com.example.datn.Service.BankCatalogService;
import com.example.datn.Service.TaiKhoanNganHangService;
import com.example.datn.Service.TaiKhoanService;
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
    private BankCatalogService bankCatalogService;

    @Autowired
    private TaiKhoanNganHangService service;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping
    public String hienThiTrangThanhToan(Model model) {
        List<TaiKhoanNganHang> list = taiKhoanRepo.findAll();
        model.addAttribute("dsTaiKhoan", list);
        model.addAttribute("banks", bankCatalogService.getBanks());

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "ThanhToan/QuanLyThanhToan";
    }

    @PostMapping("/them")
    public String themTaiKhoan(@RequestParam String bankCode,
                               @RequestParam String tenNganHang,
                               @RequestParam String soTaiKhoan,
                               @RequestParam String chuTaiKhoan,
                               @RequestParam(name="macDinh", defaultValue="false") boolean macDinh){
        service.taoMoi(bankCode, tenNganHang, soTaiKhoan, chuTaiKhoan, macDinh);
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
            model.addAttribute("banks", bankCatalogService.getBanks());
            return "ThanhToan/SuaTaiKhoan";
        } else {
            return "redirect:/quan-ly-thanh-toan";
        }
    }

    @PostMapping("/cap-nhat")
    public String capNhat(@RequestParam Integer id,
                          @RequestParam String bankCode,
                          @RequestParam String tenNganHang,
                          @RequestParam String soTaiKhoan,
                          @RequestParam String chuTaiKhoan,
                          @RequestParam(name="macDinh", defaultValue = "false") boolean macDinh) {
        service.capNhat(id, bankCode, tenNganHang, soTaiKhoan, chuTaiKhoan, macDinh);
        return "redirect:/quan-ly-thanh-toan";
    }
}
