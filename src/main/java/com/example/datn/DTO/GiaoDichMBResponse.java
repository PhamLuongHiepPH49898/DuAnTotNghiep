package com.example.datn.DTO;

import lombok.Data;

@Data
public class GiaoDichMBResponse {
    private String description;
    private String creditAmount; // số tiền
    private String date;         // ngày giao dịch
}

