package com.example.sber_tz.controller;

import com.example.sber_tz.controller.exceptionHandler.ExceptionHandler;
import com.example.sber_tz.exception.UserAlreadyExistsException;
import com.example.sber_tz.exception.UserNotFoundException;
import com.example.sber_tz.model.User;
import com.example.sber_tz.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final User user = new User(1L, "Andrey", "Lobankov", "Aleksandrovich", "good man");

    @Test
    @SneakyThrows
    void whenGetRequestToFindUserByIdAndUserExists_thenCorrectResponse() {
        Mockito.when(userService.findUserById(1L)).thenReturn(user);

        String expected = objectMapper.writeValueAsString(user);
        this.mockMvc
                .perform(get("/api/v1/user/find")
                        .param("id", String.valueOf(user.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenGetRequestToFindUserByIdAndUserDoesntExist_thenCorrectResponse() {
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("USER_NOT_FOUND"));
        Mockito.when(userService.findUserById(1L)).thenThrow(new UserNotFoundException("USER_NOT_FOUND"));
        this.mockMvc
                .perform(get("/api/v1/user/find")
                        .param("id", String.valueOf(user.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenGetRequestToFindUserByEntireNameAndUserExists_thenCorrectResponse() {
        Mockito.when(userService.findUserByEntireName(user.getFirstName(), user.getSecondName(), user.getPatronymic())).thenReturn(user);

        String expected = objectMapper.writeValueAsString(user);
        this.mockMvc
                .perform(get("/api/v1/user/find")
                        .param("firstName", user.getFirstName())
                        .param("secondName", user.getSecondName())
                        .param("patronymic", user.getPatronymic()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenGetRequestToFindUserByEntireNameAndUserDoesntExist_thenCorrectResponse() {
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("USER_NOT_FOUND"));
        Mockito.when(userService.findUserByEntireName(user.getFirstName(), user.getSecondName(), user.getPatronymic())).thenThrow(new UserNotFoundException("USER_NOT_FOUND"));
        this.mockMvc
                .perform(get("/api/v1/user/find")
                        .param("firstName", user.getFirstName())
                        .param("secondName", user.getSecondName())
                        .param("patronymic", user.getPatronymic()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenPostRequestToSaveUserAndUserIsValid_thenCorrectResponse() {
        String expected = objectMapper.writeValueAsString(user);
        Mockito.when(userService.saveUser(notNull())).thenReturn(user);
        Map<String, String> body = Map.of(
                "firstName", user.getFirstName(),
                "secondName", user.getSecondName(),
                "patronymic", user.getPatronymic(),
                "description", user.getPatronymic()
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        this.mockMvc.perform(post("/api/v1/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenPostRequestToSaveUserAndUserIsValidAndAlreadyExists_thenCorrectResponse() {
        Mockito.when(userService.saveUser(notNull())).thenThrow(new UserAlreadyExistsException("USER_ALREADY_EXISTS"));
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("USER_ALREADY_EXISTS"));
        Map<String, String> body = Map.of(
                "firstName", user.getFirstName(),
                "secondName", user.getSecondName(),
                "patronymic", user.getPatronymic(),
                "description", user.getPatronymic()
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        this.mockMvc.perform(post("/api/v1/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenPostRequestToSaveUserAndUserIsNotValid_thenCorrectAnswer() {
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("ALL_FIELDS_ARE_REQUIRED"));
        Map<String, String> body = Map.of(
                "firstName", user.getFirstName(),
                "secondName", user.getSecondName()
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        this.mockMvc.perform(post("/api/v1/user/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenPutRequestToUpdateUserAndUserIdIsPresented_thenCorrectAnswer() {
        Mockito.when(userService.updateUser(notNull())).thenReturn(user);
        String expected = objectMapper.writeValueAsString(user);
        Map<String, String> body = Map.of(
                "id", String.valueOf(user.getId()),
                "firstName", user.getFirstName(),
                "secondName", user.getSecondName(),
                "patronymic", user.getPatronymic(),
                "description", user.getPatronymic()
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        this.mockMvc.perform(put("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenPutRequestToUpdateUserAndUserIdIsNotPresented_thenCorrectAnswer() {
        Mockito.when(userService.updateUser(notNull())).thenThrow(new UserNotFoundException("NO_ID_PRESENTED"));
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("NO_ID_PRESENTED"));
        Map<String, String> body = Map.of(
                "firstName", "testFirstName"
        );
        String jsonBody = objectMapper.writeValueAsString(body);

        this.mockMvc.perform(put("/api/v1/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }

    @Test
    @SneakyThrows
    void whenDeleteRequestToDeleteUserAndUserExists_thenCorrectAnswer() {
        this.mockMvc
                .perform(delete("/api/v1/user/delete")
                        .param("id", String.valueOf(user.getId())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void whenDeleteRequestToDeleteUserAndUserDoesntExist_thenCorrectAnswer() {
        Mockito.doThrow(new UserNotFoundException("USER_NOT_FOUND")).when(userService).deleteUser(anyLong());
        String expected = objectMapper.writeValueAsString(new ExceptionHandler.ExceptionResponse("USER_NOT_FOUND"));
        this.mockMvc
                .perform(delete("/api/v1/user/delete")
                        .param("id", String.valueOf(user.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expected));
    }
}