package edinho.auth.services;

import edinho.auth.entities.LoginRequestDTO;
import edinho.auth.entities.TokenResponseDTO;
import edinho.auth.entities.User;
import edinho.auth.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(UUID.fromString(id));
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean matchCredentials(LoginRequestDTO req, User user) {
        return bCryptPasswordEncoder.matches(req.password(), user.getPassword());
    }

    public boolean matchCredentials(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.filter(value -> bCryptPasswordEncoder.matches(password, value.getPassword())).isPresent();
    }

    public void createUser(String username, String password) {
        User user = new User(username, bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public TokenResponseDTO generateToken(String subject) {
        Instant now = Instant.now();
        int expiresIn = 300;
        var claims = JwtClaimsSet
                .builder()
                .issuer("origin")
                .subject(subject)
                .expiresAt(now.plusSeconds(expiresIn))
                .issuedAt(now)
                .build();

        return new TokenResponseDTO(jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue(), expiresIn);
    }
}
