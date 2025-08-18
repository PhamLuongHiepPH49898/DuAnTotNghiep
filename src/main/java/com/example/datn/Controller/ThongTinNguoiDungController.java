package com.example.datn.Controller;

import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Repository.GiaTheoKhungGioRepo;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.SanBongRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.ThongTinNguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.stream.Collectors;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ThongTinNguoiDungController {

    @Autowired
    private TaiKhoanRepo taiKhoanRepo;

    @Autowired
    private ThongTinNguoiDungService thongTinNguoiDungService;

    @Autowired
    private LichDatSanRepo lichDatSanRepo;

    @GetMapping("/thong-tin-nguoi-dung")
    public String hienThiThongTinNguoiDung(Model model,
                                           Principal principal,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(required = false) String keyword) {
        String email = principal.getName();
        Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepo.findByEmail(email);
        if (optionalTaiKhoan.isEmpty()) {
            return "redirect:/login?error=notfound";
        }

        TaiKhoan taiKhoan = optionalTaiKhoan.get();
        Pageable pageable = PageRequest.of(page, 6, Sort.by("ngayDat").descending());
        Page<LichDatSan> lichSuPage = thongTinNguoiDungService.layLichSuDatSan((long) taiKhoan.getId(), pageable);




        List<LichDatSan> lichHomNay = thongTinNguoiDungService.timLichDatHomNay((long) taiKhoan.getId());
        boolean coLichHomNay = lichHomNay.stream().anyMatch(l -> l.getTrangThai() == 1);
        model.addAttribute("lichChoXacNhan", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 0).collect(Collectors.toList()));

        model.addAttribute("lichDaXacNhan", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 1).collect(Collectors.toList()));

        model.addAttribute("lichDaHuy", lichSuPage.getContent().stream()
                .filter(l -> l.getTrangThai() == 2).collect(Collectors.toList()));
        model.addAttribute("coLichHomNay", coLichHomNay);
        model.addAttribute("taiKhoan", taiKhoan);
        model.addAttribute("lichSuPage", lichSuPage);
        model.addAttribute("dateFormatter", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return "ThongTinND/TTND";
    }
    @GetMapping("/lich-dat/huy")
    public String showHuyForm(@RequestParam("id") Integer id, Model model) {
        Optional<LichDatSan> lich = lichDatSanRepo.findById(id);
        if (lich.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy lịch đặt.");
            return "ThongTinND/Loi";
        }
        model.addAttribute("lich", lich.get());
        return "ThongTinND/HuyLich"; // Trang chứa form/modal huỷ
    }
    @PostMapping("/lich-dat/huy")
    public String handleHuy(@RequestParam("id") Integer id,
                            @RequestParam("ghiChu") String ghiChu,
                            Model model) {
        System.out.println("Huỷ lịch ID: " + id + " - Ghi chú: " + ghiChu);
        try {
            thongTinNguoiDungService.huyLichDat(id, ghiChu);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            Optional<LichDatSan> lich = lichDatSanRepo.findById(id);
            lich.ifPresent(l -> model.addAttribute("lich", l));
            return "ThongTinND/HuyLich";
        }
        return "redirect:/thong-tin-nguoi-dung";
    }










/*    @GetMapping("/lich-dat/sua")
    public String hienFormSua(@RequestParam("id") Integer id, Model model) {
        LichDatSan lich = lichDatSanRepo.findById(id).orElse(null);
        List<SanBong> dsSan = sanBongRepo.findSanBongById();
        List<LichDatSan> lichConTrong = lichDatSanRepo.findLichConTrongTheoNgay(lich.getNgayDat());
        List<GiaTheoKhungGio> dsLoc = lichConTrong.stream()
                .map(LichDatSan::getGiaTheoKhungGio)
                .distinct()
                .toList();

        model.addAttribute("lichDatSan", lich);
        model.addAttribute("dsSan", dsSan);
        model.addAttribute("dsKhungGio", dsLoc);
        // Có thể thêm dữ liệu khác như danh sách khung giờ/sân bóng nếu cần chọn lại
        return "ThongTinND/SuaLich"; // Tên file form bạn sẽ tạo
    }
    @PostMapping("/lich-dat/cap-nhat")
    public String capNhatLich(@ModelAttribute("lichDatSan") LichDatSan lichMoi) {
        thongTinNguoiDungService.suaLichDat(lichMoi);
        return "redirect:/thong-tin-nguoi-dung";
    }*/
}
