package com.example.sber_tz.controller;

import com.example.sber_tz.model.User;
import com.example.sber_tz.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @RequestMapping(value = "/find", params = "id", method = RequestMethod.GET)
    public ResponseEntity<?> findUserById(@RequestParam Long id){
        User user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }
    @RequestMapping(value = "/find", params = {"firstName", "secondName", "patronymic"}, method = RequestMethod.GET)
    public ResponseEntity<?> findUserByEntireName(@RequestParam String firstName, @RequestParam String secondName, @RequestParam String patronymic){
        User user = userService.findUserByEntireName(firstName, secondName, patronymic);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@Valid @RequestBody User user){
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User userChanges){
        User updatedUser = userService.updateUser(userChanges);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
