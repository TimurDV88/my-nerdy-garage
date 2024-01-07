package com.mynerdygarage.parts.controller;

import com.mynerdygarage.parts.dto.NewPartDto;
import com.mynerdygarage.parts.dto.PartFullDto;
import com.mynerdygarage.parts.dto.PartShortDto;
import com.mynerdygarage.parts.dto.PartUpdateDto;
import com.mynerdygarage.parts.service.PartService;
import com.mynerdygarage.works_parts.service.WorkPartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parts")
public class PartController {

    private final PartService partService;
    private final WorkPartService workPartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartFullDto addPart(@RequestHeader("X-Owner-User-Id") Long userId,
                               @RequestBody @Valid NewPartDto newPartDto) {

        return partService.addPart(userId, newPartDto);
    }

    @PatchMapping("/{partId}")
    public PartFullDto update(@RequestHeader("X-Owner-User-Id") Long userId,
                              @PathVariable @NotNull Long partId,
                              @RequestBody @Valid PartUpdateDto inputDto) {

        return partService.update(userId, partId, inputDto);
    }


    @GetMapping("/{partId}")
    public PartFullDto getById(@RequestHeader("X-Owner-User-Id") Long userId,
                               @PathVariable @NotNull Long partId) {

        return partService.getById(userId, partId);
    }

    @GetMapping
    public List<PartFullDto> getByParams(@RequestHeader("X-Owner-User-Id") Long userId,
                                         @RequestParam(value = "searchText", required = false) String text,
                                         @RequestParam(value = "vehicleIds", required = false) Long[] vehicleIds,
                                         @RequestParam(value = "categoryIds", required = false) Long[] categoryIds,
                                         @RequestParam(value = "isReusable", required = false) Boolean isReusable,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "start", required = false) String start,
                                         @RequestParam(value = "end", required = false) String end,
                                         @RequestParam(value = "sortBy", required = false,
                                                 defaultValue = "id") String sortBy,
                                         @RequestParam(value = "from", defaultValue = "0") int from,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {

        return partService.getByParams(
                userId, text, vehicleIds, categoryIds, isReusable, status, start, end, sortBy, from, size);
    }

    @DeleteMapping("/{partId}")
    public void removeById(@RequestHeader("X-Owner-User-Id") Long userId,
                           @PathVariable @NotNull Long partId) {

        partService.removeById(userId, partId);
    }

    /*
        WorkPart service
     */

    @PostMapping("/{partId}/work/{workId}")
    public void addPartToWork(@RequestHeader("X-Owner-User-Id") Long userId,
                              @PathVariable @NotNull Long partId,
                              @PathVariable @NotNull Long workId) {

        workPartService.addPartToWork(userId, partId, workId);
    }

    @GetMapping("/{workId}")
    public List<PartShortDto> getByWorkId(@RequestHeader("X-Owner-User-Id") Long userId,
                                          @PathVariable @NotNull Long workId,
                                          @RequestParam(value = "from", defaultValue = "0") int from,
                                          @RequestParam(value = "size", defaultValue = "10") int size) {

        return workPartService.getPartsByWorkId(userId, workId, from, size);
    }

    @DeleteMapping("/{partId}/work/{workId}")
    public void removePartFromWork(@RequestHeader("X-Owner-User-Id") Long userId,
                                   @PathVariable @NotNull Long partId,
                                   @PathVariable @NotNull Long workId) {

        workPartService.removePartFromWork(userId, partId, workId);
    }
}
