package com.mynerdygarage.user.service;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;
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

        if (UserChecker.isNotCorrect(userRepository, UserMapper.userToFullDto(user))) {
            log.info("-- User has NOT been saved: {}", newUserDto);
            return null;
        }

        UserFullDto fullDtoToReturn = UserMapper.userToFullDto(userRepository.save(user));

        log.info("-- User has been saved: {}", fullDtoToReturn);

        return fullDtoToReturn;
    }

    @Override
    @Transactional
    public UserFullDto update(Long userId, UserFullDto inputFullUserDto) {

        log.info("-- Updating user by userId={}: {}", userId, inputFullUserDto);

        if (UserChecker.isNotCorrect(userRepository, inputFullUserDto)) {
            log.info("-- User with userId={} has NOT been updated", userId);
            return null;
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        NullChecker.setIfNotNull(user::setName, inputFullUserDto.getName());
        NullChecker.setIfNotNull(user::setEmail, inputFullUserDto.getEmail());
        NullChecker.setIfNotNull(user::setBirthDate, CustomFormatter.stringToDate(inputFullUserDto.getBirthDate()));

        UserFullDto fullDtoToReturn = UserMapper.userToFullDto(userRepository.save(user));

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
