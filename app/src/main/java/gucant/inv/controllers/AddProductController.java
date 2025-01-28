package gucant.inv.controllers;

import gucant.inv.utils.NavigationManager;

import java.io.IOException;

import gucant.inv.models.dao.ProduitDAO;
import gucant.inv.models.data.Produit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public AddProductController() {
        produitDAO = new ProduitDAO();
    }

    @FXML
    private void handleAddProduct(ActionEvent event) throws IOException {
        try {
            String name = nameField.getText();
            String specifications = specificationsField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            int categoryId = categoryBox.getSelectionModel().getSelectedIndex() + 1;
            int supplierId = supplierBox.getSelectionModel().getSelectedIndex() + 1;

            if (name.isEmpty() || specifications.isEmpty() || categoryId == 0 || supplierId == 0) {
                System.out.println("All fields must be filled!");
                return;
            }

            Produit produit = new Produit(name, specifications, quantity, price, categoryId, supplierId);
            produitDAO.create(produit);

            System.out.println("Product added: " + produit.getName());

            // Retour à la vue principale ou fermeture de la fenêtre
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

            // Facultatif : recharger la vue principale si nécessaire
            NavigationManager.getInstance().navigateTo("/gucant/inv/views/MainView.fxml", "Main View");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Fermer la fenêtre sans sauvegarder
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
