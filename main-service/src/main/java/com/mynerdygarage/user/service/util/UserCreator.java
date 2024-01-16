package com.mynerdygarage.user.service.util;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserUpdateDto;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class UserCreator {

    public static User createFromNewUserDto(PasswordEncoder passwordEncoder, NewUserDto newUserDto) {

        User user = new User();

        user.setName(newUserDto.getName());
        user.setEmail(newUserDto.getEmail());
        user.setBirthDate(CustomFormatter.stringToDate(newUserDto.getBirthDate()));
        user.setRegDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));

        return user;
    }

    public static User createFromUserUpdateDto(UserUpdateDto userUpdateDto) {

        User user = new User();

        user.setName(userUpdateDto.getName());
        user.setEmail(userUpdateDto.getEmail());
        user.setBirthDate(CustomFormatter.stringToDate(userUpdateDto.getBirthDate()));

        return user;
    }

}
