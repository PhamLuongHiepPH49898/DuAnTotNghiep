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

    @GetMapping("/trangchu")
    public String trangchu(Model model) {
        List<SanBong> danhSachSan = sanBongService.findAll();
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("danhSachSan", danhSachSan);
        return "/Main/TrangChu";
    }
    @GetMapping("/tim-kiem")
    public String timKiem(Model model,
                          @RequestParam(required = false) Integer loaiSan,
                          @RequestParam(required = false) Integer monTheThao) {
        List<SanBong> ketQua = sanBongService.timKiemSan(loaiSan, monTheThao);

        model.addAttribute("danhSachSan", ketQua);
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        return "/Main/TrangChu";
    }

    // 📌 Chi tiết sân
    public String chiTietSan(@PathVariable("id") int id, Model model) {
        SanBong san = sanBongService.findById(id);
        model.addAttribute("sanBongChiTiet", san);

        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        return "/Main/TrangChu";
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






}
