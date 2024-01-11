package com.mynerdygarage.work_part.service;

import com.mynerdygarage.part.dto.PartShortDto;
import com.mynerdygarage.work.dto.WorkShortDto;

import java.util.List;

public interface WorkPartService {

    void addPartToWork(Long userId, Long partId, Long workId);

    List<PartShortDto> getPartsByWorkId(Long userId, Long workId, int from, int size);

    List<WorkShortDto> getWorksByPartId(Long userId, Long workId, int from, int size);

    void removePartFromWork(Long userId, Long partId, Long workId);
}
