package com.example.sber_tz.repository;

import com.example.sber_tz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstNameAndSecondNameAndPatronymic(String firstName, String secondName, String patronymic);
    boolean existsByFirstNameAndSecondNameAndPatronymic(String firstName, String secondName, String patronymic);
}
