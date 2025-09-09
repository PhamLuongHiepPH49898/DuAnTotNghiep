package com.example.datn.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ThanhToanDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idThanhToan;
    private Double soTien;
    private String reference;
    private Integer trangThai;
}
