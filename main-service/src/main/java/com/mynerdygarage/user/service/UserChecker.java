package com.mynerdygarage.user.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;

import java.time.LocalDate;

public class UserChecker {

    public static boolean isNotCorrect(UserRepository userRepository, UserFullDto user) {

        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictOnRequestException("- User with this Email already exists");
        }

        if (user.getName() != null && userRepository.existsByName(user.getName())) {
            throw new ConflictOnRequestException("- User with this Name already exists");
        }

        LocalDate birthDate = CustomFormatter.stringToDate(user.getBirthDate());

        if (birthDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Birth date must be before current time");
        }

        return true;
    }
}
