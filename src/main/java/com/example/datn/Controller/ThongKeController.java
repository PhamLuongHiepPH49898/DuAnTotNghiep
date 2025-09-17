package com.example.datn.Controller;

import com.example.datn.DTO.DoanhThuSanProjection;
import com.example.datn.Service.ThongKeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.YearMonth;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/thongke")
public class ThongKeController {

    private final ThongKeService thongKeService;

    @GetMapping("/doanhthu")
    public String doanhThuTheoSan(
            @RequestParam(required = false) String monthYear, // dạng "yyyy-MM"
            Model model
    ) {
        // 1) Xác định tháng/năm
        YearMonth ym = StringUtils.hasText(monthYear) ? YearMonth.parse(monthYear) : YearMonth.now();

        // 2) Lấy dữ liệu tổng hợp
        var kq = thongKeService.thongKeTheoThang(ym.getMonthValue(), ym.getYear());

        // 3) Chuẩn bị dữ liệu cho Chart.js
        List<String> labels = kq.list().stream()
                .map(DoanhThuSanProjection::getTenSan)
                .toList();

        List<Double> values = kq.list().stream()
                .map(p -> p.getTongDoanhThu() == null ? 0d : p.getTongDoanhThu())
                .toList();

        // 4) Đưa vào model cho Thymeleaf (BẮT BUỘC có labels/values)
        model.addAttribute("list", kq.list());
        model.addAttribute("total", kq.tongTatCa());
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);

        // Phục vụ phần tiêu đề/lọc tháng
        model.addAttribute("month", kq.month());
        model.addAttribute("year", kq.year());
        model.addAttribute("monthYear", ym.toString()); // "yyyy-MM"

        // LƯU Ý: Render trực tiếp view, đừng redirect (redirect sẽ mất model)
        return "ThongKe/DoanhThuTheoSan";
    }
}
