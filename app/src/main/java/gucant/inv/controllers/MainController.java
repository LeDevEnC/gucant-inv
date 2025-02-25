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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

/**
 * MainController is responsible for managing the main view of the application.
 */
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
    private TableColumn<Produit, Double> colPriceHT;
    @FXML
    private TableColumn<Produit, Double> colPriceTTC;
    @FXML
    private TableColumn<Produit, Integer> colStock;
    @FXML
    private TableColumn<Produit, Void> colActions;
    @FXML
    private TableColumn<Produit, String> colCategory;
    @FXML
    private TableColumn<Produit, String> colSpecifications;

    @FXML
    private Button btnTout;

    @FXML
    private TextField globalSearchField;

    private ProduitDAO produitDAO;
    private ObservableList<Produit> produitList;
    private FilteredList<Produit> filteredData;
    private String currentCategory = "Tout";
    private static final double TVA_RATE = 0.2;

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

        colReference.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSpecifications.setCellValueFactory(new PropertyValueFactory<>("specifications"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colPriceHT.setCellValueFactory(new PropertyValueFactory<>("prixHT"));
        colPriceTTC.setCellValueFactory(new PropertyValueFactory<>("prixTTC"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Empêcher le redimensionnement des colonnes
        colReference.setResizable(false);
        colName.setResizable(false);
        colSpecifications.setResizable(false);
        colSupplier.setResizable(false);
        colCategory.setResizable(false);
        colPriceHT.setResizable(false);
        colPriceTTC.setResizable(false);
        colStock.setResizable(false);
        colActions.setResizable(false);

        // Adapter la largeur des colonnes dynamiquement
        productsTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();

            colReference.setPrefWidth(totalWidth * 0.05);
            colName.setPrefWidth(totalWidth * 0.15);
            colSpecifications.setPrefWidth(totalWidth * 0.20);
            colSupplier.setPrefWidth(totalWidth * 0.10);
            colCategory.setPrefWidth(totalWidth * 0.10);
            colPriceHT.setPrefWidth(totalWidth * 0.10);
            colPriceTTC.setPrefWidth(totalWidth * 0.10);
            colStock.setPrefWidth(totalWidth * 0.05);
            colActions.setPrefWidth(totalWidth * 0.15);
        });
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Configuration des actions
        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAdd = new Button("+");
            private final Button btnRemove = new Button("-");
            private final Button btnDelete = new Button("🗑");
            private final HBox buttonsBox = new HBox(10, btnAdd, btnRemove, btnDelete);

            {
                buttonsBox.setAlignment(Pos.CENTER);
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

        setupSearch();
        loadProducts();
        btnTout.getStyleClass().add("selected-category");
    }

    private void setupSearch() {
        globalSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFilterPredicate();
        });
    }

    private void updateFilterPredicate() {
        filteredData.setPredicate(product -> {
            String searchText = globalSearchField.getText();
            boolean matchesCategory = currentCategory.equals("Tout") ||
                    normalizeString(product.getCategoryName()).equals(normalizeString(currentCategory));

            if (searchText == null || searchText.isEmpty()) {
                return matchesCategory;
            }

            String lowerCaseFilter = normalizeString(searchText);

            return matchesCategory && (normalizeString(product.getName()).contains(lowerCaseFilter) ||
                    normalizeString(String.valueOf(product.getId())).contains(lowerCaseFilter) ||
                    normalizeString(product.getSupplierName()).contains(lowerCaseFilter) ||
                    normalizeString(product.getCategoryName()).contains(lowerCaseFilter) ||
                    String.valueOf(product.getPrixHT()).contains(lowerCaseFilter) ||
                    String.valueOf(product.getPrixTTC()).contains(lowerCaseFilter) ||
                    String.valueOf(product.getQuantite()).contains(lowerCaseFilter));
        });
    }

    private void loadProducts() {
        produitList = FXCollections.observableArrayList(produitDAO.getAll());
        filteredData = new FilteredList<>(produitList, p -> true);
        SortedList<Produit> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productsTable.comparatorProperty());
        productsTable.setItems(sortedData);
    }

    private void modifyStock(Produit produit, int amount) {
        // Sauvegarder les prix unitaires avant modification
        double prixUnitaireHT = produit.getPrixHT() / (produit.getQuantite() > 0 ? produit.getQuantite() : 1);
        double prixUnitaireTTC = produit.getPrixTTC() / (produit.getQuantite() > 0 ? produit.getQuantite() : 1);

        // Vérifier si le nouveau stock serait positif ou nul
        if (produit.getQuantite() + amount >= 0) {
            // Mettre à jour la quantité
            produit.setQuantite(produit.getQuantite() + amount);

            // Mettre à jour les prix totaux
            updateProductPrices(produit, prixUnitaireHT, prixUnitaireTTC);

            // Si le stock atteint zéro, proposer la suppression
            if (produit.getQuantite() == 0) {
                boolean deleteConfirmed = showDeleteConfirmation(produit);
                if (!deleteConfirmed) {
                    // L'utilisateur ne veut pas supprimer, remettre à 1
                    produit.setQuantite(1);
                    updateProductPrices(produit, prixUnitaireHT, prixUnitaireTTC);
                } else {
                    // Supprimer le produit
                    produitDAO.delete(produit.getName());
                }
            } else {
                // Mettre à jour le produit dans la base de données
                produitDAO.update(produit);
            }

            // Actualiser l'affichage
            refreshTable();
        }
    }

    /**
     * Met à jour les prix totaux en fonction de la quantité et des prix unitaires.
     */
    private void updateProductPrices(Produit produit, double prixUnitaireHT, double prixUnitaireTTC) {
        if (produit.getQuantite() > 0) {
            // Calculer les prix totaux en fonction de la quantité
            double newPriceHT = prixUnitaireHT * produit.getQuantite();
            double newPriceTTC = prixUnitaireTTC * produit.getQuantite();
            produit.setPrixHT(newPriceHT);
            produit.setPrixTTC(newPriceTTC);
        } else {
            // Si la quantité est zéro, mettre les prix à zéro
            produit.setPrixHT(0.0);
            produit.setPrixTTC(0.0);
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
        updateFilterPredicate();
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

        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof Button button) {
                button.getStyleClass().remove("selected-category");
            }
        }
        clickedButton.getStyleClass().add("selected-category");
        updateFilterPredicate();
    }
}
