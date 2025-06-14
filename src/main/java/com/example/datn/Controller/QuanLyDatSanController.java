package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @GetMapping("/quan-ly-dat-san")
    public String quanLyDatSan(
            @RequestParam(name = "ngayTao", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayTao,
            Model model) {

        List<LichDatSan> lichDatList;

        if (ngayTao == null) {
            // Không truyền ngày → hiển thị tất cả
            lichDatList = lichDatSanService.getLichDatSan();
        } else {
            // Có ngày → lọc theo ngày
            LocalDateTime startDateTime = ngayTao.atStartOfDay();
            LocalDateTime endDateTime = ngayTao.plusDays(1).atStartOfDay();
            lichDatList = lichDatSanRepo.findByNgayTaoBetween(startDateTime, endDateTime);
        }

        model.addAttribute("danhSachLichDatSan", lichDatList);
        model.addAttribute("ngayTao", ngayTao);
        model.addAttribute("isTimKiem", false); // đánh dấu đây không phải tìm kiếm

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyDatSan/QuanLyDatSan";
    }

    @GetMapping("/duyet/{id}")
    public String duyet(@PathVariable int id) {
        lichDatSanService.duyet(id);
        return "redirect:/quan-ly-dat-san";
    }

    @GetMapping("/huy/{id}")
    public String huy(@PathVariable int id) {
        lichDatSanService.huy(id);
        return "redirect:/quan-ly-dat-san";
    }

    @GetMapping("/quan-ly-dat-san/tim-kiem")
    public String quanLyDatSanTimKiem(Model model,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDat,
                                      @RequestParam(required = false) Integer sanBong,
                                      @RequestParam(required = false) Integer trangThai) {

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        if (keyword != null) {
            keyword = keyword.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        }


        List<LichDatSan> danhSachLichDatSan = lichDatSanService.timKiem(keyword, ngayDat, sanBong, trangThai);
        model.addAttribute("danhSachLichDatSan", danhSachLichDatSan);
        model.addAttribute("khongCoKetQua", danhSachLichDatSan.isEmpty());
        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("isTimKiem", true); //  đánh dấu đây là tìm kiếm

        return "QuanLyDatSan/QuanLyDatSan";
    }


}
