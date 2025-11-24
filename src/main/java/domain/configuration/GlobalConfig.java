package domain.configuration;

import domain.common.BaseEntity;
import domain.common.Guard;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Represents the system-wide configuration.
 * Intended to be a singleton (only one record in the database).
 */
@Entity
@Table(name = "global_configuration")
public class GlobalConfig extends BaseEntity {

    private double defaultProfitMargin;

    protected GlobalConfig() {
    }

    public GlobalConfig(double defaultProfitMargin) {
        setProfitMargin(defaultProfitMargin);
    }

    public void updateProfitMargin(double newMargin) {
        setProfitMargin(newMargin);
    }

    private void setProfitMargin(double margin) {
        Guard.againstNegative(margin, "Profit Margin");
        Guard.againstUnrealisticProfitMargin(margin, "Profit Margin");
        this.defaultProfitMargin = margin;
    }

    public double getDefaultProfitMargin() {
        return defaultProfitMargin;
    }
}
