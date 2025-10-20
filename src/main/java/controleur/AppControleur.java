package controleur;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppControleur {

    private static AppControleur instance;
    private Stage primaryStage;
    private final Scene menu;


    private AppControleur() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AppControleur.class.getResource("/HomePage.fxml"));
        this.menu = new Scene(fxmlLoader.load(), 1400, 900);
    }

    public static AppControleur getInstance() throws IOException {
        if (instance == null) {
            instance = new AppControleur();
        }
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void MenuPrincipal() {
        primaryStage.setTitle("HomePage");
        primaryStage.setScene(this.menu);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

}
