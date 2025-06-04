package com.example.datn.Controller;

import com.example.datn.DTO.LichDatSanDTO;
import com.example.datn.Entity.*;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class SanBongController {

    private final LoaiMatSanRepo loaiMatSanRepo;
    private final LoaiSanRepo loaiSanRepo;
    private final LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    private final TaiKhoanRepo taiKhoanRepo;
    @Autowired
    private SanBongService sanBongService;

    @Autowired
    private DatSanService datSanService;
    public SanBongController(SanBongService sanBongService, LoaiMatSanRepo loaiMatSanRepo, LoaiSanRepo loaiSanRepo, LoaiMonTheThaoRepo loaiMonTheThaoRepo, TaiKhoanRepo taiKhoanRepo) {
        this.sanBongService = sanBongService;
        this.loaiMatSanRepo = loaiMatSanRepo;
        this.loaiSanRepo = loaiSanRepo;
        this.loaiMonTheThaoRepo = loaiMonTheThaoRepo;
        this.taiKhoanRepo = taiKhoanRepo;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "Main/Login"; // ✅ Trả về trang login từ templates/Main/Login.html
    }

    @GetMapping("/trangchu")
    public String trangChu(Model model, HttpServletRequest request) {
        String username = getAuthenticatedUsername();
        model.addAttribute("username", username);
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        return "Main/TrangChuUser"; // Giữ nguyên TrangChuUser
    }

    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "loaiSan", required = false) Long loaiSan,
                          @RequestParam(value = "monTheThao", required = false) Long monTheThao) {
        keyword = sanitizeKeyword(keyword);
        List<SanBong> ketQua = sanBongService.timKiemSan(keyword, loaiSan, monTheThao);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());
        populateModel(model);
        return "Main/TimKiem";
    }

    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        if (san == null) return "redirect:/trangchu";
        model.addAttribute("sanBongChiTiet", san);
        return "Main/ChiTietSan";
    }

    private void populateModel(Model model) {
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
    }

    private String getAuthenticatedUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName()))
                ? auth.getName() : null;
    }

    private String sanitizeKeyword(String keyword) {
        return (keyword != null && !keyword.isBlank())
                ? keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim()
                : null;
    }

    @GetMapping("/datsan")
    public String hienThiTrangDatSan(Model model) {
        List<SanBong> sanList = datSanService.layDanhSachSan();
        List<KhungGio> khungGioList = datSanService.layDanhSachKhungGio();
        Map<String, BigDecimal> bangGia = datSanService.layBangGia();

        System.out.println("San: " + sanList.size());
        System.out.println("KhungGio: " + khungGioList.size());
        System.out.println("BangGia: " + bangGia.size());

        model.addAttribute("danhSachSan", sanList);
        model.addAttribute("danhSachKhungGio", khungGioList);
        model.addAttribute("bangGia", bangGia);
        return "Main/DatLich"; // Tên file HTML
    }


}
