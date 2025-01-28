package gucant.inv;

import gucant.inv.utils.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class of the application.
 */
public class App extends Application {
    /**
     * Start the application.
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            NavigationManager.getInstance().setPrimaryStage(primaryStage);
            NavigationManager.getInstance().navigateTo("/gucant/inv/views/Main.fxml", "Inventory Management");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method of the application.
     * @param args The arguments of the application.
     */
    public static void main(String[] args) {
        TestDAO.main(args);
        launch(args);

    }
}