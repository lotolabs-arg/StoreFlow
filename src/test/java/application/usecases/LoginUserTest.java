package application.usecases;

import application.interfaces.UserRepository;
import application.session.SessionContext;
import domain.common.DomainException;
import domain.users.User;
import domain.users.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoginUserTest {

    private LoginUser loginUser;
    private SessionContext sessionContext;

    private final UserRepository fakeRepo = new UserRepository() {
        @Override
        public User save(User user) { return user; }
        @Override
        public Optional<User> findById(String id) { return Optional.empty(); }

        @Override
        public Optional<User> findByUsername(String username) {
            if ("admin".equals(username)) {
                return Optional.of(new User("admin", "1234", UserRole.ADMIN));
            }
            return Optional.empty();
        }
    };

    @BeforeEach
    void setUp() {
        sessionContext = SessionContext.getInstance();
        sessionContext.logout();
        loginUser = new LoginUser(fakeRepo, sessionContext);
    }

    @Test
    @DisplayName("Should login successfully with correct credentials")
    void shouldLoginSuccessfully() {
        User user = loginUser.execute("admin", "1234");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());

        assertTrue(sessionContext.getCurrentUser().isPresent());
        assertEquals("admin", sessionContext.getCurrentUser().get().getUsername());
    }

    @Test
    @DisplayName("Should fail with incorrect password")
    void shouldFail_WithIncorrectPassword() {
        DomainException exception = assertThrows(DomainException.class, () -> {
            loginUser.execute("admin", "wrongpass");
        });

        assertEquals("Invalid credentials.", exception.getMessage());
        assertFalse(sessionContext.getCurrentUser().isPresent());
    }

    @Test
    @DisplayName("Should fail with non-existent user")
    void shouldFail_WithNonExistentUser() {
        assertThrows(DomainException.class, () -> {
            loginUser.execute("ghost", "1234");
        });
        assertFalse(sessionContext.getCurrentUser().isPresent());
    }
}
