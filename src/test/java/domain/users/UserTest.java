package domain.users;

import domain.common.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should create user successfully with valid data")
    void shouldCreateUser_WithValidData() {
        User admin = new User("admin", "1234", UserRole.ADMIN);

        assertNotNull(admin.getId());
        assertEquals("admin", admin.getUsername());
        assertTrue(admin.isAdmin());
    }

    @Test
    @DisplayName("Should fail when creating user with empty username")
    void shouldFail_WhenUsernameIsEmpty() {
        assertThrows(DomainException.class, () -> {
            new User("", "1234", UserRole.SELLER);
        });
    }

    @Test
    @DisplayName("Should authenticate correctly")
    void shouldAuthenticate_Correctly() {
        User seller = new User("vendedor1", "secret", UserRole.SELLER);

        assertTrue(seller.authenticate("secret"), "Should return true for correct password");
        assertFalse(seller.authenticate("wrong"), "Should return false for wrong password");
    }

    @Test
    @DisplayName("Should identify roles correctly")
    void shouldIdentifyRoles_Correctly() {
        User admin = new User("admin", "1234", UserRole.ADMIN);
        User seller = new User("seller", "1234", UserRole.SELLER);

        assertTrue(admin.isAdmin());
        assertFalse(seller.isAdmin());
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePassword_Successfully() {
        User user = new User("user", "oldPass", UserRole.SELLER);

        user.changePassword("newPass");

        assertTrue(user.authenticate("newPass"));
    }

    @Test
    @DisplayName("Should fail when changing password to the same one")
    void shouldFail_WhenNewPasswordIsSameAsOld() {
        User user = new User("user", "pass", UserRole.SELLER);

        assertThrows(DomainException.class, () -> user.changePassword("pass"));
    }
}
