package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class QuanLyDatSanController {

    @Autowired
    private LichDatSanService lichDatSanService;
    @Autowired
    private SanBongService sanBongService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/quan-ly-dat-san")
    public String quanLyDatSan(
            @RequestParam(name = "ngayTao", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayTao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<LichDatSan> lichDatList;

        if (ngayTao == null) {
            //hiển thị tất cả
            lichDatList = lichDatSanService.getLichDatSan(page, size);
        } else {
            // lọc theo ngày
            LocalDateTime startDateTime = ngayTao.atStartOfDay();
            LocalDateTime endDateTime = ngayTao.plusDays(1).atStartOfDay();
            lichDatList = lichDatSanService.timKiemTheoNgayTao(startDateTime, endDateTime, page, size);
        }

        model.addAttribute("danhSachLichDatSan", lichDatList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", lichDatList.getTotalPages());
        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("ngayTao", ngayTao);
        model.addAttribute("khongCoKetQua", lichDatList.isEmpty());
        model.addAttribute("isTimKiem", false);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyDatSan/QuanLyDatSan";
    }

    @GetMapping("/duyet/{id}")
    public String duyet(@PathVariable int id, RedirectAttributes redirectAttributes) {

        try {
            lichDatSanService.duyet(id);
            redirectAttributes.addFlashAttribute("success", "Đã duyệt lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Duyệt lịch thất bại!");
        }
        return "redirect:/quan-ly-dat-san";
    }

    @PostMapping("/huy")
    public String huy(@RequestParam("id") int id,
                      @RequestParam("ghiChu") String ghiChu,
                      RedirectAttributes redirectAttributes) {
        try {
            lichDatSanService.huy(id, ghiChu);
            redirectAttributes.addFlashAttribute("success", "Đã hủy lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hủy lịch thất bại!");
        }
        return "redirect:/quan-ly-dat-san";
    }


    @GetMapping("/quan-ly-dat-san/tim-kiem")
    public String quanLyDatSanTimKiem(Model model,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDat,
                                      @RequestParam(required = false) Integer sanBong,
                                      @RequestParam(required = false) Integer trangThai,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        if (keyword != null) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        }


        Page<LichDatSan> danhSachLichDatSan = lichDatSanService.timKiem(keyword, ngayDat, sanBong, trangThai, page, size);
        model.addAttribute("danhSachLichDatSan", danhSachLichDatSan);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", danhSachLichDatSan.getTotalPages());
        model.addAttribute("khongCoKetQua", danhSachLichDatSan.isEmpty());
        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sanBong", sanBong);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("khongCoKetQua", danhSachLichDatSan.isEmpty());
        model.addAttribute("isTimKiem", true);

        return "QuanLyDatSan/QuanLyDatSan";
    }


}
