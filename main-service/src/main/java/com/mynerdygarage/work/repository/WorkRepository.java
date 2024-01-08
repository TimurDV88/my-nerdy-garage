package com.mynerdygarage.work.repository;

import com.mynerdygarage.work.model.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long>, QuerydslPredicateExecutor<Work> {

    Work findByVehicleIdAndStartDateAndTitleIgnoreCase(Long vehicleId, LocalDate startDate, String title);

    Page<Work> findByVehicleId(Long vehicleId, Pageable pageable);
}
