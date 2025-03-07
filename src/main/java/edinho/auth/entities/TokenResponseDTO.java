package edinho.auth.entities;

public record TokenResponseDTO(String token, long expiresAt) {
    @Override
    public String token() {
        return token;
    }

    @Override
    public long expiresAt() {
        return expiresAt;
    }
}
