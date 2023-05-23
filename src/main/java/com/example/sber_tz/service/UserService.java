package com.example.sber_tz.service;

import com.example.sber_tz.exception.UserAlreadyExistsException;
import com.example.sber_tz.exception.UserNotFoundException;
import com.example.sber_tz.model.User;
import com.example.sber_tz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
    }

    public User findUserByEntireName(String firstName, String secondName, String patronymic){
        return userRepository.findByFirstNameAndSecondNameAndPatronymic(firstName, secondName, patronymic).orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
    }

    public User saveUser(User user){
        if (userRepository.existsByFirstNameAndSecondNameAndPatronymic(user.getFirstName(), user.getSecondName(), user.getPatronymic())) throw new UserAlreadyExistsException("USER_ALREADY_EXISTS");
        return userRepository.save(user);
    }

    public User updateUser(User userChanges){
        if (Objects.isNull(userChanges.getId())) throw new UserNotFoundException("NO_ID_PRESENTED");
        User user = userRepository.findById(userChanges.getId()).orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
        if (!Objects.isNull(userChanges.getFirstName())) user.setFirstName(userChanges.getFirstName());
        if (!Objects.isNull(userChanges.getSecondName())) user.setSecondName(userChanges.getSecondName());
        if (!Objects.isNull(userChanges.getPatronymic())) user.setPatronymic(userChanges.getPatronymic());
        if (!Objects.isNull(userChanges.getDescription())) user.setDescription(userChanges.getDescription());
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));
        userRepository.delete(user);
    }
}
