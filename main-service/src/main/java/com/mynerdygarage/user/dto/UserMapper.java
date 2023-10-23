package com.mynerdygarage.user.dto;


import com.mynerdygarage.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserFullDto userToFullDto(User user) {

        return new UserFullDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                String.valueOf(user.getBirthDate()),
                user.getRegDate()
        );
    }

    public static List<UserFullDto> userToFullDto(Iterable<User> users) {

        List<UserFullDto> toReturn = new ArrayList<>();

        for (User user : users) {
            toReturn.add(userToFullDto(user));
        }

        return toReturn;
    }

    public static UserShortDto userToShortDto(User user) {

        return new UserShortDto(user.getId(), user.getName());
    }

    public static List<UserShortDto> userToShortDto(Iterable<User> users) {

        List<UserShortDto> userShortDtoList = new ArrayList<>();

        for (User user : users) {
            userShortDtoList.add(userToShortDto(user));
        }

        return userShortDtoList;
    }
}
