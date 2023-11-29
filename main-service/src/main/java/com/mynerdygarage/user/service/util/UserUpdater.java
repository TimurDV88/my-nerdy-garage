package com.mynerdygarage.user.service.util;

import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserMapper;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.user.repository.UserRepository;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;

public class UserUpdater {

    public static UserFullDto update(UserRepository userRepository, Long userId, UserFullDto inputFullUserDto) {

        UserChecker.isCorrect(userRepository, inputFullUserDto);

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("- UserId not found: " + userId));

        NullChecker.setIfNotNull(user::setName, inputFullUserDto.getName());
        NullChecker.setIfNotNull(user::setEmail, inputFullUserDto.getEmail());
        NullChecker.setIfNotNull(user::setBirthDate, CustomFormatter.stringToDate(inputFullUserDto.getBirthDate()));

        return UserMapper.userToFullDto(userRepository.save(user));
    }
}
