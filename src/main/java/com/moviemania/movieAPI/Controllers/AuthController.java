package com.moviemania.movieAPI.Controllers;

import com.moviemania.movieAPI.Service.AuthService;
import com.moviemania.movieAPI.auth.entities.RefreshToken;
import com.moviemania.movieAPI.auth.entities.User;
import com.moviemania.movieAPI.auth.services.JwtService;
import com.moviemania.movieAPI.auth.services.RefreshTokenService;
import com.moviemania.movieAPI.auth.utils.AuthResponse;
import com.moviemania.movieAPI.auth.utils.LoginRequest;
import com.moviemania.movieAPI.auth.utils.RefreshTokenRequest;
import com.moviemania.movieAPI.auth.utils.RegisterRequest;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
       RefreshToken refreshToken= refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
       User user=refreshToken.getUser();
       String accessToken=jwtService.generateToken(user);
       return ResponseEntity.ok(AuthResponse.builder()
               .accessToken(accessToken)
               .refreshToken(refreshToken.getRefreshToken())
               .build());
    }
}
