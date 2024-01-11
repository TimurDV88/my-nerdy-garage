package com.mynerdygarage.work_part.model;

import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.work.model.Work;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "works_parts")
@Getter
@Setter
@ToString
public class WorkPart {

    @EmbeddedId
    private WorkPartId workPartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("workId")
    @JoinColumn(name = "work_id")
    Work work;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("partId")
    @JoinColumn(name = "part_id")
    Part part;
}
