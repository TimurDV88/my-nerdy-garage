package com.mynerdygarage.work.controller;

import com.mynerdygarage.work.dto.NewWorkDto;
import com.mynerdygarage.work.dto.WorkFullDto;
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.dto.WorkUpdateDto;
import com.mynerdygarage.work.service.WorkService;
import com.mynerdygarage.works_parts.service.WorkPartService;
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
    private final WorkPartService workPartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkFullDto addWork(@RequestHeader("X-Initiator-User-Id") Long userId,
                               @RequestBody @Valid NewWorkDto newWorkDto) {

        return workService.addWork(userId, newWorkDto);
    }

    @PatchMapping("/{workId}")
    public WorkFullDto update(@RequestHeader("X-Initiator-User-Id") Long userId,
                              @PathVariable @NotNull Long workId,
                              @RequestBody @Valid WorkUpdateDto workUpdateDto) {

        return workService.update(userId, workId, workUpdateDto);
    }

    @GetMapping("/{workId}")
    public WorkFullDto getById(@RequestHeader("X-Initiator-User-Id") Long userId,
                               @PathVariable @NotNull Long workId) {

        return workService.getById(userId, workId);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public List<WorkShortDto> getByVehicleId(@RequestHeader("X-Initiator-User-Id") Long userId,
                                             @PathVariable @NotNull Long vehicleId,
                                             @RequestParam(value = "sortBy", required = false,
                                                     defaultValue = "id") String sortBy,
                                             @RequestParam(value = "from", defaultValue = "0") int from,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {

        return workService.getByVehicleId(userId, vehicleId, sortBy, from, size);
    }

    @GetMapping
    public List<WorkFullDto> getByParams(@RequestHeader("X-Initiator-User-Id") Long userId,
                                         @RequestParam(value = "searchText", required = false) String text,
                                         @RequestParam(value = "vehicleIds", required = false) Long[] vehicleIds,
                                         @RequestParam(value = "categoryIds", required = false) Long[] categoryIds,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "start", required = false) String start,
                                         @RequestParam(value = "end", required = false) String end,
                                         @RequestParam(value = "sortBy", required = false,
                                                 defaultValue = "id") String sortBy,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {

        return workService.getByParams(
                userId, text, vehicleIds, categoryIds, status, start, end, sortBy, from, size);
    }

    @DeleteMapping("/{workId}")
    public void removeById(@RequestHeader("X-Initiator-User-Id") Long userId,
                           @PathVariable @NotNull Long workId) {

        workService.removeById(userId, workId);
    }

    /*
        WorkPart service
    */
    @GetMapping("/{partId}")
    public List<WorkShortDto> getByPartId(@RequestHeader("X-Initiator-User-Id") Long userId,
                                          @PathVariable @NotNull Long partId,
                                          @RequestParam(value = "from", defaultValue = "0") int from,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {

        return workPartService.getWorksByPartId(userId, partId, from, size);
    }
}
