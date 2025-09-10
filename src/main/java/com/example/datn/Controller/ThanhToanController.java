package com.example.datn.Controller;

import com.example.datn.DTO.ThanhToanDTO;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Entity.TaiKhoan;
import com.example.datn.Entity.ThanhToan;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.TaiKhoanNganHangService;
import com.example.datn.Service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ThanhToanController {

    @Autowired
    private ThanhToanService thanhToanService;

    @Autowired
    private TaiKhoanNganHangService taiKhoanNganHangService;

    @Autowired
    private LichDatSanService lichDatSanService;

    /**
     * Trang thanh toán (cho nhiều lịch đặt sân → 1 giao dịch)
     */
    @GetMapping("/thanh-toan")
    public String hienThiTrangThanhToan(@RequestParam("id") List<Integer> idLichDatSan, Model model) {
        // Tạo giao dịch cho nhiều lịch
        ThanhToan tt = thanhToanService.taoGiaoDichChoNhieuLichDat(idLichDatSan);

        // Lấy danh sách lịch để hiển thị trong giao diện
        List<LichDatSan> lichList = lichDatSanService.findByIds(idLichDatSan);

        model.addAttribute("thanhToan", tt);
        model.addAttribute("dsLichDat", lichList);

        return "Main/ThanhToan";
    }

    /**
     * API trả về trạng thái JSON (AJAX gọi để cập nhật trạng thái realtime)
     */
    @GetMapping("/thanh-toan/status/{id}")
    @ResponseBody
    public ThanhToanDTO kiemTraTrangThai(@PathVariable("id") Integer idThanhToan) {
        return thanhToanService.kiemTraThanhToan(idThanhToan);
    }


    /**
     * Trang hiển thị khi thanh toán thành công
     */
    @GetMapping("/thanh-toan/thanh-cong")
    public String hienThiThanhCong() {
        return "Main/Success";
    }
}
