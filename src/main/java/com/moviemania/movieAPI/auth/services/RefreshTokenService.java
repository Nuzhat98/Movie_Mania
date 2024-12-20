package com.moviemania.movieAPI.auth.services;

import com.moviemania.movieAPI.auth.entities.RefreshToken;
import com.moviemania.movieAPI.auth.entities.User;
import com.moviemania.movieAPI.auth.repositories.RefreshTokenRepository;
import com.moviemania.movieAPI.auth.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    public final UserRepository userRepository;
    public final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(String username){
        User user=userRepository.findByEmail(username).orElseThrow(
                ()->new UsernameNotFoundException("Username not found! with "+username));
        RefreshToken refreshToken=user.getRefreshToken();

        if(refreshToken==null){
            long refreshTokenValidity=5*60*60*10000;
            refreshToken= RefreshToken.builder()
                    .refreshToken(UUID.randomUUID().toString())
                    .expirationTime(Instant.now().plusMillis(refreshTokenValidity))
                    .user(user)
                    .build();
            refreshTokenRepository.save(refreshToken);
        }

        return refreshToken;
    }

    public RefreshToken verifyRefreshToken(String refreshToken){
        RefreshToken refToken= refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new RuntimeException("RefreshToken not found"));
        if(refToken.getExpirationTime().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refToken);
            throw new RuntimeException("Refresh Token expired!");
        }
        return refToken;
    }
}
