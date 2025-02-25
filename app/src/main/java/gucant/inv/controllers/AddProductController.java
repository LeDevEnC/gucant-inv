package gucant.inv.controllers;

import java.io.IOException;
import gucant.inv.models.dao.ProduitDAO;
import gucant.inv.models.data.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import gucant.inv.models.dao.CategoryDAO;
import gucant.inv.models.dao.SupplierDAO;
import gucant.inv.utils.StringSimilarity;
import java.util.List;
import java.util.stream.Collectors;

public class AddProductController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField specificationsField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceHTField;
    @FXML
    private TextField priceTTCField;
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private ComboBox<String> supplierBox;

    private ProduitDAO produitDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    private static final double TVA_RATE = 0.2; // Assuming a 20% VAT rate

    public AddProductController() {
        produitDAO = new ProduitDAO();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();
    }

    private boolean isUpdating = false;

    @FXML
    public void initialize() {
        categoryBox.getItems().addAll(categoryDAO.getAllCategories());
        supplierBox.getItems().addAll(supplierDAO.getAllSuppliers());

        // Remplacer les deux listeners existants par ceux-ci
        priceHTField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isUpdating) {
                try {
                    isUpdating = true;
                    if (newValue.isEmpty()) {
                        priceTTCField.setText("");
                    } else {
                        String cleanValue = newValue.replace(",", ".");
                        double priceHT = Double.parseDouble(cleanValue);
                        double priceTTC = priceHT * (1 + TVA_RATE);
                        priceTTCField.setText(String.format("%.2f", priceTTC));
                    }
                } catch (NumberFormatException e) {
                    priceTTCField.setText("");
                } finally {
                    isUpdating = false;
                }
            }
        });
        
        // Listener pour le prix TTC
        priceTTCField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isUpdating) {
                try {
                    isUpdating = true;
                    if (newValue.isEmpty()) {
                        priceHTField.setText("");
                    } else {
                        String cleanValue = newValue.replace(",", ".");
                        double priceTTC = Double.parseDouble(cleanValue);
                        double priceHT = priceTTC / (1 + TVA_RATE); // Correction de la formule
                        priceHTField.setText(String.format("%.2f", priceHT));
                    }
                } catch (NumberFormatException e) {
                    priceHTField.setText("");
                } finally {
                    isUpdating = false;
                }
            }
        });
        
    }

    private boolean isProductSimilarToExisting(String newProductName) {
        List<String> existingProductNames = produitDAO.getAll()
                .stream()
                .map(Produit::getName)
                .collect(Collectors.toList());

        for (String existingName : existingProductNames) {
            if (StringSimilarity.areSimilar(newProductName, existingName)) {
                return true;
            }
        }
        return false;
    }

    private void showSimilarProductAlert(String productName) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Produit similaire détecté");
        alert.setHeaderText("Un produit similaire existe déjà");
        alert.setContentText("Le produit '" + productName + "' semble être similaire à un produit existant. " +
                "Veuillez vérifier la liste des produits existants.");
        alert.showAndWait();
    }

    @FXML
    private void handleAddProduct(ActionEvent event) throws IOException {
        try {
            String name = nameField.getText();
            String specifications = specificationsField.getText();

            if (name.isEmpty() || specifications.isEmpty() ||
                    categoryBox.getSelectionModel().getSelectedIndex() == -1 ||
                    supplierBox.getSelectionModel().getSelectedIndex() == -1) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Champs manquants");
                alert.setContentText("Tous les champs doivent être remplis !");
                alert.showAndWait();
                return;
            }

            if (isProductSimilarToExisting(name)) {
                showSimilarProductAlert(name);
                return;
            }

            int quantity = Integer.parseInt(quantityField.getText().replace(",", "."));
            double priceHT = Double.parseDouble(priceHTField.getText().replace(",", "."));
            double priceTTC = Double.parseDouble(priceTTCField.getText().replace(",", "."));

            if (quantity < 0 || priceHT < 0 || priceTTC < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Valeurs invalides");
                alert.setContentText("La quantité et les prix doivent être positifs !");
                alert.showAndWait();
                return;
            }

            int categoryId = categoryBox.getSelectionModel().getSelectedIndex() + 1;
            int supplierId = supplierBox.getSelectionModel().getSelectedIndex() + 1;

            Produit produit = new Produit(name, specifications, quantity, priceHT, priceTTC, categoryId, supplierId);
            produitDAO.create(produit);

            MainController.notifyProductAdded();

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Format invalide");
            alert.setContentText("La quantité et les prix doivent être des nombres valides !");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}