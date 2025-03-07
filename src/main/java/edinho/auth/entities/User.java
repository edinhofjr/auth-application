package edinho.auth.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    public User(String username, String encode) {
        this.username = username;
        this.password = encode;
    }

    public User() {

    }

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
