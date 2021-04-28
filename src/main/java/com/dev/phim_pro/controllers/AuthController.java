package com.dev.phim_pro.controllers;

import com.dev.phim_pro.dto.AuthenticationResponse;
import com.dev.phim_pro.dto.LoginRequest;
import com.dev.phim_pro.dto.RegisterRequest;
import com.dev.phim_pro.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest){
     try{
       authService.signUp(registerRequest);
     }catch (Exception e){
         return new ResponseEntity<>(e.getMessage(), NOT_ACCEPTABLE);
     }

        return new ResponseEntity<>("Activation your email to login", OK);
    }
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("<div style=\"max-width: 700px; margin:auto; border: 10px solid #ddd; padding: 50px 20px; font-size: 110%;text-align:center;\">\n" +
                "    <h2 style=\"text-align: center; text-transform: uppercase;color: teal;\">Welcome to\n" +
                "        Phim Pro.</h2>\n" +
                "    <p>Congratulations on successful registration of an account at Phim Pro.\n" +
                "       Email has been verified, please click the button below to login\n" +
                "    </p>\n" +
                "\n" +
                "    <a href=\"" +"http://localhost:3000/account"  + "\"\n" +
                "       style=\"background: crimson; text-decoration: none; color: white; padding: 10px " +
                "20px; margin: 10px 0; display: inline-block;border-radius:0.5rem;font-weight: bold;" +
                "box-shadow:0 3px 6px crimson;text-transform: uppercase\">Confirm</a>\n" +
                "\n" +
                "    <p>If you clicked on the button above but failed, you can go to the website address below:</p>\n" +
                "\n" +
                "    <span >" + "http://localhost:3000/account" + "</span>\n" +
                "</div>", OK);
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);
    }
    @GetMapping("/refresh/token")
    public AuthenticationResponse refreshToken(){
       return authService.refreshToken();

    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(OK).body("Refresh token deleted successfully!!");
    }
}
