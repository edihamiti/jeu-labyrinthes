import controleur.MenuPrincipalControleur;
import javafx.application.Application;
import javafx.stage.Stage;
import modele.Jeu;
import vue.MenuPrincipal;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        Jeu jeu = new Jeu();
        MenuPrincipalControleur menuControleur = new MenuPrincipalControleur(jeu);
        MenuPrincipal menu = new MenuPrincipal(jeu);
    }
}
