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
    private TextField priceField;
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private ComboBox<String> supplierBox;
   
    private ProduitDAO produitDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;

    public AddProductController() {
        produitDAO = new ProduitDAO();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();
    }

    @FXML
    public void initialize() {
        categoryBox.getItems().addAll(categoryDAO.getAllCategories());
        supplierBox.getItems().addAll(supplierDAO.getAllSuppliers());
    }

    private boolean isProductSimilarToExisting(String newProductName) {
        // Récupérer tous les noms de produits existants
        List<String> existingProductNames = produitDAO.getAll()
            .stream()
            .map(Produit::getName)
            .collect(Collectors.toList());

        // Vérifier si un produit similaire existe
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
            
            // Vérifier les champs vides
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

            // Vérifier si un produit similaire existe
            if (isProductSimilarToExisting(name)) {
                showSimilarProductAlert(name);
                return;
            }

            // Vérifier et convertir les valeurs numériques
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            
            if (quantity < 0 || price < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Valeurs invalides");
                alert.setContentText("La quantité et le prix doivent être positifs !");
                alert.showAndWait();
                return;
            }

            int categoryId = categoryBox.getSelectionModel().getSelectedIndex() + 1;
            int supplierId = supplierBox.getSelectionModel().getSelectedIndex() + 1;

            Produit produit = new Produit(name, specifications, quantity, price, categoryId, supplierId);
            produitDAO.create(produit);
            
            // Notifier MainController du changement
            MainController.notifyProductAdded();
           
            // Fermer la fenêtre
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("Format invalide");
            alert.setContentText("La quantité et le prix doivent être des nombres valides !");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}