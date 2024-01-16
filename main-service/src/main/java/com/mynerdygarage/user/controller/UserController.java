package com.mynerdygarage.user.controller;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.dto.UserUpdateDto;
import com.mynerdygarage.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public UserFullDto registerNewUser(@RequestBody @Valid NewUserDto newUserDto) {

        return userService.addUser(newUserDto);
    }

    @PatchMapping("/{userId}")
    public UserFullDto update(@PathVariable @NotNull Long userId,
                              @RequestBody @Valid UserUpdateDto userUpdateDto) {

        return userService.update(userId, userUpdateDto);
    }

    @GetMapping("/{userId}")
    public UserFullDto getById(@PathVariable @NotNull Long userId) {

        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable @NotNull Long userId) {

        userService.removeById(userId);
    }
}
