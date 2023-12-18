package com.mynerdygarage.work.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewWorkDto {

    @NotNull
    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String title;

    @Size(min = 2, message = "size must be between 2 and 250")
    @Size(max = 250, message = "size must be between 2 and 250")
    private final String description;

    private final Long vehicleId;

    private final Long categoryId;

    @Size(min = 2, message = "size must be between 2 and 50")
    @Size(max = 50, message = "size must be between 2 and 50")
    private final String status;

    @Size(min = 12, message = "size must be between 12 and 13")
    @Size(max = 13, message = "size must be between 12 and 13")
    private final String startDate;

    @Size(min = 12, message = "size must be between 12 and 13")
    @Size(max = 13, message = "size must be between 12 and 13")
    private final String endDate;
}
