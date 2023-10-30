package com.mynerdygarage.vehicle.model;

import com.mynerdygarage.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@ToString
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "producer")
    private String producer;

    @Column(name = "model")
    private String model;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "engine_volume")
    private Double engineVolume;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "power")
    private Integer power;

    @Column(name = "description")
    private String description;
}
