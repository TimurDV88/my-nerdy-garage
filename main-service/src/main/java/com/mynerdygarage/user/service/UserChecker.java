package com.mynerdygarage.user.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;

import java.time.LocalDate;

public class UserChecker {

    public static boolean isNotCorrect(UserRepository userRepository, UserFullDto userDto) {

        if (userDto.getEmail() != null && userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictOnRequestException("- User with this Email already exists: " + userDto.getEmail());
        }

        if (userDto.getName() != null && userRepository.existsByName(userDto.getName())) {
            throw new ConflictOnRequestException("- User with this Name already exists: " + userDto.getName());
        }

        LocalDate birthDate = CustomFormatter.stringToDate(userDto.getBirthDate());

        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Birth date must be before current time");
        }

        return false;
    }
}
