package com.mynerdygarage.part.service;

import com.mynerdygarage.part.dto.NewPartDto;
import com.mynerdygarage.part.dto.PartFullDto;
import com.mynerdygarage.part.dto.PartUpdateDto;

import java.util.List;

public interface PartService {

    PartFullDto addPart(Long userId, NewPartDto newPartDto);

    PartFullDto update(Long userId, Long partId, PartUpdateDto inputDto);

    PartFullDto getById(Long userId, Long partId);

    List<PartFullDto> getByParams(Long userId,
                                  String text,
                                  Long[] vehicleIds,
                                  Long[] categoryIds,
                                  Boolean isReusable,
                                  String status,
                                  String start,
                                  String end,
                                  String sortBy, int from, int size);

    void removeById(Long userId, Long partId);
}
