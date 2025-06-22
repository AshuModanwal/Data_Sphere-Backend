package com.datasphere.backend.services.impl;

import com.datasphere.backend.dtos.LoginUserDto;
import com.datasphere.backend.dtos.UserDto;
import com.datasphere.backend.models.User;
import com.datasphere.backend.repositories.UserRepository;
import com.datasphere.backend.services.AuthenticationService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class AuthenticationImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationImpl(
            UserRepository userRepository, PasswordEncoder passwordEncoder
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerNewUser(UserDto dto){
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User user = new User();
        user.setUserName(dto.getUserName());
        user.setEmail(dto.getEmail());
        user.setCompany(dto.getCompany());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return userRepository.save(user);

    }

    @Override
    public User authenticateUser(LoginUserDto dto){
        String email = dto.getEmail();
        String password = dto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Password is incorrect");
        }

        System.out.println("User: "+ user);

        return user;
    }

    @Override
    public User getProfileData(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }




}
