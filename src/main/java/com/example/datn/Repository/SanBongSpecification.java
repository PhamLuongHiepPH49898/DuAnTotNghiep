package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class SanBongSpecification {
    public static Specification<SanBong> searchBy(Integer loaiSan, Integer monTheThao) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (loaiSan != null) {
                predicates.add(criteriaBuilder.equal(root.get("loaiSan").get("id"), loaiSan));
            }

            if (monTheThao != null) {
                predicates.add(criteriaBuilder.equal(root.get("loaiMonTheThao").get("id"), monTheThao));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
