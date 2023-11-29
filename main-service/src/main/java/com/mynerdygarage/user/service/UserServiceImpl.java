package com.mynerdygarage.user.service;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.user.service.util.UserChecker;
import com.mynerdygarage.user.service.util.UserCreator;
import com.mynerdygarage.user.service.util.UserUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserFullDto addUser(NewUserDto newUserDto) {

        log.info("-- Saving user: {}", newUserDto);

        User user = UserCreator.create(newUserDto);

        UserChecker.isCorrect(userRepository, UserMapper.userToFullDto(user));

        UserFullDto fullDtoToReturn = UserMapper.userToFullDto(userRepository.save(user));

        log.info("-- User has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public UserFullDto update(Long userId, UserFullDto inputFullUserDto) {

        log.info("-- Updating user by userId={}: {}", userId, inputFullUserDto);

        UserFullDto fullDtoToReturn = UserUpdater.update(userRepository, userId, inputFullUserDto);

        log.info("-- User has been updated: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    public UserFullDto getById(Long userId) {

        log.info("-- Getting user by userId={}", userId);

        UserFullDto fullDtoToReturn = UserMapper.userToFullDto(userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId)));

        log.info("-- User returned: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public void removeById(Long userId) {

        log.info("--- Deleting user by userId={}", userId);

        User userToCheck = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        UserFullDto dtoToShowInLog = UserMapper.userToFullDto(userToCheck);

        userRepository.deleteById(userId);

        log.info("--- User deleted: {}", dtoToShowInLog);
    }
}
