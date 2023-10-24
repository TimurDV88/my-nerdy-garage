package com.mynerdygarage.user.controller;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto add(@RequestBody @Valid NewUserDto newUserDto) {

        return userService.addUser(newUserDto);
    }

    @PatchMapping
    public UserFullDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody @Valid UserFullDto userFullDto) {

        return userService.update(userId, userFullDto);
    }


}
