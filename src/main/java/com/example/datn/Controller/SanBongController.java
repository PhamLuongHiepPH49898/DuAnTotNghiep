package com.example.datn.Controller;

import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.LoaiMatSanRepo;
import com.example.datn.Repository.LoaiMonTheThaoRepo;
import com.example.datn.Repository.LoaiSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.SanBongService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu sai");
        }
        return "Main/Login";
    }
    @GetMapping("/logout")
    public String logoutPage(Model model) {
        return "Main/TrangChu";
    }
    @GetMapping("/dang-ky")
    public String dangKy(){
        return "Main/DangKi";
    }


    // ✅ Trang chủ chính
    @GetMapping("/trangchu")
    public String trangchu(Model model) {

        return "/Main/TrangChu";
    }
    @GetMapping("/user/trang-chu")
    public String trangChu_nguoiDung(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged-in user: " + auth.getName());
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        System.out.println("Logged-in user: " + auth.getName());
        return "/Main/TrangChu_NguoiDung";
    }
    @GetMapping("/admin/trang-chu")
    public String trangChu_QuanTri(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Logged-in user: " + auth.getName());
        model.addAttribute("username", auth.getName());
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("danhSachSan", danhSachSan);
        populateModel(model);
        System.out.println("Logged-in user: " + auth.getName());
        return "/Main/TrangChu_QuanTri";
    }

    // ✅ Trang tìm kiếm riêng
    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(required = false) Long loaiSan,
                          @RequestParam(required = false) Long monTheThao) {
        if (keyword != null) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        }
        List<SanBong> ketQua = sanBongService.timKiemSan(keyword, loaiSan, monTheThao);
        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("khongCoKetQua", ketQua.isEmpty());
        populateModel(model);
        return "/Main/TimKiem"; // 👉 trang riêng biệt
    }
    // ✅ Trang chi tiết riêng
    @GetMapping("/chi-tiet/{id}")
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        if (san == null) {
            return "redirect:/trangchu"; // Chuyển hướng về trang chủ nếu không tìm thấy
        }
        model.addAttribute("sanBongChiTiet", san);
        return "/Main/ChiTietSan"; // 👉 trang riêng biệt
    }

    @GetMapping("quan-ly-san")
    public String QuanLySan(Model model) {
        List<SanBong> danhSachSan = sanBongService.getSanBong();
        model.addAttribute("danhSachSan", danhSachSan);
        return "/san/QuanLySan";
    }
    @GetMapping("form-them-san-bong")
    public String formThem(Model model) {
        model.addAttribute("sanBong", new SanBong());
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
        return "san/ThemSan";
    }

    @PostMapping("/them-san-bong")
    public String themSanBong(@Valid @ModelAttribute("sanBong") SanBong sanBong, BindingResult bindingResult, Model model, @RequestParam(value = "hinhAnh", required = false) MultipartFile hinhAnh) throws IOException {

        // Nếu có ảnh thì lưu ảnh
        if (hinhAnh != null && !hinhAnh.isEmpty()) {
            String tenFile = UUID.randomUUID() + "_" + hinhAnh.getOriginalFilename();
            String path = "uploads/" + tenFile;
            File file = new File(path);
            file.getParentFile().mkdirs();
            hinhAnh.transferTo(file);

            sanBong.setHinh_anh(tenFile);
        }


        sanBongService.them(sanBong);
        return "redirect:/quan-ly-san";
    }

    @GetMapping("/xoa-san-bong/{id}")
    public String xoa(@PathVariable int id) {
        sanBongService.xoa(id);
        return "redirect:/quan-ly-san";
    }
    @GetMapping("/sua-san-bong/{id}")
    public String viewSua(@PathVariable("id") int id, Model model) {
        model.addAttribute("sanBong", sanBongService.findById(id));
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
        return "/san/SuaSan";
    }
    @PostMapping("/sua-san-bong/{id}")
    public String suaSanBong(@Valid @ModelAttribute SanBong sanBongMoi,
                             @PathVariable("id") int id,
                             BindingResult bindingResult,
                             Model model) throws IOException {

        SanBong sanBongGoc = sanBongService.findById(id);
        if (sanBongGoc == null) {
            return "redirect:/quan-ly-san";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("dsLoaiMonTheThao", loaiMonTheThaoRepo.findAll());
            model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
            model.addAttribute("dsLoaiMatSan", loaiMatSanRepo.findAll());
            model.addAttribute("sanBong", sanBongMoi);
            return "san/SuaSan";
        }

        // Cập nhật các trường thông tin khác
        sanBongGoc.setTen_san_bong(sanBongMoi.getTen_san_bong());
        sanBongGoc.setGia(sanBongMoi.getGia());
        sanBongGoc.setMo_ta(sanBongMoi.getMo_ta());
        sanBongGoc.setTrang_thai(sanBongMoi.getTrang_thai());
        sanBongGoc.setLoaiMatSan(sanBongMoi.getLoaiMatSan());
        sanBongGoc.setLoaiMonTheThao(sanBongMoi.getLoaiMonTheThao());
        sanBongGoc.setLoaiSan(sanBongMoi.getLoaiSan());

        MultipartFile file = sanBongMoi.getFile();
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadPath = Paths.get("uploads");

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            sanBongGoc.setHinh_anh(fileName); // Chỉ lưu tên file
        }



        sanBongService.sua(sanBongGoc);
        return "redirect:/quan-ly-san";
    }

    // Phương thức tiện ích để thêm các thuộc tính chung vào model
    private void populateModel(Model model) {
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
    }

}
