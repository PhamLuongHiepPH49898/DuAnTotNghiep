package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SanBongSpecification {

    public static Specification<SanBong> searchBy(String keyword, Integer loaiSan, Integer monTheThao) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Tìm kiếm theo từ khóa
            if (keyword != null && !keyword.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("ten_san_bong")),
                        "%" + keyword.toLowerCase() + "%"
                ));
            }

            // Lọc theo loại sân
            if (loaiSan != null) {
                predicates.add(criteriaBuilder.equal(root.get("loaiSan").get("id"), loaiSan));
            }

            // Lọc theo môn thể thao
            if (monTheThao != null) {
                predicates.add(criteriaBuilder.equal(root.get("loaiMonTheThao").get("id"), monTheThao));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
