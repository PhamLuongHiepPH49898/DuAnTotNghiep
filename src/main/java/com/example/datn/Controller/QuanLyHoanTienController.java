package com.example.datn.Controller;

import com.example.datn.Entity.HoanTien;
import com.example.datn.Service.HoanTienService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class QuanLyHoanTienController {
    @Autowired
    private HoanTienService hoanTienService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/quan-ly-hoan-tien")
    public String quanLyHoanTien(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<HoanTien> dsHoanTien = hoanTienService.getAllHoanTien(page, size);
        model.addAttribute("dsHoanTien", dsHoanTien);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", dsHoanTien.getTotalPages());
        model.addAttribute("isTimKiem", false);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);


        return "QuanLyHoanTien/quanLyHoanTien";
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

}
