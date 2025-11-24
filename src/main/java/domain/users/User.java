package domain.users;

import domain.common.BaseEntity;
import domain.common.DomainException;
import domain.common.Guard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Represents a system user (Administrator or Seller).
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    protected User() {
    }

    public User(String username, String password, UserRole role) {
        Guard.againstNullOrEmpty(username, "Username");
        Guard.againstNullOrEmpty(password, "Password");
        Guard.againstNull(role, "User Role");

        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Validates if the provided password matches the user's password.
     *
     * @param candidatePassword the password to check
     * @return true if matches, false otherwise
     */
    public boolean authenticate(String candidatePassword) {
        Guard.againstNullOrEmpty(candidatePassword, "Candidate password");
        return this.password.equals(candidatePassword);
    }

    public void changePassword(String newPassword) {
        Guard.againstNullOrEmpty(newPassword, "New password");
        if (newPassword.equals(this.password)) {
            throw new DomainException("New password cannot be the same as the old one.");
        }
        this.password = newPassword;
    }

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
