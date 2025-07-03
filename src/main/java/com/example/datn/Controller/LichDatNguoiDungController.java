package com.example.datn.Controller;

import com.example.datn.DTO.LichDatSanForm;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Security.CustomUserDetails;
import com.example.datn.Service.LichDatSanService;
import com.example.datn.Service.LichDatSanService2;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/user/lich-dat")
@RequiredArgsConstructor
@PreAuthorize("hasRole('NGUOI_DUNG')")
public class LichDatNguoiDungController {
    private final LichDatSanService2 lichSvc;

    private Integer userId(Principal p) {
        return ((CustomUserDetails) ((Authentication)p).getPrincipal()).getTaiKhoan().getId();
    }

    @GetMapping
    public String list(Model m, Principal p) {
        m.addAttribute("books", lichSvc.getBookingsOfUser(userId(p)));
        return "lichdat/my-bookings";
    }

    @GetMapping("/{id}/sua")
    public String editForm(@PathVariable Integer id, Model m, Principal p) {
        LichDatSan b = lichSvc.findByIdForUser(id, userId(p));
        m.addAttribute("booking", b);
        m.addAttribute("lichDatSanForm", LichDatSanForm.fromEntity(b));
        return "lichdat/booking-edit";
    }

    @PostMapping("/{id}/sua")
    public String saveEdit(@PathVariable Integer id,
                           @ModelAttribute @Valid LichDatSanForm f,
                           BindingResult rs,
                           RedirectAttributes ra,
                           Principal p) {
        if (rs.hasErrors()) return "lichdat/booking-edit";
        lichSvc.updateBooking(id, f, userId(p));
        ra.addFlashAttribute("success", "Đã sửa lịch đặt");
        return "redirect:/user/lich-dat";
    }

    @PostMapping("/{id}/huy")
    public String cancel(@PathVariable Integer id,
                         RedirectAttributes ra,
                         Principal p) {
        lichSvc.cancelBooking(id, userId(p));
        ra.addFlashAttribute("success", "Đã hủy lịch đặt");
        return "redirect:/user/lich-dat";
    }
}
