package com.mynerdygarage.user.controller;

import com.mynerdygarage.user.dto.NewUserDto;
import com.mynerdygarage.user.dto.UserFullDto;
import com.mynerdygarage.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @PatchMapping("/{userId}")
    public UserFullDto update(@PathVariable @NotNull Long userId,
                              @RequestBody @Valid UserFullDto userFullDto) {

        return userService.update(userId, userFullDto);
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
