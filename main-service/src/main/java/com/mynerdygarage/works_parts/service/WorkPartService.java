package com.mynerdygarage.works_parts.service;

import com.mynerdygarage.parts.dto.PartShortDto;

import java.util.List;

public interface WorkPartService {

    void addPartToWork(Long userId, Long partId, Long workId);

    List<PartShortDto> getPartsByWorkId(Long userId, Long workId);
    void removePartFromWork(Long userId, Long partId, Long workId);
}
