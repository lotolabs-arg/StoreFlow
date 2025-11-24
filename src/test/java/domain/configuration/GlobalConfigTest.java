package domain.configuration;

import domain.common.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GlobalConfigTest {

    @Test
    @DisplayName("Should create config with valid profit margin (0.30)")
    void shouldCreateConfig_WithValidMargin() {
        GlobalConfig config = new GlobalConfig(0.30);

        assertNotNull(config.getId());
        assertEquals(0.30, config.getDefaultProfitMargin());
    }

    @Test
    @DisplayName("Should allow high profit margins like 200% (2.0)")
    void shouldAllowHighProfitMargin() {
        GlobalConfig config = new GlobalConfig(2.0);
        assertEquals(2.0, config.getDefaultProfitMargin());
    }

    @Test
    @DisplayName("Should fail when setting negative profit margin")
    void shouldFail_WhenMarginIsNegative() {
        assertThrows(DomainException.class, () -> {
            new GlobalConfig(-0.10);
        });
    }

    @Test
    @DisplayName("Should fail when setting unrealistic profit margin (Typo check)")
    void shouldFail_WhenMarginIsUnrealistic() {
        assertThrows(DomainException.class, () -> {
            new GlobalConfig(50.0);
        }, "Should throw exception for likely typo");
    }

    @Test
    @DisplayName("Should update profit margin successfully")
    void shouldUpdateProfitMargin() {
        GlobalConfig config = new GlobalConfig(0.30);

        config.updateProfitMargin(0.45);

        assertEquals(0.45, config.getDefaultProfitMargin());
    }
}
