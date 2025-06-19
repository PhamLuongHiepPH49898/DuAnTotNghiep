package com.example.datn.DTO;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class ThongKeDoanhThuDTO {
    private String tenSan;
    private int thang;
    private BigDecimal tongDoanhThu;
}
