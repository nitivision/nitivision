package com.niti.vision.service;

import java.util.List;

import com.niti.vision.dto.UserDto;
import com.niti.vision.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();
}
