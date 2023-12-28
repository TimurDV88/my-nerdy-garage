package com.mynerdygarage.works_parts.repository;

import com.mynerdygarage.works_parts.model.WorkPart;
import com.mynerdygarage.works_parts.model.WorkPartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkPartRepository extends JpaRepository<WorkPart, WorkPartId> {

    List<WorkPart> findByWorkPartIdWorkId(Long workId);
}
