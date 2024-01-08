package com.mynerdygarage.work.service;

import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.dto.WorkUpdateDto;

import java.util.List;

public interface WorkService {

    WorkFullDto addWork(Long userId, NewWorkDto newWorkDto);

    WorkFullDto update(Long userId, Long workId, WorkUpdateDto workUpdateDto);

    WorkFullDto getById(Long userId, Long workId);

    List<WorkShortDto> getByVehicleId(Long userId, Long vehicleId, String sortBy, int from, int size);

    List<WorkFullDto> getByParams(Long userId,
                                  String text,
                                  Long[] vehicleIds,
                                  Long[] categoryIds,
                                  String status,
                                  String start,
                                  String end,
                                  String sortBy,
                                  int from, int size);

    void removeById(Long userId, Long workId);
}
