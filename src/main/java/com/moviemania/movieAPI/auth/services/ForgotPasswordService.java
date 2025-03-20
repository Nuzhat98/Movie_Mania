package com.moviemania.movieAPI.auth.services;

import com.moviemania.movieAPI.Dto.MailBody;
import com.moviemania.movieAPI.Service.EmailService;
import com.moviemania.movieAPI.auth.entities.ForgotPassword;
import com.moviemania.movieAPI.auth.entities.User;
import com.moviemania.movieAPI.auth.repositories.ForgotPasswordRepository;
import com.moviemania.movieAPI.auth.repositories.UserRepository;
import com.moviemania.movieAPI.auth.utils.ChangePassword;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(UserRepository userRepository, ForgotPasswordRepository forgotPasswordRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ResponseEntity<String> verifyMail(String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found!"));
        int otp=otpGenerator();
        MailBody mailBody= MailBody.builder()
                .to(email)
                .text("Your OTP for forgot password request is "+ otp)
                .subject("OTP for forgot password")
                .build();
        forgotPasswordRepository.deleteByUser(user);
        ForgotPassword forgotPassword=ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                .user(user)
                .build();


        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(forgotPassword);
        return ResponseEntity.ok("Email sent to your mail address");
    }


    public ResponseEntity<String> verifyOtp(Integer otp, String email){
        User user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Email address not found!"));
        ForgotPassword forgotPassword=forgotPasswordRepository.findByOtpAndUser(otp,email).orElseThrow(()-> new RuntimeException("Invalid OTP for email "+email));

        if(forgotPassword.getExpirationTime().before(Date.from(Instant.now()))){
            forgotPasswordRepository.deleteById(forgotPassword.getFpId());
            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP verified successfully!");
    }


    public ResponseEntity<String> changePasswordHandler(ChangePassword changePassword, String email){
        if(!Objects.equals(changePassword.password(),changePassword.repeatPassword())){
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }

        String encodedPassword=passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email,encodedPassword);
        return ResponseEntity.ok("Password has been changed successfully!");
    }

    private Integer otpGenerator(){
        Random random=new Random();
        return random.nextInt(100000,999999);
    }
}
