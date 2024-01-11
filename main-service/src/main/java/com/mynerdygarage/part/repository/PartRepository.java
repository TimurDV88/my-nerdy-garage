package com.mynerdygarage.part.repository;

import com.mynerdygarage.part.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PartRepository extends JpaRepository<Part, Long>, QuerydslPredicateExecutor<Part> {

    Part findByOwnerIdAndOrderDateAndPartNumberIgnoreCase(Long userId, LocalDate orderDate, String partNumber);
}
