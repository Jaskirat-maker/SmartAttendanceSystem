package com.gfg.repository;

import com.gfg.model.FootfallLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FootFallServiceRepository extends JpaRepository<FootfallLog, Long> {

    // ✅ Check if attendance is already marked by user on a given date
    boolean existsByUser_IdAndDate(Long userId, LocalDate date);
}
