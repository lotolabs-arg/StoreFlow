package application.dtos;

import domain.stock.UnitType;
import java.math.BigDecimal;

public record ProductEntryDTO(
    String name,
    String description,
    String barcode,
    UnitType unitType,
    BigDecimal quantity,
    BigDecimal cost
) {
}
