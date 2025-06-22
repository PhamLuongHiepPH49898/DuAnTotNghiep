
package com.example.datn.Controller;

import com.example.datn.DTO.ChiTietDatLichDTO;
import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.*;
import com.example.datn.Repository.LichDatSanRepo;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.DatSanService;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.XacNhanDatLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class DatLichController {
    @Autowired
    private TaiKhoanRepo taiKhoanRepo;
    @Autowired
    private LichDatSanService lichDatSan;
    @Autowired
    private DatSanService datSanService;
    @Autowired
    private XacNhanDatLichService xacNhanDatLichService;

    @GetMapping("/datsan")
    public String hienThiTrangDatSan(@RequestParam("id") Integer idSan, Model model) {
        SanBong san = datSanService.laySanTheoId(idSan);  // <-- Lấy 1 sân
        if (san == null) return "redirect:/trangchu";

        List<SanBong> sanList = datSanService.layDanhSachSan();
        List<KhungGio> khungGioList = datSanService.layDanhSachKhungGio();
        List<GiaTheoKhungGio> danhSachGiaTheoKhungGio = datSanService.layDanhGiaTheoKhungGio(); // lấy danh sách giá theo khung giờ
        List<String> cacSlotDaDat = datSanService.getAllSlotKeys();

        // Map key = "idSan_idKhungGio" -> Giá thuê
        Map<String, BigDecimal> bangGia = new HashMap<>();
        for (GiaTheoKhungGio gia : danhSachGiaTheoKhungGio) {
            String key = gia.getSanBong().getId_san_bong() + "_" + gia.getKhungGio().getId();
            bangGia.put(key, gia.getGiaThue());
        }

        // Map key = "idSan_idKhungGio" -> ID bảng giá
        Map<String, Integer> bangGiaId = new HashMap<>();
        for (GiaTheoKhungGio gia : danhSachGiaTheoKhungGio) {
            String key = gia.getSanBong().getId_san_bong() + "_" + gia.getKhungGio().getId();
            bangGiaId.put(key, gia.getIdGiaTheoKhungGio());
        }

        model.addAttribute("danhSachSan", List.of(san));
        model.addAttribute("danhSachKhungGio", khungGioList);
        model.addAttribute("bangGia", bangGia);
        model.addAttribute("bangGiaId", bangGiaId);
        model.addAttribute("cacSlotDaDat", cacSlotDaDat);

        System.out.println("San: " + sanList.size());
        System.out.println("KhungGio: " + khungGioList.size());
        System.out.println("BangGia: " + bangGia.size());
        System.out.println("BangGiaId: " + bangGiaId.size());

        return "Main/DatLich";
    }

    @GetMapping("/xacnhan")
    public String hienThiFormXacNhan(Model model, Principal principal) {
        String email = principal.getName(); // Lấy email từ người dùng đang đăng nhập
        Optional<TaiKhoan> taiKhoan = taiKhoanRepo.findByEmail(email);

        if (taiKhoan.isPresent()) {
            XacNhanDatLichDTO xacNhan = new XacNhanDatLichDTO();
            xacNhan.setHoTen(taiKhoan.get().getHo_ten());
            xacNhan.setSoDienThoai(taiKhoan.get().getSo_dien_thoai());
            xacNhan.setEmail(taiKhoan.get().getEmail());

            // Gán vào model đúng cách
            model.addAttribute("danhSachLichDatSan", lichDatSan.getLichDatSan());
            model.addAttribute("xacNhan", xacNhan);
        } else {
            return "redirect:/login?error=notfound";
        }
        return "Main/XacNhanDatLich";
    }

    @GetMapping("/datLichThanhCong")
    public String hienThiTrangDatLichThanhCong() {
        return "/Main/Success";
    }

    @PostMapping("/datLichThanhCong")
    public String luuDatLich(@ModelAttribute XacNhanDatLichDTO xacNhan, Model model, Principal principal) {
        List<ChiTietDatLichDTO> danhSachChiTiet = xacNhan.getChiTietDatLichList();

        // In log kiểm tra
        for (ChiTietDatLichDTO chiTiet : danhSachChiTiet) {
            System.out.println("Ngày: " + chiTiet.getNgayDat());
            System.out.println("Giờ: " + chiTiet.getThoiGian());
            System.out.println("Sân: " + chiTiet.getTenSan());
            System.out.println("Giá: " + chiTiet.getGia());
            System.out.println("ID Giá Thuê: " + chiTiet.getIdGiaTheoKhungGio());
        }

        // Gọi service để lưu lịch và lấy ra danh sách ID đã lưu
        List<Integer> idLichDatDuocLuu = xacNhanDatLichService.luuDatLich(xacNhan);

        if (!idLichDatDuocLuu.isEmpty()) {
            // Chuyển sang trang giả lập thanh toán với lịch đầu tiên
            return "redirect:/gia-lap-thanh-toan?idLichDatSan=" + idLichDatDuocLuu.get(0);
        }

        // Fallback nếu không có gì được lưu
        return "redirect:/datLichThanhCong";
    }
}
