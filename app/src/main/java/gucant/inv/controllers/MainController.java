package gucant.inv.controllers;

import gucant.inv.models.dao.ProduitDAO;
import gucant.inv.models.data.Produit;
import gucant.inv.utils.NavigationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

public class MainController {

    @FXML
    private FlowPane categoryGrid;

    @FXML
    private TableView<Produit> productsTable;

    @FXML
    private TableColumn<Produit, String> colReference;
    @FXML
    private TableColumn<Produit, String> colName;
    @FXML
    private TableColumn<Produit, String> colSupplier;
    @FXML
    private TableColumn<Produit, Double> colPrice;
    @FXML
    private TableColumn<Produit, Integer> colStock;
    @FXML
    private TableColumn<Produit, Void> colActions;
    @FXML
    private TableColumn<Produit, String> colCategory;


    private ProduitDAO produitDAO;
    private ObservableList<Produit> produitList;

    public MainController() {
        produitDAO = new ProduitDAO();
    }

    
    @FXML
    public void initialize() {
        colReference.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));  
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));  
        colPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    
        loadProducts();
    }
    

    private void loadProducts() {
        produitList = FXCollections.observableArrayList(produitDAO.getAll());
        produitList.forEach(produit -> {
            produit.setPrix(produit.getPrix() * produit.getQuantite());
        });
        productsTable.setItems(produitList);

    }

    @FXML
private void handleNewProduct(ActionEvent event) {
    NavigationManager.getInstance().openNewWindow("/gucant/inv/views/AddProductView.fxml", "Add New Product");
}




    @FXML
    private void handleCategorySelection(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        
        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof Button button) {
                if (!button.getStyleClass().contains("category-button")) {
                    button.getStyleClass().add("category-button");
                }
                button.getStyleClass().remove("selected-category");
            }
        }
        
        clickedButton.getStyleClass().add("selected-category");
    }

    public void refreshTable() {
        loadProducts();
    }
}
