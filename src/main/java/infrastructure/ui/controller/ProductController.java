package infrastructure.ui.controller;

import application.dtos.ProductEntryDTO;
import application.session.SessionContext;
import application.usecases.RegisterProductEntry;
import domain.common.DomainException;
import domain.stock.UnitType;
import domain.users.User;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class ProductController {

    private static final Logger LOGGER = Logger.getLogger(ProductController.class.getName());

    @FXML private TextField barcodeField;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField costField;
    @FXML private TextField quantityField;
    @FXML private ToggleGroup unitTypeGroup;
    @FXML private RadioButton unitTypeUnit;
    @FXML private RadioButton unitTypeKilo;

    private final RegisterProductEntry registerProductEntry;
    private final SessionContext sessionContext;

    public ProductController(RegisterProductEntry registerProductEntry, SessionContext sessionContext) {
        this.registerProductEntry = registerProductEntry;
        this.sessionContext = sessionContext;
    }

    /**
     * Handles the "Save/Restock" button click event.
     * <p>
     * This method orchestrates the product entry process by:
     * 1. Retrieving the currently logged-in user from the session context.
     * 2. transforming the raw form data into a structured {@link ProductEntryDTO}.
     * 3. Invoking the {@link RegisterProductEntry} use case to execute the business logic.
     * 4. Providing feedback to the user via Alerts (Success, Validation Warning, or System Error).
     * </p>
     *
     * @throws DomainException       if the business rules fail (e.g., duplicate barcode, invalid stock).
     * Handled by showing a WARNING alert.
     * @throws NumberFormatException if the numeric fields (Cost, Quantity) contain invalid characters.
     * Handled by showing an ERROR alert.
     * @throws RuntimeException      if an unexpected system error occurs.
     * Handled by logging the error and showing a generic ERROR alert.
     */
    @FXML
    public void onSaveProductClick() {
        try {
            User currentUser = sessionContext.getCurrentUser()
                .orElseThrow(() -> new DomainException("No active session found."));

            ProductEntryDTO dto = buildDtoFromForm();

            registerProductEntry.execute(currentUser, dto);

            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Producto guardado/actualizado correctamente.");
            clearForm();

        } catch (DomainException e) {
            showAlert(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato", "Por favor verifique que Costo y Cantidad sean números"
                + " válidos.");
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Unexpected error saving product", e);
            showAlert(Alert.AlertType.ERROR, "Error del Sistema", "Ocurrió un error inesperado.");
        }
    }

    private ProductEntryDTO buildDtoFromForm() {
        String barcode = barcodeField.getText();
        String name = nameField.getText();
        String description = descriptionField.getText();

        BigDecimal cost = new BigDecimal(costField.getText().replace(",", "."));
        BigDecimal quantity = new BigDecimal(quantityField.getText().replace(",", "."));

        UnitType type = unitTypeUnit.isSelected() ? UnitType.UNIT : UnitType.FRACTION;

        return new ProductEntryDTO(name, description, barcode, type, quantity, cost);
    }

    private void clearForm() {
        barcodeField.clear();
        nameField.clear();
        descriptionField.clear();
        costField.clear();
        quantityField.clear();
        unitTypeUnit.setSelected(true);
        barcodeField.requestFocus();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
