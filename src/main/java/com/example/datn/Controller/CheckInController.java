package com.example.datn.Controller;
import com.example.datn.Entity.LichDatSan;
import com.example.datn.Service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;

@Controller
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @GetMapping("/check-in/{id}")
    public String checkIn(@PathVariable("id") Integer id, Model model, Principal principal) {
        LichDatSan lich = checkInService.getLichDatSanById(id);
        if (lich == null) {
            return "redirect:/?error=notfound";
        }

        String currentEmail = principal.getName(); // lấy email đăng nhập hiện tại

        // Kiểm tra xem lịch này có thuộc về user hiện tại hay không
        if (!lich.getTaiKhoan().getEmail().equals(currentEmail)) {
            return "redirect:/?error=unauthorized";
        }

        String message = checkInService.checkIn(id);
        model.addAttribute("message", message);
        return "CheckIn/CheckIn";
    }

}