package infrastructure.ui.controller;

import application.dtos.ProductEntryDTO;
import application.dtos.UpdateProductDTO;
import application.interfaces.ProductRepository;
import application.session.SessionContext;
import application.usecases.RegisterProductEntry;
import application.usecases.UpdateProductDetails;
import domain.common.DomainException;
import domain.stock.Product;
import domain.stock.UnitType;
import domain.users.User;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
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

    @FXML private ListView<Product> productListView;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final RegisterProductEntry registerProductEntry;
    private final UpdateProductDetails updateProductDetails;
    private final ProductRepository productRepository;
    private final SessionContext sessionContext;

    private String selectedProductId;

    public ProductController(RegisterProductEntry registerProductEntry,
                             UpdateProductDetails updateProductDetails,
                             ProductRepository productRepository,
                             SessionContext sessionContext) {
        this.registerProductEntry = registerProductEntry;
        this.updateProductDetails = updateProductDetails;
        this.productRepository = productRepository;
        this.sessionContext = sessionContext;
    }

    @FXML
    public void initialize() {
        refreshList();

        productListView.getSelectionModel().selectedItemProperty()
                .            addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                onProductSelected(newVal);
            }
        });
    }

    private void onProductSelected(Product product) {
        this.selectedProductId = product.getId();

        barcodeField.setText(product.getBarcode().getValue());
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        costField.setText(product.getCost().toString());
        quantityField.setText(product.getStockQuantity().toString());

        if (product.getUnitType() == UnitType.UNIT) {
            unitTypeUnit.setSelected(true);
        } else {
            unitTypeKilo.setSelected(true);
        }

        saveButton.setText("Guardar Cambios");
        cancelButton.setVisible(true);

        quantityField.setDisable(true);
    }

    /**
     * Guarda
     */
    @FXML
    public void onSaveProductClick() {
        try {
            User currentUser = sessionContext.getCurrentUser()
                .orElseThrow(() -> new DomainException("No active session found."));

            if (selectedProductId == null) {
                ProductEntryDTO dto = buildEntryDtoFromForm();
                registerProductEntry.execute(currentUser, dto);
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Producto procesado correctamente.");
            } else {
                if (confirmUpdate()) {
                    UpdateProductDTO dto = buildUpdateDtoFromForm();
                    updateProductDetails.execute(currentUser, dto);
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "Producto modificado correctamente.");
                } else {
                    return;
                }
            }

            clearForm();
            refreshList();

        } catch (DomainException e) {
            showAlert(Alert.AlertType.WARNING, "Validación", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de Formato", "Verifique los números ingresados.");
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Error saving product", e);
            showAlert(Alert.AlertType.ERROR, "Error", "Ocurrió un error inesperado.");
        }
    }

    @FXML
    public void onCancelClick() {
        clearForm();
    }

    private void clearForm() {
        selectedProductId = null;
        barcodeField.clear();
        nameField.clear();
        descriptionField.clear();
        costField.clear();
        quantityField.clear();
        unitTypeUnit.setSelected(true);
        quantityField.setDisable(false);

        saveButton.setText("Guardar / Reponer");
        cancelButton.setVisible(false);

        productListView.getSelectionModel().clearSelection();
        barcodeField.requestFocus();
    }

    private void refreshList() {
        productListView.setItems(FXCollections.observableArrayList(productRepository.findAll()));
        productListView.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " - " + item.getBarcode().getValue());
                }
            }
        });
    }

    private boolean confirmUpdate() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Edición");
        alert.setHeaderText("Está a punto de modificar un producto existente.");
        alert.setContentText("¿Desea continuar?");
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private ProductEntryDTO buildEntryDtoFromForm() {
        return new ProductEntryDTO(
            nameField.getText(),
            descriptionField.getText(),
            barcodeField.getText(),
            getSelectedUnitType(),
            new BigDecimal(quantityField.getText().replace(",", ".")),
            new BigDecimal(costField.getText().replace(",", "."))
        );
    }

    private UpdateProductDTO buildUpdateDtoFromForm() {
        return new UpdateProductDTO(
            selectedProductId,
            nameField.getText(),
            descriptionField.getText(),
            barcodeField.getText(),
            getSelectedUnitType(),
            new BigDecimal(costField.getText().replace(",", "."))
        );
    }

    private UnitType getSelectedUnitType() {
        return unitTypeUnit.isSelected() ? UnitType.UNIT : UnitType.FRACTION;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
