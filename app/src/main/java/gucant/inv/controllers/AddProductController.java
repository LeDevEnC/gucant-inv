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
import gucant.inv.models.dao.CategoryDAO;
import gucant.inv.models.dao.SupplierDAO;

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

            // Fermer la fenêtre
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
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}

