package com.dev.phim_pro.services;

import com.dev.phim_pro.dto.*;
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
import org.springframework.beans.factory.annotation.Value;
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
    private final VnPayConfig vnPayConfig;

    public void signUp(RegisterRequest registerRequest) {
        User findUser = userRepository.findByUser(registerRequest.getUsername());
        if (findUser.getUsername().length() > 0) {
            throw new SpringPhimException("User name đã tồn tài!");
        }
        if (registerRequest.getUsername().length() < 5 || registerRequest.getPassword().length() < 5) {
            throw new SpringPhimException("Email and password must bigger than 5");
        }
        if (!registerRequest.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=" +
                ".*[0-9]).{8,}$")) {
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
                user.getEmail(), "https://phim-pro-spring.herokuapp.com/api/v1/auth/accountVerification/" + token
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
        return userRepository.findByUserNameAndEnable(principal.getUsername())
                .orElseThrow(() -> new SpringPhimException("Tài khoản không tồn tại với user name - " + principal.getUsername()));

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

    public AuthenticationResponse loginGoogle(Oauth2Request oauth2Request) {
        Optional<User> check = userRepository.findByEmail(oauth2Request.getEmail());

        if (check.isPresent()) {
            User user = check.get();
//            if (passwordEncoder.matches(user.getPassword(),
//                    oauth2Request.getEmail() + jwtProvider.getGoogleSecret())) {
            user.setName(oauth2Request.getName());
            user.setAvatar(oauth2Request.getAvatar());

            userRepository.save(user);

//            } else {
//                throw new SpringPhimException("Not authenticate accepted!");
//            }

        } else {
            User user = new User();
            String password = oauth2Request.getEmail() + jwtProvider.getGoogleSecret();
            user.setUsername(oauth2Request.getEmail());
            user.setName(oauth2Request.getName());
            user.setPassword(passwordEncoder.encode(password));
            user.setAvatar(oauth2Request.getAvatar());
            user.setEnable(true);
            user.setCreate_time(Instant.now());
            user.setRole(false);
            user.setEmail(oauth2Request.getEmail());

            userRepository.save(user);

        }
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(oauth2Request.getEmail(),
                        oauth2Request.getEmail() + jwtProvider.getGoogleSecret()));
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

    public AuthenticationResponse loginFacebook(Oauth2Request oauth2Request) {
        Optional<User> check = userRepository.findByEmail(oauth2Request.getEmail());

        if (check.isPresent()) {
            User user = check.get();
//            if (passwordEncoder.matches(user.getPassword(),
//                    oauth2Request.getEmail() + jwtProvider.getFacebookSecret())) {
            user.setName(oauth2Request.getName());
            user.setAvatar(oauth2Request.getAvatar());

            userRepository.save(user);

//            } else {
//                throw new SpringPhimException("Not authenticate accepted!");
//            }
        } else {
            User user = new User();
            String password = oauth2Request.getEmail() + jwtProvider.getFacebookSecret();
            user.setUsername(oauth2Request.getEmail());
            user.setName(oauth2Request.getName());
            user.setPassword(passwordEncoder.encode(password));
            user.setAvatar(oauth2Request.getAvatar());
            user.setEnable(true);
            user.setCreate_time(Instant.now());
            user.setRole(false);
            user.setEmail(oauth2Request.getEmail());

            userRepository.save(user);

        }
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(oauth2Request.getEmail(),
                        oauth2Request.getEmail() + jwtProvider.getFacebookSecret()));
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

    public String forgotPassword(String email) {
        Optional<User> check = userRepository.findByEmail(email);
        if (check.isPresent()) {
            User user = check.get();
            String token = jwtProvider.generateTokenWithUserName(user.getUsername());
            mailService.sendMail(new NotificationEmail("Lấy lại mật khẩu của bạn | phimpro",
                    user.getEmail(), vnPayConfig.getClientUrl()+"/reset-password/" + token
            ));
            return "Kiểm tra email của bạn để lấy lại mật khẩu";
        } else {
            throw new SpringPhimException("Email is not exist!");
        }
    }

    public String resetPassword(String password,String token){
        if(!jwtProvider.validateToken(token)){
            throw new SpringPhimException("Invalid token");
        }else{
            String username = jwtProvider.getUsernameFromJwt(token);

            Optional<User> check = userRepository.findByUsername(username);
            if(!check.isPresent()){
                throw new SpringPhimException( "User name not exist!");
            }else{
                User user = check.get();
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                System.out.println(user.toString());
                return "Password changed successfully";
            }
        }
    }
    public List<String> getWatched(Long id) {
        return watchedRepository.findAllByUserId(id);
    }

    public List<String> getFavorites(Long id) {
        return favoriteRepository.findAllByUserId(id);
    }

    public void updateAvatar(AvatarRequest avatar) {
        User user = (User) getCurrentUser();
        user.setAvatar(avatar.getAvatar());
        userRepository.save(user);
    }

    public void updateTypeUser(Long id) {
        userRepository.updateTypeUser(id);
    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


}
