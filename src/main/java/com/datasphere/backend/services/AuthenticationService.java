package com.datasphere.backend.services;

import com.datasphere.backend.dtos.LoginUserDto;
import com.datasphere.backend.dtos.SignupUserDto;
import com.datasphere.backend.models.UserModel;

public interface AuthenticationService {
    /**
     * Register a new user from the signup DTO.
     * @param dto incoming signup data
     * @return the saved UserModel
     */
    UserModel registerNewUser(SignupUserDto dto);

    UserModel authenticateUser(LoginUserDto dto);

    UserModel getProfileData(String email);
}



