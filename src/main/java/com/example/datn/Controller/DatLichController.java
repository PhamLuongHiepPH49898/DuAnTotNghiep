
package com.example.datn.Controller;

import com.example.datn.DTO.ChiTietDatLichDTO;
import com.example.datn.DTO.XacNhanDatLichDTO;
import com.example.datn.Entity.*;
import com.example.datn.Repository.TaiKhoanRepo;
import com.example.datn.Service.DatSanService;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.XacNhanDatLichService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
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
        if (san == null) return "redirect:/trang-chu";

        List<SanBong> sanList = datSanService.layDanhSachSan();
        List<KhungGio> khungGioList = datSanService.layDanhSachKhungGio();
        List<GiaTheoKhungGio> danhSachGiaTheoKhungGio = datSanService.layDanhGiaTheoKhungGio(); // lấy danh sách giá theo khung giờ
        List<String> cacSlotDaDat = datSanService.getAllSlotKeys();
        List<String> cacSlotTonTai = datSanService.getAllSlotKeysTonTai(); // dạng: "2025-07-07_Sân A_08:00-09:00"
        model.addAttribute("cacSlotTonTai", cacSlotTonTai);
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
    public String kiemTraHopLeVaChuyenThanhToan(
            @ModelAttribute @Validated XacNhanDatLichDTO xacNhan,
            BindingResult result,
            Model model,
            HttpSession session) {

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

        // ✅ Toàn bộ hợp lệ → lưu tạm vào Session → sang trang thanh toán
        session.setAttribute("thongTinDatLich", xacNhan);

        return "redirect:/thanh-toan"; // vd sang trang thanh toan
    }
    //giả lập
    @GetMapping("/thanh-toan")
    public String hienThiTrangThanhToan(HttpSession session, Model model) {
        XacNhanDatLichDTO xacNhan = (XacNhanDatLichDTO) session.getAttribute("thongTinDatLich");
        if (xacNhan == null) {
            return "redirect:/xacnhan";
        }

        model.addAttribute("xacNhan", xacNhan);
        return "Main/thanh-toan";
    }
    //giả lập để test khi xac nhan thanh toan sẽ luu lich và tb thanh cong
    @PostMapping("/thanh-toan/xac-nhan")
    public String luuSauKhiThanhToan(HttpSession session, Model model) {
        XacNhanDatLichDTO xacNhan = (XacNhanDatLichDTO) session.getAttribute("thongTinDatLich");
        if (xacNhan == null) {
            return "redirect:/xacnhan";
        }

        List<Integer> danhSachIdLich = xacNhanDatLichService.luuDatLich(xacNhan);
        session.removeAttribute("thongTinDatLich"); // Xóa sau khi đã lưu

        if (danhSachIdLich.isEmpty()) {
            model.addAttribute("ketQua", "fail");
        } else if (danhSachIdLich.size() < xacNhan.getChiTietDatLichList().size()) {
            model.addAttribute("ketQua", "partial");
        } else {
            model.addAttribute("ketQua", "success");
        }

        return "Main/ketQuaTb";
    }
    // file giả lập thanh-toan.html

}
