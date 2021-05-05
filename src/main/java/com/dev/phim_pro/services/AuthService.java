package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.AuthenticationResponse;
import com.dev.phim_pro.dto.AvatarRequest;
import com.dev.phim_pro.dto.LoginRequest;
import com.dev.phim_pro.dto.RegisterRequest;
import com.dev.phim_pro.exceptions.SpringPhimException;
import com.dev.phim_pro.models.NotificationEmail;
import com.dev.phim_pro.models.Token;
import com.dev.phim_pro.models.User;
import com.dev.phim_pro.repository.FavoriteRepository;
import com.dev.phim_pro.repository.TokenRepository;
import com.dev.phim_pro.repository.UserRepository;
import com.dev.phim_pro.repository.WatchedRepository;
import com.dev.phim_pro.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final WatchedRepository watchedRepository;
    private final FavoriteRepository favoriteRepository;

    public void signUp(RegisterRequest registerRequest) {
        if (registerRequest.getUsername().length() < 5 || registerRequest.getPassword().length() < 5) {
            throw new SpringPhimException("Email and password must bigger than 5");
        }
        if(!registerRequest.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=" +
                ".*[0-9]).{8,}$")){
            throw new SpringPhimException("Password must have least a lowercase ,uppercase,digit character " +
                    "and length bigger than 8");
        }
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreate_time(Instant.now());
        user.setEnable(false);
        user.setRole(false);
        user.setAvatar("https://res.cloudinary.com/giangtheshy/image/upload/v1618042500/dev/khumuivietnam/pcwl6uqwzepykmhnpuks.jpg");

        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(new NotificationEmail("Xác nhận e-mail của bạn",
                user.getEmail(), "http://localhost:8080/api/v1/auth/accountVerification/" + token
        ));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        Token verificationToken = new Token();
        verificationToken.setToken(token);
        verificationToken.setUser_id(user.getId());

        tokenRepository.save(verificationToken);
        return token;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new SpringPhimException("User name not found with name - " + principal.getUsername()));

    }

    private void fetchUserAndEnable(Token token) {
        Long uid = token.getUser_id();

        User user = userRepository.findById(uid).orElseThrow(() -> new SpringPhimException(("User not found " +
                "with id - " + uid)));
        user.setEnable(true);
        userRepository.save(user);
    }

    public void verifyAccount(String token) {

        Optional<Token> verificationToken = tokenRepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new SpringPhimException("Invalid token")));
        tokenRepository.deleteByToken(token);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);
        User user = getCurrentUser();
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .name(user.getName())
                .avatar(user.getAvatar())
                .role(user.getRole())
                .type(user.getType())
                .favorites(getFavorites(user.getId()))
                .watched(getWatched(user.getId()))
                .build();
    }

    public AuthenticationResponse refreshToken() {

        User user = getCurrentUser();
        String token = jwtProvider.generateTokenWithUserName(user.getUsername());

        return AuthenticationResponse.builder()
                .name(user.getName())
                .avatar(user.getAvatar())
                .authenticationToken(token)
                .role(user.getRole())
                .favorites(getFavorites(user.getId()))
                .watched(getWatched(user.getId()))
                .type(user.getType())
                .build();

    }
    public List<String> getWatched(Long id){
        return watchedRepository.findAllByUserId(id);
    }
    public List<String> getFavorites(Long id){
        return favoriteRepository.findAllByUserId(id);
    }
    public void updateAvatar(AvatarRequest avatar){
        User user =(User) getCurrentUser();
        user.setAvatar(avatar.getAvatar());
        userRepository.save(user);
    }
    public void updateTypeUser(Long id){
        userRepository.updateTypeUser(id);
    }
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


}
