package com.mynerdygarage.work.service;

import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.dto.WorkShortDto;

import java.time.LocalDate;
import java.util.List;

public interface WorkService {

    WorkFullDto addWork(Long userId, NewWorkDto newWorkDto);

    WorkFullDto update(Long userId, Long workId, WorkFullDto workFullDto);

    WorkFullDto getById(Long userId, Long workId);

    List<WorkShortDto> getByVehicleId(Long userId, Long vehicleId, int from, int size);

    List<WorkFullDto> getByParams(Long userId,
                                  Long[] vehicleIds,
                                  Boolean isPlanned,
                                  LocalDate start,
                                  LocalDate end,
                                  String sort,
                                  int from, int size);

    void removeById(Long userId, Long workId);
}
