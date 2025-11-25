package application.dtos;

import domain.stock.UnitType;
import java.math.BigDecimal;

public record UpdateProductDTO(
    String id,
    String name,
    String description,
    String barcode,
    UnitType unitType,
    BigDecimal cost
) {
}
