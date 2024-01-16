package com.mynerdygarage.user.service.util;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.IncorrectRequestException;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

public class UserChecker {

    private static void checkBirthDate(User user) {

        LocalDate birthDate = user.getBirthDate();

        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new IncorrectRequestException("- Birth date must be before current time");
        }
    }

    public static void checkNewUser(UserRepository userRepository, User userToCheck) {

        checkBirthDate(userToCheck);

        if (userRepository.existsByNameIgnoreCaseOrEmailIgnoreCase(userToCheck.getName(), userToCheck.getEmail())) {
            throw new ConflictOnRequestException(String.format(
                    "- User with name=%s or email=%s already exists", userToCheck.getName(), userToCheck.getEmail()));
        }
    }

    public static void checkUserUpdate(UserRepository userRepository, User userToCheck) {

        checkBirthDate(userToCheck);

        Optional<User> foundUser = userRepository.findByNameOrEmail(userToCheck.getName(), userToCheck.getEmail());

        if (foundUser.isPresent() && !foundUser.get().getId().equals(userToCheck.getId())) {
            throw new ConflictOnRequestException(String.format(
                    "- Another user with name=%s or email=%s already exists",
                    userToCheck.getName(), userToCheck.getEmail()));
        }
    }
}
