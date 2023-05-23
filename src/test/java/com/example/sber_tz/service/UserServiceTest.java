package com.example.sber_tz.service;

import com.example.sber_tz.exception.UserAlreadyExistsException;
import com.example.sber_tz.exception.UserNotFoundException;
import com.example.sber_tz.model.User;
import com.example.sber_tz.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User user = new User(1L, "Andrey", "Lobankov", "Aleksandrovich", "good man");

    @Test
    void whenCallFindUserByIdAndUserExists_thenReturnUser() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Assertions.assertEquals(user, userService.findUserById(user.getId()));
    }

    @Test
    void whenCallFindUserByIdAndUserDoesntExist_thenThrowException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserById(anyLong()));
    }

    @Test
    void whenCallFindUserByEntireNameAndUserExists_thenReturnUser() {
        Mockito.when(userRepository.findByFirstNameAndSecondNameAndPatronymic(user.getFirstName(), user.getSecondName(), user.getPatronymic())).thenReturn(Optional.of(user));
        Assertions.assertEquals(user, userService.findUserByEntireName(user.getFirstName(), user.getSecondName(), user.getPatronymic()));
    }

    @Test
    void whenCallFindUserByEntireNameAndUserDoesntExist_thenThrowException() {
        Mockito.when(userRepository.findByFirstNameAndSecondNameAndPatronymic(anyString(), anyString(), anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.findUserByEntireName(anyString(), anyString(), anyString()));
    }

    @Test
    void whenCallSaveUserAndUserDoesntExist_thenReturnUser() {
        Mockito.when(userRepository.existsByFirstNameAndSecondNameAndPatronymic(user.getFirstName(), user.getSecondName(), user.getPatronymic())).thenReturn(false);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(user, userService.saveUser(user));
    }

    @Test
    void whenCallSaveUserAndUserAlreadyExists_thenThrowException() {
        Mockito.when(userRepository.existsByFirstNameAndSecondNameAndPatronymic(user.getFirstName(), user.getSecondName(), user.getPatronymic())).thenReturn(true);
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.saveUser(user));
    }

    @Test
    void whenCallUpdateUserAndIdIsPresentedAndUserExists_thenReturnUser() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Assertions.assertEquals(user, userService.updateUser(user));
    }

    @Test
    void whenCallUpdateUserAndIdIsNotPresented_thenThrowException() {
        User user1 = new User(null, user.getFirstName(), user.getSecondName(), user.getPatronymic(), user.getDescription());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(user1));
    }

    @Test
    void whenCallUpdateUserAndUserDoesntExist_thenThrowException() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void whenCallDeleteUserAndUserExists() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void whenCallDeleteUserAndUserDoesntExist() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));
    }
}