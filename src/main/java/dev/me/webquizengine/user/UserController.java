package dev.me.webquizengine.user;

import dev.me.webquizengine.validation.exceptions.EmailAlreadyTakenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        return userService.tryToAuthenticateUser(user);
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @RequestBody User user) throws EmailAlreadyTakenException {
        userService.registerUser(user);
        return "Registration successful";
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return userService.logoutUser();
    }
}
