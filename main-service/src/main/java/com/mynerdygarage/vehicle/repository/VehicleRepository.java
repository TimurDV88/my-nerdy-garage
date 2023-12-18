package com.mynerdygarage.vehicle.repository;

import com.mynerdygarage.vehicle.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByOwnerIdAndId(Long id, Long ownerId);

    @Query("SELECT v.id " +
            "FROM Vehicle AS v " +
            "WHERE v.owner.id = :ownerId")
    List<Long> findIdsByOwnerId(Long ownerId);

    boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);

    Page<Vehicle> findByOwnerId(Long ownerId, Pageable pageable);
}
