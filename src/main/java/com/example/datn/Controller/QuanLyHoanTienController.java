package com.example.datn.Controller;

import com.example.datn.Entity.HoanTien;
import com.example.datn.Service.HoanTienService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;


@Controller
public class QuanLyHoanTienController {
    @Autowired
    private HoanTienService hoanTienService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private SanBongService sanBongService;

    @GetMapping("/quan-ly-hoan-tien")
    public String quanLyHoanTien(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayHuy,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {

        if (ngayHuy == null) {
            ngayHuy = LocalDate.now();
        }

        Page<HoanTien> dsHoanTien = hoanTienService.getAllHoanTien(ngayHuy, page, size);
        model.addAttribute("dsHoanTien", dsHoanTien);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("ngayHuy", ngayHuy);
        model.addAttribute("totalPages", dsHoanTien.getTotalPages());
        model.addAttribute("isTimKiem", false);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);


        return "QuanLyHoanTien/QuanLyHoanTien";
    }

    @GetMapping("/quan-ly-hoan-tien/duyet/{id}")
    public String duyet(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            hoanTienService.duyet(id);
            redirectAttributes.addFlashAttribute("success", "Đã duyệt thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Duyệt thất bại!");
        }
        return "redirect:/quan-ly-hoan-tien";
    }

    @GetMapping("/quan-ly-hoan-tien/tu-choi/{id}")
    public String tuChoi(@PathVariable int id, RedirectAttributes redirectAttributes) {

        try {
            hoanTienService.tuChoi(id);
            redirectAttributes.addFlashAttribute("success", "Đã từ chối thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Từ chối thất bại!");
        }
        return "redirect:/quan-ly-hoan-tien";
    }

    @GetMapping("/quan-ly-hoan-tien/tim-kiem")
    public String timKiem(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayHuy,
                          @RequestParam(required = false) String tenNguoiDat,
                          @RequestParam(required = false) String soDienThoai,
                          @RequestParam(required = false) Integer sanBongId,
                          @RequestParam(required = false) Integer trangThai,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        if (tenNguoiDat != null) {
            tenNguoiDat = tenNguoiDat.trim();
        }
        if (soDienThoai != null) {
            soDienThoai = soDienThoai.replaceAll("[^0-9]", "").trim();
        }

        Page<HoanTien> dsHoanTien = hoanTienService.timKiem(tenNguoiDat, ngayHuy, soDienThoai, sanBongId, trangThai, page, size);
        model.addAttribute("dsHoanTien", dsHoanTien);
        model.addAttribute("dsSanBong", sanBongService.getSanBong());
        model.addAttribute("tenNguoiDat", tenNguoiDat);
        model.addAttribute("soDienThoai", soDienThoai);
        model.addAttribute("sanBongId", sanBongId);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("currentPage", page);
        model.addAttribute("ngayHuy", ngayHuy);
        model.addAttribute("totalPages", dsHoanTien.getTotalPages());
        model.addAttribute("khongCoKetQua", dsHoanTien.isEmpty());
        model.addAttribute("isTimKiem", true);

        return "QuanLyHoanTien/QuanLyHoanTien";
    }

}
