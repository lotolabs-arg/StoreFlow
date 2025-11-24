package application.usecases;

import application.interfaces.GlobalConfigRepository;
import domain.common.DomainException;
import domain.configuration.GlobalConfig;
import domain.users.User;
import domain.users.UserRole;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateGlobalConfigTest {

    private static final BigDecimal TEST_MARGIN = new BigDecimal("0.50");

    private final GlobalConfigRepository fakeRepo = new GlobalConfigRepository() {
        private GlobalConfig stored = new GlobalConfig(new BigDecimal("0.10"));

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

        useCase.execute(admin, new BigDecimal("0.45"));

        assertEquals(new BigDecimal("0.45"), fakeRepo.find()
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
            useCase.execute(admin, new BigDecimal("50.0"));
        });
    }
}
