package com.example.datn.Controller;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.SanBongService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SanBongController {
    private final SanBongService sanBongService;
    private final LoaiMatSanRepo loaiMatSanRepo;
    private final LoaiSanRepo loaiSanRepo;
    private final LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    private final TaiKhoanRepo taiKhoanRepo;

    public SanBongController(SanBongService sanBongService, LoaiMatSanRepo loaiMatSanRepo, LoaiSanRepo loaiSanRepo, LoaiMonTheThaoRepo loaiMonTheThaoRepo, TaiKhoanRepo taiKhoanRepo) {
        this.sanBongService = sanBongService;
        this.loaiMatSanRepo = loaiMatSanRepo;
        this.loaiSanRepo = loaiSanRepo;
        this.loaiMonTheThaoRepo = loaiMonTheThaoRepo;
        this.taiKhoanRepo = taiKhoanRepo;
    }

    @GetMapping("/")
    public String homeRedirect() {
        return "redirect:/trangchu";
    }

    // ✅ Trang chủ chính
    @GetMapping("/trangchu")
    public String trangchu(Model model) {
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        return "/Main/TrangChu";
    }

    // ✅ Trang tìm kiếm riêng
    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(required = false) Integer loaiSan,
                          @RequestParam(required = false) Integer monTheThao) {
        List<SanBong> ketQua = sanBongService.timKiemSan(loaiSan, monTheThao);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        return "/Main/TimKiem"; // 👉 trang riêng biệt
    }

    // ✅ Trang chi tiết riêng
    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        model.addAttribute("sanBongChiTiet", san);
        return "/Main/ChiTietSan"; // 👉 trang riêng biệt
    }


}
