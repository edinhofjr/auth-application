package edinho.auth.controllers;

import edinho.auth.entities.*;
import edinho.auth.services.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(JwtEncoder jwtEncoder, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUserDTO registerUserDTO) {
        Optional<User> user = userService.findByUsername(registerUserDTO.username());
        if (user.isPresent()) {
            return ResponseEntity.badRequest().body("user exists");
        }
        userService.createUser(registerUserDTO.username(), registerUserDTO.password());
        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        User user = userService.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!userService.matchCredentials(req, user)) {
            new RuntimeException("Invalid credentials");
        };

        TokenResponseDTO token = userService.generateToken(user.getId().toString());

        return ResponseEntity.ok(token);
    }

    @GetMapping("/token")
    public ResponseEntity<?> validateToken(@RequestBody Test t,  JwtAuthenticationToken token) {
        Optional<User> user = userService.findById(token.getName());
        
        return ResponseEntity.ok(user);
    }
}
