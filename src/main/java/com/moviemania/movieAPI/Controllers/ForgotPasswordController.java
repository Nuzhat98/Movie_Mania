package com.moviemania.movieAPI.Controllers;


import com.moviemania.movieAPI.auth.services.ForgotPasswordService;
import com.moviemania.movieAPI.auth.utils.ChangePassword;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyMail(@PathVariable(name = "email") String email){
        return forgotPasswordService.verifyMail(email);
    }

    @PostMapping("/verify/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable(name = "otp")Integer otp,
                                            @PathVariable(name="email")String email){
        return forgotPasswordService.verifyOtp(otp,email);
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable(name = "email") String email){
        return forgotPasswordService.changePasswordHandler(changePassword, email);
    }


}
