package com.mynerdygarage.works_parts.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class WorkPartId implements Serializable {

    @Column(name = "work_id")
    private final Long workId;

    @Column(name = "part_id")
    private final Long partId;
}
