package com.mynerdygarage.user.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.user.dto.UserUpdateDto;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.user.service.util.UserChecker;
import com.mynerdygarage.user.service.util.UserCreator;
import com.mynerdygarage.user.service.util.UserUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserFullDto addUser(NewUserDto newUserDto) {

        log.info("-- Saving user: {}", newUserDto);

        if (!newUserDto.getPassword().equals(newUserDto.getPasswordConfirm())) {

            throw new ConflictOnRequestException(
                    "- Password does not match to confirmation, user not saved");
        }

        User user = UserCreator.createFromNewUserDto(passwordEncoder, newUserDto);

        UserChecker.checkNewUser(userRepository, user);

        UserFullDto fullDtoToReturn = UserMapper.modelToFullDto(userRepository.save(user));

        log.info("-- User has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public UserFullDto update(Long userId, UserUpdateDto userUpdateDto) {

        log.info("-- Updating user by userId={}: {}", userId, userUpdateDto);

        User userToUpdate = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        User userToCheck = UserCreator.createFromUserUpdateDto(userUpdateDto);

        UserChecker.checkUserUpdate(userRepository, userToCheck);

        User updatedUser = UserUpdater.update(userToUpdate, userUpdateDto);

        UserFullDto fullDtoToReturn = UserMapper.modelToFullDto(userRepository.save(updatedUser));

        log.info("-- User has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public UserFullDto getById(Long userId) {

        log.info("-- Getting user by userId={}", userId);

        UserFullDto fullDtoToReturn = UserMapper.modelToFullDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId)));

        log.info("-- User returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long userId) {

        log.info("-- Deleting user by userId={}", userId);

        User userToCheck = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        UserFullDto dtoToShowInLog = UserMapper.modelToFullDto(userToCheck);

        userRepository.deleteById(userId);

        log.info("-- User deleted: {}", dtoToShowInLog);
    }
}
