package com.moviemania.movieAPI.auth.repositories;

import com.moviemania.movieAPI.auth.entities.ForgotPassword;
import com.moviemania.movieAPI.auth.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword,Integer> {

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.otp = ?1 AND fp.user = ?2")
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM ForgotPassword f WHERE f.user = :user")
    void deleteByUser(@Param("user") User user);
}

