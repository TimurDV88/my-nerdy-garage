package com.mynerdygarage.vehicle.repository;

import com.mynerdygarage.vehicle.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);

    Page<Vehicle> findByOwnerId(Long ownerId, Pageable pageable);
}
