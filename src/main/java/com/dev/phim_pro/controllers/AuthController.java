package com.dev.phim_pro.controllers;

import com.dev.phim_pro.dto.*;
import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        try {
            authService.signUp(registerRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), NOT_ACCEPTABLE);
        }

        return new ResponseEntity<>("Activation your email to login", OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("<div style=\"max-width: 700px; margin:auto; border: 10px solid #ddd; padding: 50px 20px; font-size: 110%;text-align:center;\">\n" +
                "    <h2 style=\"text-align: center; text-transform: uppercase;color: teal;\">Welcome to\n" +
                "        Phim Pro.</h2>\n" +
                "    <p>Congratulations on successful registration of an account at Phim Pro.\n" +
                "       Email has been verified, please click the button below to login\n" +
                "    </p>\n" +
                "\n" +
                "    <a href=\"" + "https://phim-pro.netlify.app/account" + "\"\n" +
                "       style=\"background: crimson; text-decoration: none; color: white; padding: 10px " +
                "20px; margin: 10px 0; display: inline-block;border-radius:0.5rem;font-weight: bold;" +
                "box-shadow:0 3px 6px crimson;text-transform: uppercase\">Confirm</a>\n" +
                "\n" +
                "    <p>If you clicked on the button above but failed, you can go to the website address below:</p>\n" +
                "\n" +
                "    <span >" + "https://phim-pro.netlify.app/account" + "</span>\n" +
                "</div>", OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/login_google")
    public ResponseEntity<AuthenticationResponse> loginGoogle(@RequestBody Oauth2Request oauth2Request) {
        try {
            return ResponseEntity.status(OK).body(authService.loginGoogle(oauth2Request));
        } catch (Exception e) {
            throw new SpringPhimException("Can not login with gooogle", e);
        }
    }

    @PostMapping("/login_facebook")
    public ResponseEntity<AuthenticationResponse> loginFacebook(@RequestBody Oauth2Request oauth2Request) {
        try {
            return ResponseEntity.status(OK).body(authService.loginFacebook(oauth2Request));
        } catch (Exception e) {
            throw new SpringPhimException("Can not login with facebook", e);
        }

    }

    @GetMapping("/refresh/token")
    public AuthenticationResponse refreshToken() {
        return authService.refreshToken();

    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(OK).body("Refresh token deleted successfully!!");
    }

    @PutMapping("/avatar")
    public void updateAvatar(@RequestBody AvatarRequest avatar) {
        authService.updateAvatar(avatar);
    }

    @GetMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email) {
        try {
            return ResponseEntity.status(OK).body(authService.forgotPassword(email));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@RequestBody RequestResetPassword requestResetPassword,
                                                @PathVariable String token) {
       try{
           if (requestResetPassword.getPassword().equals(requestResetPassword.getConfirmPassword())) {
               return ResponseEntity.status(OK).body(authService.resetPassword(requestResetPassword.getPassword(), token));
           } else {
               return ResponseEntity.status(NOT_ACCEPTABLE).body("Password not matches!");
           }
       }catch (Exception e){
           return ResponseEntity.status(NOT_ACCEPTABLE).body(e.getMessage());
       }
    }
}
