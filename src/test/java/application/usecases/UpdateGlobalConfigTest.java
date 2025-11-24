package application.usecases;

import application.interfaces.GlobalConfigRepository;
import domain.common.DomainException;
import domain.configuration.GlobalConfig;
import domain.users.User;
import domain.users.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UpdateGlobalConfigTest {

    private static final double TEST_MARGIN = 0.50;

    private final GlobalConfigRepository fakeRepo = new GlobalConfigRepository() {
        private GlobalConfig stored = new GlobalConfig(0.10);

        @Override
        public GlobalConfig save(GlobalConfig config) {
            this.stored = config;
            return config;
        }

        @Override
        public Optional<GlobalConfig> find() {
            return Optional.ofNullable(stored);
        }
    };

    private final UpdateGlobalConfig useCase = new UpdateGlobalConfig(fakeRepo);

    @Test
    @DisplayName("Should allow ADMIN to update profit margin")
    void shouldAllowAdminToUpdate() {
        User admin = new User("admin", "1234", UserRole.ADMIN);

        useCase.execute(admin, 0.45);

        assertEquals(0.45, fakeRepo.find()
                .get()
                    .getDefaultProfitMargin());
    }

    @Test
    @DisplayName("Should DENY access to SELLER")
    void shouldDenyAccessToSeller() {
        User seller = new User("vendedor", "1234", UserRole.SELLER);

        Exception exception = assertThrows(DomainException.class, () -> {
            useCase.execute(seller, TEST_MARGIN);
        });

        assertEquals("Access Denied: Only ADMIN can modify global configuration.", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception for unrealistic margin (Typo)")
    void shouldFailOnTypo() {
        User admin = new User("admin", "1234", UserRole.ADMIN);

        assertThrows(DomainException.class, () -> {
            useCase.execute(admin, 50.0);
        });
    }
}
