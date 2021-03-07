package com.vanyaadev.bookrepository.service;

import com.vanyaadev.bookrepository.exception.FieldsNotMatchException;
import com.vanyaadev.bookrepository.exception.ResourceAlreadyExistsException;
import com.vanyaadev.bookrepository.model.User;
import com.vanyaadev.bookrepository.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent())
            throw new ResourceAlreadyExistsException("User already exists!");

        if (!newUser.getPassword().equals(newUser.getConfirmedPassword()))
            throw new FieldsNotMatchException("Passwords do not match!");

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
    }


}
