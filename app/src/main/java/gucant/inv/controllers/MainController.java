package gucant.inv.controllers;

import gucant.inv.utils.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    private void handleNewProduct(ActionEvent event) {
        // Charger la vue d'ajout de produit
        NavigationManager.getInstance().openNewWindow("/gucant/inv/views/AddProductView.fxml", "Add New Product");
    }
}
