package com.mynerdygarage.works_parts.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@ToString
public class WorkPartId implements Serializable {

    @Column(name = "work_id")
    private Long workId;

    @Column(name = "part_id")
    private Long partId;
}
