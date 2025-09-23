package com.example.datn.Controller;

import com.example.datn.Entity.*;
import com.example.datn.Repository.*;
import com.example.datn.Service.HoanTienService;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class QuanLyDatSanController {

    @Autowired
    private LichDatSanService lichDatSanService;
    @Autowired
    private SanBongService sanBongService;
    @Autowired
    private LoaiSanRepo loaiSanRepo;
    @Autowired
    private LoaiMatSanRepo loaiMatSanRepo;
    @Autowired
    private LoaiMonTheThaoRepo loaiMonTheThaoRepo;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private LichDatSanRepo lichDatSanRepo;
    @Autowired
    private HoanTienRepo hoanTienRepo;
    @Autowired
    private HoanTienService hoanTienService;



    @GetMapping("/duyet-huy-lich")
    public String quanLyDatSan(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDat,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {


        if (ngayDat == null) {
            ngayDat = LocalDate.now();
        }
        Page<LichDatSan> lichDatList = lichDatSanService.getLichDatSan(ngayDat, page, size);

        model.addAttribute("danhSachLichDatSan", lichDatList);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", lichDatList.getTotalPages());
        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("ngayDat", ngayDat);
        model.addAttribute("khongCoKetQua", lichDatList.isEmpty());


        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);

        return "QuanLyDatSan/DuyetHuyDatLich";
    }

    @GetMapping("/quan-ly-lich-dat")
    public String hienThiLich(
            Model model,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayDat,
            @RequestParam(required = false) String tenSan,
            @RequestParam(required = false) Integer loaiSanId,
            @RequestParam(required = false) Integer matSanId,
            @RequestParam(required = false) Integer monTheThaoId
    ) {
        if (ngayDat == null) {
            ngayDat = LocalDate.now();
        }if (tenSan != null) {
            tenSan = tenSan.trim();
        }


        List<SanBong> danhSachSanLoc = sanBongService.timKiemSan(tenSan, loaiSanId, matSanId, monTheThaoId);
        Map<SanBong, List<LichDatSan>> lichDatMap = lichDatSanService.getLichDatSanTheoNgay(ngayDat, danhSachSanLoc);
        List<KhungGio> khungGios = lichDatSanService.getAllKhungGio();


        boolean khongCoKetQua = lichDatMap == null || lichDatMap.isEmpty()
                                || lichDatMap.values().stream().allMatch(List::isEmpty);


        model.addAttribute("ngayDuocChon", ngayDat);
        model.addAttribute("lichDatMap", lichDatMap);
        model.addAttribute("dsKhungGio", khungGios);

        model.addAttribute("tenSan", tenSan);
        model.addAttribute("loaiSanId", loaiSanId);
        model.addAttribute("matSanId", matSanId);
        model.addAttribute("monTheThaoId", monTheThaoId);

        model.addAttribute("dsLoaiSan", loaiSanRepo.findAll());
        model.addAttribute("dsMatSan", loaiMatSanRepo.findAll());
        model.addAttribute("dsMonTheThao", loaiMonTheThaoRepo.findAll());
        model.addAttribute("thoiGianHienTai", LocalDateTime.now());


        model.addAttribute("khongCoKetQua", khongCoKetQua);


        String hoTen = taiKhoanService.getHoTenDangNhap();
        model.addAttribute("hoTen", hoTen);


        return "QuanLyDatSan/QuanLyLichDat";
    }

    @GetMapping("/duyet/{id}")
    public String duyet(@PathVariable int id, RedirectAttributes redirectAttributes) {

        try {
            lichDatSanService.duyet(id);
            redirectAttributes.addFlashAttribute("success", "Đã duyệt lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Duyệt lịch thất bại!");
        }
        return "redirect:/duyet-huy-lich";
    }

    @PostMapping("/huy")
    public String huy(@RequestParam("id") int id,
                      @RequestParam("ghiChu") String ghiChu,
                      @RequestParam("hoanTien") Integer hoanTien,
                      RedirectAttributes redirectAttributes) {
        try {
            LichDatSan lich = lichDatSanRepo.findById(id).orElseThrow();
            lichDatSanService.huy(id, ghiChu);
            if (hoanTien == 1  && lich.getThanhToan().getTrangThai() == 1) {
                hoanTienService.taoHoanTienAdmin(lich);
            }
            redirectAttributes.addFlashAttribute("success", "Đã hủy lịch thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hủy lịch thất bại!");
        }
        return "redirect:/duyet-huy-lich";
    }


    @GetMapping("/duyet-huy-lich/tim-kiem")
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
            keyword = keyword.trim();
        }

        Page<LichDatSan> danhSachLichDatSan = lichDatSanService.timKiem(keyword, ngayDat, sanBong, trangThai, page, size);
        model.addAttribute("danhSachLichDatSan", danhSachLichDatSan);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", danhSachLichDatSan.getTotalPages());
        model.addAttribute("khongCoKetQua", danhSachLichDatSan.isEmpty());
        model.addAttribute("danhSachSan", sanBongService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("ngayDat", ngayDat);
        model.addAttribute("sanBong", sanBong);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("khongCoKetQua", danhSachLichDatSan.isEmpty());

        return "QuanLyDatSan/DuyetHuyDatLich";
    }

}
