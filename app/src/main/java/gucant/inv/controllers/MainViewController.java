package gucant.inv.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

public class MainViewController {
    @FXML
    private HBox categoriesContainer;

    @FXML
    private TableView<?> productsTable;

    @FXML
    public void filterByCategory() {
        // Logique de filtrage des catégories
    }

    @FXML
    public void openAddProductForm() {
        // Logique pour ouvrir le formulaire d'ajout de produit
    }
}