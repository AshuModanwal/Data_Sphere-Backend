package com.datasphere.backend.services;

import com.datasphere.backend.dtos.LoginUserDto;
import com.datasphere.backend.dtos.UserDto;
import com.datasphere.backend.models.User;

public interface AuthenticationService {
    /**
     * Register a new user from the signup DTO.
     * @param dto incoming signup data
     * @return the saved UserModel
     */
    User registerNewUser(UserDto dto);

    User authenticateUser(LoginUserDto dto);

    User getProfileData(String email);
}



