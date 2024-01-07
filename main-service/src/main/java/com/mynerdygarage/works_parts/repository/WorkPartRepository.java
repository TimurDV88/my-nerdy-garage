package com.mynerdygarage.works_parts.repository;

import com.mynerdygarage.works_parts.model.WorkPart;
import com.mynerdygarage.works_parts.model.WorkPartId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkPartRepository extends JpaRepository<WorkPart, WorkPartId> {

    Page<WorkPart> findByWorkPartIdWorkId(Long workId, Pageable pageable);

    Page<WorkPart> findByWorkPartIdPartId(Long partId, Pageable pageable);
}
