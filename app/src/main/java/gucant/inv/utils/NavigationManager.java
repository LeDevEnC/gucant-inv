package gucant.inv.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.io.IOException;

/**
 * Class to manage the navigation between pages of the application.
 */
public class NavigationManager {
    /**
     * Singleton instance of the NavigationManager.
     */
    private static NavigationManager instance;

    /**
     * The primary stage of the application.
     */
    private final Stage primaryStage;

    /**
     * The pages of the application.
     */
    private final HashMap<String, Parent> pages = new HashMap<>();

    /**
     * Constructor of the NavigationManager.
     * @param stage The primary stage of the application.
     */
    private NavigationManager(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Get the instance of the NavigationManager.
     * @param stage The primary stage of the application.
     * @return The instance of the NavigationManager.
     */
    public static NavigationManager getInstance(Stage stage) {
        if (instance == null) {
            instance = new NavigationManager(stage);
        }
        return instance;
    }

    /**
     * Add a page to the application.
     * @param name The name of the page.
     * @param fxmlFile The FXML file of the page.
     */
    public void addPage(String name, String fxmlFile) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource("/gucant/inv/views/" + fxmlFile));
            pages.put(name, page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a page of the application.
     * @param name The name of the page.
     */
    public void showPage(String name) {
        Parent page = pages.get(name);
        if (page != null) {
            primaryStage.setScene(new Scene(page));
            primaryStage.show();
        } else {
            System.err.println("Page introuvable : " + name);
        }
    }
}