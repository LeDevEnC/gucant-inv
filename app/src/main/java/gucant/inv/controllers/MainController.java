package gucant.inv.controllers;

import java.util.Optional;
import java.text.Normalizer;

import gucant.inv.models.dao.ProduitDAO;
import gucant.inv.models.data.Produit;
import gucant.inv.utils.NavigationManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class MainController {
    public static MainController instance;

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

    @FXML
    private Button btnTout;

    private ProduitDAO produitDAO;
    private ObservableList<Produit> produitList;
    private FilteredList<Produit> filteredData;
    private String currentCategory = "Tout";

    public MainController() {
        produitDAO = new ProduitDAO();
    }

    private String normalizeString(String str) {
        return Normalizer
            .normalize(str, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase();
    }

    @FXML
    private void initialize() {
        instance = this;
        
        // Configuration des colonnes
        colReference.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colPrice.setCellValueFactory(cellData -> {
            Produit produit = cellData.getValue();
            double totalPrice = produit.getPrix() * produit.getQuantite();
            return new SimpleObjectProperty<>(totalPrice);
        });
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Configuration de la colonne d'actions
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAdd = new Button("+");
            private final Button btnRemove = new Button("-");
            private final Button btnDelete = new Button("🗑");
            private final HBox buttonsBox = new HBox(5, btnAdd, btnRemove, btnDelete);

            {
                btnAdd.setOnAction(event -> modifyStock(getTableView().getItems().get(getIndex()), 1));
                btnRemove.setOnAction(event -> modifyStock(getTableView().getItems().get(getIndex()), -1));
                btnDelete.setOnAction(event -> deleteProduct(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }
        });

        // Initialisation des données
        loadProducts();
        
        // Set "Tout" button as selected by default
        btnTout.getStyleClass().add("selected-category");
    }

    private void loadProducts() {
        produitList = FXCollections.observableArrayList(produitDAO.getAll());
        
        // Create filtered list
        filteredData = new FilteredList<>(produitList, p -> true);
        
        // Bind filtered list to table
        SortedList<Produit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productsTable.comparatorProperty());
        productsTable.setItems(sortedData);
    }

    private void modifyStock(Produit produit, int amount) {
        if (produit.getQuantite() + amount >= 0) {
            produit.setQuantite(produit.getQuantite() + amount);
            if (produit.getQuantite() == 0) {
                boolean deleteConfirmed = showDeleteConfirmation(produit);
                if (!deleteConfirmed) {
                    produit.setQuantite(1);
                } else {
                    produitDAO.delete(produit.getName());
                }
            } else {
                produitDAO.update(produit);
            }
            refreshTable();
        }
    }

    private boolean showDeleteConfirmation(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Le produit " + produit.getName() + " a une quantité de 0.");
        alert.setContentText("Voulez-vous supprimer ce produit ?");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    private void deleteProduct(Produit produit) {
        produitDAO.delete(produit.getName());
        refreshTable();
    }

    public void refreshTable() {
        produitList.clear();
        produitList.addAll(produitDAO.getAll());
        
        // Reapply current category filter with accent handling
        filteredData.setPredicate(product -> {
            if (currentCategory.equals("Tout")) {
                return true;
            }
            return normalizeString(product.getCategoryName())
                   .equals(normalizeString(currentCategory));
        });
        
        productsTable.refresh();
    }

    public static void notifyProductAdded() {
        if (instance != null) {
            instance.refreshTable();
        }
    }

    @FXML
    private void handleNewProduct(ActionEvent event) {
        NavigationManager.getInstance().openNewWindow("/gucant/inv/views/AddProductView.fxml", "Add New Product");
    }

    @FXML
    private void handleCategorySelection(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        currentCategory = clickedButton.getText();

        // Update button styles
        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("selected-category");
            }
        }
        clickedButton.getStyleClass().add("selected-category");

        // Update filter with accent-insensitive comparison
        filteredData.setPredicate(product -> {
            if (currentCategory.equals("Tout")) {
                return true;
            }
            return normalizeString(product.getCategoryName())
                   .equals(normalizeString(currentCategory));
        });
    }
}