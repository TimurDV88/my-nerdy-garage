package com.mynerdygarage.user.service;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserUpdateDto;

public interface UserService {

    UserFullDto addUser(NewUserDto newUserDto);

    UserFullDto update(Long userId, UserUpdateDto userUpdateDto);

    UserFullDto getById(Long userId);

    void removeById(Long userId);
}
