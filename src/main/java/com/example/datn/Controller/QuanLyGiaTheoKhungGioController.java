package com.example.datn.Controller;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Service.GiaTheoKhungGioService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class QuanLyGiaTheoKhungGioController {

    @Autowired
    private GiaTheoKhungGioService giaTheoKhungGioService;
    @Autowired
    private SanBongService sanBongService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private KhungGioRepo khungGioRepo;


    @GetMapping("/quan-ly-gia-theo-khung-gio")
    public String quanLyGiaTheoKhungGio(Model model) {
        List<GiaTheoKhungGio> list = giaTheoKhungGioService.getGiaTheoKhungGio();
        model.addAttribute("dsGia", list);
        model.addAttribute("dsSanBong", sanBongService.getSanBong());
        model.addAttribute("dsKhungGio", khungGioRepo.findAll());
        model.addAttribute("gia", new GiaTheoKhungGio());


        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyGia/QuanLyGiaTheoKhungGio";
    }

    @GetMapping("/xoa-gia/{id}")
    public String xoaGia(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            giaTheoKhungGioService.xoa(id);
            redirectAttributes.addFlashAttribute("success", "Xóa giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa giá thất bại!");
        }
        return "redirect:/quan-ly-gia-theo-khung-gio";
    }

    @PostMapping("/sua-gia")
    public String suaGia(@RequestParam("id") int id, @RequestParam("giaThue") BigDecimal giaThue, RedirectAttributes redirectAttributes) {
        try {
            giaTheoKhungGioService.sua(id, giaThue);
            redirectAttributes.addFlashAttribute("success", "Sửa giá thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sửa giá thất bại!");
        }
        return "redirect:/quan-ly-gia-theo-khung-gio";
    }

    @PostMapping("/them-gia")
    public String themGia(@RequestParam("giaThue") BigDecimal giaThue,
                          @RequestParam("idSanBong") int idSanBong,
                          @RequestParam("idKhungGio") int idKhungGio, Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            giaTheoKhungGioService.them(giaThue, idSanBong, idKhungGio);
            redirectAttributes.addFlashAttribute("success", "Thêm giá thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm giá thất bại!");
        }
        return "redirect:/quan-ly-gia-theo-khung-gio";
    }

    @GetMapping("/quan-ly-gia-theo-khung-gio/tim-kiem")
    public String timKiem(@RequestParam(required = false) Integer idSanBong,
                          @RequestParam(required = false) Integer idKhungGio,
                          Model model) {

        List<GiaTheoKhungGio> dsGia = giaTheoKhungGioService.timKiem(idSanBong, idKhungGio);

        model.addAttribute("dsGia", dsGia);
        model.addAttribute("dsSanBong", sanBongService.getSanBong());
        model.addAttribute("dsKhungGio", khungGioRepo.findAll());

        // Để giữ lại lựa chọn người dùng
        model.addAttribute("idSanBong", idSanBong);
        model.addAttribute("idKhungGio", idKhungGio);

        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyGia/QuanLyGiaTheoKhungGio";
    }



}
