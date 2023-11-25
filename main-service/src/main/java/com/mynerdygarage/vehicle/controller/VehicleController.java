package com.mynerdygarage.vehicle.controller;

import com.mynerdygarage.vehicle.dto.NewVehicleDto;
import com.mynerdygarage.vehicle.dto.VehicleFullDto;
import com.mynerdygarage.vehicle.dto.VehicleShortDto;
import com.mynerdygarage.vehicle.dto.VehicleUpdateDto;
import com.mynerdygarage.vehicle.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleFullDto addVehicle(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @RequestBody @Valid NewVehicleDto newVehicleDto) {

        return vehicleService.addVehicle(ownerId, newVehicleDto);
    }

    @PatchMapping("/{vehicleId}")
    public VehicleFullDto update(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                 @PathVariable @NotNull Long vehicleId,
                                 @RequestBody @Valid VehicleUpdateDto vehicleUpdateDto) {

        return vehicleService.update(ownerId, vehicleId, vehicleUpdateDto);
    }

    @GetMapping("/{vehicleId}")
    public VehicleFullDto getById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                  @PathVariable @NotNull Long vehicleId) {

        return vehicleService.getById(ownerId, vehicleId);
    }

    @GetMapping
    public List<VehicleShortDto> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(value = "from", required = false,
                                                      defaultValue = "0") int from,
                                              @RequestParam(value = "size", required = false,
                                                      defaultValue = "10") int size) {

        return vehicleService.getByOwnerId(ownerId, from, size);
    }

    @DeleteMapping("/{vehicleId}")
    public void removeById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                           @PathVariable @NotNull Long vehicleId) {

        vehicleService.removeById(ownerId, vehicleId);
    }
}
