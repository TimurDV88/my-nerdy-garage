package com.mynerdygarage.work.repository;

import com.mynerdygarage.work.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface WorkRepository extends JpaRepository<Work, Long> {

    boolean existsByVehicleIdAndStartDateAndTitleIgnoreCase(Long userId, LocalDate startDate, String title);
}
