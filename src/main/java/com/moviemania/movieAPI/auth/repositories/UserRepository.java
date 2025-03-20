package com.moviemania.movieAPI.auth.repositories;

import com.moviemania.movieAPI.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {


    Optional<User> findByEmail(String username);

    @Transactional
    @Query("UPDATE User u SET u.password=?2 WHERE u.email=?1")
    void updatePassword(String email,String password);
}
