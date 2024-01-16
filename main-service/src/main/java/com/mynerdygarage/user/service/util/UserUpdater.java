package com.mynerdygarage.user.service.util;

import com.mynerdygarage.user.dto.UserUpdateDto;
import com.mynerdygarage.user.model.User;
import com.mynerdygarage.util.CustomFormatter;
import com.mynerdygarage.util.NullChecker;

public class UserUpdater {

    public static User update(User user, UserUpdateDto userUpdateDto) {

        NullChecker.setIfNotNull(user::setName, userUpdateDto.getName());
        NullChecker.setIfNotNull(user::setEmail, userUpdateDto.getEmail());
        NullChecker.setIfNotNull(user::setBirthDate, CustomFormatter.stringToDate(userUpdateDto.getBirthDate()));

        return user;
    }
}
