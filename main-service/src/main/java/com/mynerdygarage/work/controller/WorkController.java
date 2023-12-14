package com.mynerdygarage.work.controller;

import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.dto.WorkUpdateDto;
import com.mynerdygarage.work.service.WorkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/works")
public class WorkController {

    private final WorkService workService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkFullDto addWork(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody @Valid NewWorkDto newWorkDto) {

        return workService.addWork(userId, newWorkDto);
    }

    @PatchMapping("/{workId}")
    public WorkFullDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable @NotNull Long workId,
                              @RequestBody @Valid WorkUpdateDto workUpdateDto) {

        return workService.update(userId, workId, workUpdateDto);
    }

    @GetMapping("/{workId}")
    public WorkFullDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable @NotNull Long workId) {

        return workService.getById(userId, workId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<WorkShortDto> getByVehicleId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable @NotNull Long vehicleId,
                                             @RequestParam(value = "sortBy", required = false,
                                                     defaultValue = "id") String sortBy,
                                             @RequestParam(value = "from", defaultValue = "0") int from,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {

        return workService.getByVehicleId(userId, vehicleId, sortBy, from, size);
    }

    @GetMapping
    public List<WorkFullDto> getByParams(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(value = "searchText", required = false) String text,
                                         @RequestParam(value = "vehicleIds", required = false) Long[] vehicleIds,
                                         @RequestParam(value = "categoryIds", required = false) Long[] categoryIds,
                                         @RequestParam(value = "isPlanned", required = false) Boolean isPlanned,
                                         @RequestParam(value = "start", required = false) String start,
                                         @RequestParam(value = "end", required = false) String end,
                                         @RequestParam(value = "sortBy", required = false,
                                                 defaultValue = "id") String sortBy,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {

        return workService.getByParams(
                userId, text, vehicleIds, categoryIds, isPlanned, start, end, sortBy, from, size);
    }

    @DeleteMapping("/{workId}")
    public void removeById(Long userId, Long workId) {

        workService.removeById(userId, workId);
    }

}
