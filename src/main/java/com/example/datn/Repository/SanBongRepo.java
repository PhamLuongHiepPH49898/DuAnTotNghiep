package com.example.datn.Repository;

import com.example.datn.Entity.SanBong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SanBongRepo extends JpaRepository<SanBong, Integer>, JpaSpecificationExecutor<SanBong> {

}
