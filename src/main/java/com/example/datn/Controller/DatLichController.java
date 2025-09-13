
package com.example.datn.Controller;

import com.example.datn.DTO.ChiTietDatLichDTO;
import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.*;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.DatSanService;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.XacNhanDatLichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

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
    public String hienThiTrangDatSan(
            @RequestParam("id") Integer idSan,
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            Model model
    ) {
        SanBong san = datSanService.laySanTheoId(idSan);
        if (san == null) return "redirect:/trang-chu";

        // ✅ Tính ngày bắt đầu của tuần dựa trên offset
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.plusWeeks(offset).with(DayOfWeek.MONDAY);
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        // ✅ Các dữ liệu như cũ
        List<KhungGio> khungGioList = datSanService.layDanhSachKhungGio();
        List<GiaTheoKhungGio> dsGiaTheoKhung = datSanService.layDanhGiaTheoKhungGio();
        List<String> cacSlotDaDat = datSanService.getAllSlotKeys();
        List<String> cacSlotTonTai = datSanService.getAllSlotKeysTonTai();

        // ✅ Map giá thuê
        Map<String, BigDecimal> bangGia = new HashMap<>();
        Map<String, Integer> bangGiaId = new HashMap<>();
        for (GiaTheoKhungGio gia : dsGiaTheoKhung) {
            String key = gia.getSanBong().getId_san_bong() + "_" + gia.getKhungGio().getId();
            System.out.println("Key: " + key + ", Giá: " + gia.getGiaThue());
            bangGia.put(key, BigDecimal.valueOf(gia.getGiaThue()));
            bangGiaId.put(key, gia.getIdGiaTheoKhungGio());
        }


        // ✅ Truyền thêm danh sách 7 ngày vào model
        model.addAttribute("danhSachSan", List.of(san));
        model.addAttribute("danhSachKhungGio", khungGioList);
        model.addAttribute("bangGia", bangGia);
        model.addAttribute("bangGiaId", bangGiaId);
        model.addAttribute("cacSlotDaDat", cacSlotDaDat);
        model.addAttribute("cacSlotTonTai", cacSlotTonTai);
        model.addAttribute("weekDates", weekDates);
        model.addAttribute("offset", offset); // để JS biết đang ở tuần nào

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
            model.addAttribute("xacNhan", xacNhan);
        } else {
            return "redirect:/login?error=notfound";
        }
        return "Main/XacNhanDatLich";
    }

    @GetMapping("/datLichThanhCong")
    public String hienThiTrangDatLichThanhCong() {
        return "Main/ketQuaTb";
    }

    @PostMapping("/datLichThanhCong")
    public String chuyenSangTrangThanhToan(@ModelAttribute XacNhanDatLichDTO xacNhan, Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            return "Main/XacNhanDatLich";
        }

        List<ChiTietDatLichDTO> danhSachChiTiet = xacNhan.getChiTietDatLichList();

        for (ChiTietDatLichDTO chiTiet : danhSachChiTiet) {
            if (chiTiet.getNgayDat() == null || chiTiet.getThoiGian() == null || chiTiet.getGia() == null) {
                model.addAttribute("error", "Một số thông tin lịch đặt không hợp lệ. Vui lòng kiểm tra lại.");
                return "Main/XacNhanDatLich";
            }

            if (chiTiet.getNgayDat().isBefore(LocalDate.now())) {
                model.addAttribute("error", "Ngày đặt lịch không thể trước ngày hiện tại.");
                return "Main/XacNhanDatLich";
            }
        }
        // Kiểm tra logic nghiệp vụ (slot có bị trùng không, giá có hợp lệ không,...)
        List<Integer> lichTamHopLe = xacNhanDatLichService.kiemTraHopLeTamThoi(xacNhan);

        int tongSoLich = danhSachChiTiet.size();
        int soLichHopLe = lichTamHopLe.size();

        if (soLichHopLe == 0) {
            model.addAttribute("ketQua", "fail");
            return "Main/ketQuaTb";
        } else if (soLichHopLe < tongSoLich) {
            model.addAttribute("ketQua", "partial");
            return "Main/ketQuaTb";
        }
        List<Integer> idLichDatDuocLuu = xacNhanDatLichService.luuDatLich(xacNhan);
        if (idLichDatDuocLuu.isEmpty()) {
            return "redirect:/xacnhan"; // nếu không có đơn nào
        }

        // Ghép danh sách id thành query string: id=1&id=2&id=3
        StringBuilder query = new StringBuilder();
        for (Integer id : idLichDatDuocLuu) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append("id=").append(id);
        }

        // redirect sang trang thanh toán, truyền toàn bộ id
        return "redirect:/thanh-toan?" + query;
    }

}
