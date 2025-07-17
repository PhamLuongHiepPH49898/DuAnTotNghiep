package com.example.datn.Exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleError(Exception ex, Model model) {
        model.addAttribute("message", "Có lỗi xảy ra!"); // Truyền thông báo lỗi
        return "error"; // ✅ Hiển thị trang lỗi
    }

}
