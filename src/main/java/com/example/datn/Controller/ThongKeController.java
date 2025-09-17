package com.example.datn.Controller;

import com.example.datn.DTO.DoanhThuSanProjection;
import com.example.datn.Service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/thongke/doanhthu")
    public String doanhThuTheoSan(
            @RequestParam(required = false) String monthYear,
            Model model) {

        // Tính tháng/năm (yyyy-MM)
        YearMonth ym = (monthYear == null || monthYear.isBlank())
                ? YearMonth.now()
                : YearMonth.parse(monthYear);
        int month = ym.getMonthValue();
        int year  = ym.getYear();

        // Lấy dữ liệu bảng
        List<DoanhThuSanProjection> list = thongKeService.tongHopTheoSan(month, year);
        // DoanhThuSanRow { String tenSan; long soLuotDat; Double tongDoanhThu; }

        // Tổng
        double total = list.stream()
                .map(DoanhThuSanProjection::getTongDoanhThu)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        // >>> DỮ LIỆU BIỂU ĐỒ
        List<String> labels = list.stream()
                .map(DoanhThuSanProjection::getTenSan)
                .toList();

        List<Double> values = list.stream()
                .map(DoanhThuSanProjection::getTongDoanhThu)
                .map(v -> v == null ? 0d : v)
                .toList();

        // Add vào model (BẮT BUỘC có)
        model.addAttribute("list", list);
        model.addAttribute("total", total);
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);

        // Phục vụ phần header/lọc
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("monthYear", ym.toString()); // "yyyy-MM"

        return "thongke/doanhthu"; // KHÔNG redirect
    }

}
