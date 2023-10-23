package com.mynerdygarage.user.service;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;

public interface UserService {

    UserFullDto addUser(NewUserDto newUserDto);

    UserFullDto update(Long userId, UserFullDto fullUserDto);

    UserFullDto getById(Long userId);

    void removeById(Long userId);
}
