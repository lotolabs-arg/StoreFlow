package domain.configuration;

import domain.common.BaseEntity;
import domain.common.Guard;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "global_configuration")
public class GlobalConfig extends BaseEntity {

    private BigDecimal defaultProfitMargin;

    protected GlobalConfig() {
    }

    public GlobalConfig(BigDecimal defaultProfitMargin) {
        setProfitMargin(defaultProfitMargin);
    }

    public void updateProfitMargin(BigDecimal newMargin) {
        setProfitMargin(newMargin);
    }

    private void setProfitMargin(BigDecimal margin) {
        Guard.againstNegative(margin, "Profit Margin");
        Guard.againstUnrealisticProfitMargin(margin, "Profit Margin");
        this.defaultProfitMargin = margin;
    }

    public BigDecimal getDefaultProfitMargin() {
        return defaultProfitMargin;
    }
}
