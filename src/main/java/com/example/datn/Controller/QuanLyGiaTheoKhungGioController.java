package com.example.datn.Controller;

import com.example.datn.Entity.GiaTheoKhungGio;
import com.example.datn.Entity.SanBong;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.KhungGioRepo;
import com.example.datn.Service.GiaTheoKhungGioService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;


@Controller
public class QuanLyGiaTheoKhungGioController {

    @Autowired
    private GiaTheoKhungGioService giaTheoKhungGioService;
    @Autowired
    private GiaTheoKhungGioRepo giaTheoKhungGioRepo;
    @Autowired
    private SanBongService sanBongService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private KhungGioRepo khungGioRepo;


    @GetMapping("/quan-ly-gia-theo-khung-gio")
    public String quanLyGiaTheoKhungGio(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        Model model) {

        List<GiaTheoKhungGio> dsGia = giaTheoKhungGioService.getGiaTheoKhungGio();
        Page<SanBong> dsSanBong = sanBongService.getSanBongPage(page, size);

        // Map<sânId, Map<khungGioId, Gia>>
        Map<Integer, Map<Integer, GiaTheoKhungGio>> bangGia = new HashMap<>();
        for (GiaTheoKhungGio g : dsGia) {
            bangGia
                    .computeIfAbsent(g.getSanBong().getId_san_bong(), k -> new HashMap<>())
                    .put(g.getKhungGio().getId(), g);
        }

        Map<Integer, List<GiaTheoKhungGio>> bangGiaTheoSan = new HashMap<>();
        for (SanBong san : dsSanBong) {
            List<GiaTheoKhungGio> giaList = giaTheoKhungGioRepo.findBySanBongId(san.getId_san_bong());
            giaList.removeIf(Objects::isNull);
            bangGiaTheoSan.put(san.getId_san_bong(), giaList);
        }


        model.addAttribute("dsSanBong", sanBongService.getSanBong());
        model.addAttribute("dsSanBongHienThi", dsSanBong);
        model.addAttribute("dsKhungGio", khungGioRepo.findAll());
        model.addAttribute("bangGia", bangGia);
        model.addAttribute("bangGiaTheoSan", bangGiaTheoSan);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", dsSanBong.getTotalPages());

        model.addAttribute("isTimKiem", false);


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


    @PostMapping("/quan-ly-gia-theo-khung-gio/sua-gia")
    public String suaGia(@RequestParam("ids") List<Integer> ids,
                         @RequestParam("giaThues") List<Double> giaThues, RedirectAttributes redirectAttributes) {
        try {
            for (int i = 0; i < ids.size(); i++) {
                int id = ids.get(i);
                Double giaMoi = giaThues.get(i);
                giaTheoKhungGioService.sua(id, giaMoi);
                redirectAttributes.addFlashAttribute("success", "Sửa giá thành công!");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Sửa giá thất bại!");
        }
        return "redirect:/quan-ly-gia-theo-khung-gio";
    }


    @PostMapping("/quan-ly-gia-theo-khung-gio/them-gia")
    public String themGia(@RequestParam("giaThue") Double giaThue,
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
    public String timKiem(@RequestParam(required = false) Integer sanBong,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          Model model) {

        List<GiaTheoKhungGio> dsGia = giaTheoKhungGioService.timKiem(sanBong);

        Map<Integer, Map<Integer, GiaTheoKhungGio>> bangGia = new HashMap<>();
        for (GiaTheoKhungGio g : dsGia) {
            bangGia
                    .computeIfAbsent(g.getSanBong().getId_san_bong(), k -> new HashMap<>())
                    .put(g.getKhungGio().getId(), g);
        }

        Map<Integer, List<GiaTheoKhungGio>> bangGiaTheoSan = new HashMap<>();

        if (sanBong != null) {
            // Tìm kiếm 1 sân cụ thể → không phân trang
            SanBong sb = sanBongService.findById(sanBong);
            if (sb != null) {
                model.addAttribute("dsSanBongHienThi", List.of(sb));
                List<GiaTheoKhungGio> giaList = giaTheoKhungGioRepo.findBySanBongId(sb.getId_san_bong());
                bangGiaTheoSan.put(sb.getId_san_bong(), giaList);
            }

            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 1);

        } else {
            // Tìm tất cả sân → phân trang theo SanBong
            Page<SanBong> pageSan = sanBongService.getSanBongPage(page, size);
            List<SanBong> dsSanBongHienThi = pageSan.getContent();
            for (SanBong san : dsSanBongHienThi) {
                List<GiaTheoKhungGio> giaList = giaTheoKhungGioRepo.findBySanBongId(san.getId_san_bong());
                bangGiaTheoSan.put(san.getId_san_bong(), giaList);
            }
            model.addAttribute("dsSanBongHienThi", dsSanBongHienThi);
            model.addAttribute("currentPage", pageSan.getNumber());
            model.addAttribute("totalPages", pageSan.getTotalPages());

        }

        model.addAttribute("bangGia", bangGia);
        model.addAttribute("bangGiaTheoSan", bangGiaTheoSan);
        model.addAttribute("dsSanBong", sanBongService.getSanBong());
        model.addAttribute("dsKhungGio", khungGioRepo.findAll());
        model.addAttribute("sanBong", sanBong);

        model.addAttribute("khongCoKetQua", dsGia.isEmpty());
        model.addAttribute("isTimKiem", true);


        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyGia/QuanLyGiaTheoKhungGio";
    }


}
