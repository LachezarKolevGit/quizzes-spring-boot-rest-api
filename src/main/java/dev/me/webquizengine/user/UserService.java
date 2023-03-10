package dev.me.webquizengine.user;

import dev.me.webquizengine.security.jwt.JwtCookie;
import dev.me.webquizengine.validation.exceptions.EmailAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider dao;
    private final JwtCookie jwtCookie;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationProvider dao, JwtCookie jwtCookie) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.dao = dao;
        this.jwtCookie = jwtCookie;
    }

    public User registerUser(User user) throws EmailAlreadyTakenException {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyTakenException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Roles.USER);

        return userRepository.save(user);
    }

    public ResponseEntity<?> tryToAuthenticateUser(User user) {
        Authentication authentication = dao.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user1 = new User(userDetails.getUsername());
        ResponseCookie responseCookie = jwtCookie.generateJwtCookie(user1);


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Login successful");
    }

    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName());
    }

    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtCookie.getCleanJwtCookie();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Signed out successfully");
    }

}
