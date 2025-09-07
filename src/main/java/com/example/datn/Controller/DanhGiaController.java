package com.example.datn.Controller;

import com.example.datn.Entity.DanhGia;
import com.example.datn.Entity.SanBong;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Service.DanhGiaService;
import com.example.datn.Service.SanBongService;
import com.example.datn.Service.TaiKhoanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Map;

@Controller
@RequestMapping("/danh-gia")
public class DanhGiaController {

    @Autowired
    private DanhGiaService danhGiaService;

    @Autowired
    private SanBongService sanBongService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    // POST: gửi đánh giá
//    @PostMapping("/them")
//    public String themDanhGia(@ModelAttribute("danhGia") DanhGia danhGia,
//                              @RequestParam("id_san_bong") Integer id_san_bong,
//                              RedirectAttributes redirectAttributes) {
//
//        // Lấy sân bóng
//        SanBong sanBong = sanBongService.findById(id_san_bong);
//        if (sanBong == null) {
//            redirectAttributes.addFlashAttribute("error", "Sân bóng không tồn tại!");
//            return "redirect:/ve-trang-chu";
//        }
//
//        // Lấy tài khoản đang đăng nhập
//        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanDangNhap();
//        if (taiKhoan == null) {
//            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để đánh giá!");
//            return "redirect:/chi-tiet/" + id_san_bong;
//        }
//
//        // Kiểm tra đã đánh giá chưa
//        if (danhGiaService.kiemTraDanhGiaTonTai(taiKhoan.getId(), id_san_bong)) {
//            redirectAttributes.addFlashAttribute("error", "Bạn đã đánh giá sân này rồi!");
//            return "redirect:/chi-tiet/" + id_san_bong;
//        }
//
//        // Gán thông tin cho đánh giá
//        danhGia.setSanBong(sanBong);
//        danhGia.setTaiKhoan(taiKhoan);
//        danhGia.setTrangThai(1); // 1 = hiển thị
//        danhGiaService.luuDanhGia(danhGia);
//
//        // Thông báo và trung bình sao
//        redirectAttributes.addFlashAttribute("success", "Đánh giá thành công!");
//        redirectAttributes.addFlashAttribute("trungBinhSao", danhGiaService.tinhTrungBinhSao(id_san_bong));
//
//        return "redirect:/chi-tiet/" + id_san_bong;
//    }
    @PostMapping("/them")
    public String themDanhGia(@RequestParam("id_san_bong") Integer idSanBong,
                              @RequestParam("sao") Integer sao,
                              @RequestParam("noiDung") String noiDung,
                              RedirectAttributes redirectAttributes) {

        // Lấy sân bóng
        SanBong sanBong = sanBongService.findById(idSanBong);
        if (sanBong == null) {
            redirectAttributes.addFlashAttribute("error", "Sân bóng không tồn tại!");
            return "redirect:/ve-trang-chu";
        }

        // Lấy tài khoản đang đăng nhập
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanDangNhap();
        if (taiKhoan == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập để đánh giá!");
            return "redirect:/chi-tiet/" + idSanBong;
        }

        // Kiểm tra đã đánh giá chưa
        if (danhGiaService.kiemTraDanhGiaTonTai(taiKhoan.getId(), idSanBong)) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đánh giá sân này rồi!");
            return "redirect:/chi-tiet/" + idSanBong;
        }

        // Tạo và gán thông tin cho đánh giá
        DanhGia danhGia = new DanhGia();
        danhGia.setSanBong(sanBong);
        danhGia.setTaiKhoan(taiKhoan);
        danhGia.setSao(sao);
        danhGia.setNoiDung(noiDung);
        danhGia.setTrangThai(1);
        danhGia.setNgayDanhGia(java.time.LocalDate.now());

        danhGiaService.luuDanhGia(danhGia);

        redirectAttributes.addFlashAttribute("success", "Đánh giá thành công!");
        redirectAttributes.addFlashAttribute("trungBinhSao", danhGiaService.tinhTrungBinhSao(idSanBong));

        return "redirect:/chi-tiet/" + idSanBong;
    }


    // GET: danh sách đánh giá của sân
    @GetMapping("/san/{id_san_bong}")
    public String hienThiDanhSachDanhGia(@PathVariable("id_san_bong") Integer id_san_bong,
                                         Model model,
                                         HttpServletRequest request) {

        // Lấy danh sách đánh giá
        model.addAttribute("danhSachDanhGia", danhGiaService.layDanhGiaTheoSan(id_san_bong));

        // Trung bình sao
        model.addAttribute("trungBinhSao", danhGiaService.tinhTrungBinhSao(id_san_bong));

        // Object rỗng cho form đánh giá
        model.addAttribute("danhGia", new DanhGia());

        // Flash attributes
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (flashMap != null) {
            model.addAllAttributes(flashMap);
        }

        return "Main/ChiTietSan";
    }
}
