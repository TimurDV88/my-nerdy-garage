package com.mynerdygarage.user.dto;


import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;

import java.util.ArrayList;
import java.util.List;

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

        List<UserFullDto> toReturn = new ArrayList<>();

        for (User user : users) {
            toReturn.add(modelToFullDto(user));
        }

        return toReturn;
    }

    public static UserShortDto modelToShortDto(User user) {

        return user != null ? new UserShortDto(user.getId(), user.getName()) : null;
    }

    public static List<UserShortDto> modelToShortDto(Iterable<User> users) {

        List<UserShortDto> userShortDtoList = new ArrayList<>();

        for (User user : users) {
            userShortDtoList.add(modelToShortDto(user));
        }

        return userShortDtoList;
    }
}
