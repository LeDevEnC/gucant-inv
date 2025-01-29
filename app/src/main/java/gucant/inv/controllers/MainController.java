package gucant.inv.controllers;

import gucant.inv.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;

public class MainController {

    @FXML
    private FlowPane categoryGrid; // Au lieu de HBox

    @FXML
    private void handleNewProduct(ActionEvent event) {
        // Charger la vue d'ajout de produit
        NavigationManager.getInstance().openNewWindow("/gucant/inv/views/AddProductView.fxml", "Add New Product");
    }

    @FXML
    private void handleCategorySelection(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        
        // Réinitialise tous les boutons
        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof Button button) {
                // S'assure que tous les boutons ont au moins la classe category-button
                if (!button.getStyleClass().contains("category-button")) {
                    button.getStyleClass().add("category-button");
                }
                button.getStyleClass().remove("selected-category");
            }
        }
        
        // Applique le style selected-category au bouton cliqué
        clickedButton.getStyleClass().add("selected-category");
    }
}
