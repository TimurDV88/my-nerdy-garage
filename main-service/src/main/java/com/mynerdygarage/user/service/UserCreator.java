package com.mynerdygarage.user.service;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;

import java.time.LocalDateTime;

public class UserCreator {

    public static User create(NewUserDto newUserDto) {

        User user = new User();

        user.setName(newUserDto.getName());
        user.setEmail(newUserDto.getEmail());

        user.setBirthDate(CustomFormatter.stringToDate(newUserDto.getBirthDate()));

        user.setRegDate(LocalDateTime.now());

        return user;
    }

}
