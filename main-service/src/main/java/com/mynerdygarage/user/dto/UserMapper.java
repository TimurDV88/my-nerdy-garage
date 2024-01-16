package com.mynerdygarage.user.dto;


import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;

import java.util.List;
import java.util.stream.StreamSupport;

public class UserMapper {

    public static UserFullDto modelToFullDto(User user) {

        return new UserFullDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                CustomFormatter.dateToString(user.getBirthDate()),
                CustomFormatter.dateTimeToString(user.getRegDate())
        );
    }

    public static List<UserFullDto> modelToFullDto(Iterable<User> users) {

        return StreamSupport.stream(users.spliterator(), false)
                .map(UserMapper::modelToFullDto)
                .toList();
    }

    public static UserShortDto modelToShortDto(User user) {

        return user != null ? new UserShortDto(user.getId(), user.getName()) : null;
    }

    public static List<UserShortDto> modelToShortDto(Iterable<User> users) {

        return StreamSupport.stream(users.spliterator(), false)
                .map(UserMapper::modelToShortDto)
                .toList();
    }
}
