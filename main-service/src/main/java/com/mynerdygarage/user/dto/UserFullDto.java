package com.mynerdygarage.user.dto;

import lombok.Data;

@Data
public class UserFullDto {

    private final Long id;

    private final String name;

    private final String email;

    private final String birthDate;

    private final String regDate;
}
