package vue;

import controleur.AppControleur;
import controleur.Controleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Jeu;

import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

/**
 * Classe permettant de router plus efficacement à travers les vues.
 * Utilise une pile pour gérer l'historique de navigation et permettre le retour en arrière.
 */
public class Router {
    private static Stack<RouteInfo> navigationStack = new Stack<>();
    private static Stage primaryStage;
    private static AppControleur appControleur;
    private static Jeu jeu;

    /**
     * Informations sur une route dans l'historique de navigation.
     */
    private static class RouteInfo {
        String fxmlPath;
        Object data;
        boolean maximized;

        RouteInfo(String fxmlPath, Object data, boolean maximized) {
            this.fxmlPath = fxmlPath;
            this.data = data;
            this.maximized = maximized;
        }

        public String toString() {
            return fxmlPath;
        }
    }

    /**
     * Initialise le router avec le stage principal et les dépendances globales.
     *
     * @param stage le stage principal de l'application
     * @param appControleur le contrôleur principal de l'application
     * @param jeu l'instance de Jeu
     */
    public static void initialize(Stage stage, AppControleur appControleur, Jeu jeu) {
        Router.primaryStage = stage;
        Router.appControleur = appControleur;
        Router.jeu = jeu;
    }

    /**
     * Navigue vers une nouvelle vue FXML.
     *
     * @param fxmlPath le chemin vers le fichier FXML (ex: "/HomePage.fxml")
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void route(String fxmlPath) throws IOException {
        route(fxmlPath, null);
    }

    /**
     * Navigue vers une nouvelle vue FXML avec des données optionnelles.
     *
     * @param fxmlPath le chemin vers le fichier FXML (ex: "/HomePage.fxml")
     * @param data des données à passer au contrôleur (optionnel)
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void route(String fxmlPath, Object data) throws IOException {
        route(fxmlPath, data, true);
    }

    /**
     * Navigue vers une nouvelle vue FXML avec des données et options de fenêtre.
     *
     * @param fxmlPath le chemin vers le fichier FXML (ex: "/HomePage.fxml")
     * @param data des données à passer au contrôleur (optionnel)
     * @param maximized si true, la fenêtre sera maximisée
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void route(String fxmlPath, Object data, boolean maximized) throws IOException {
        // Sauvegarder la route actuelle dans la pile
        if (primaryStage.getScene() != null) {
            if (navigationStack.isEmpty() || !Objects.equals(navigationStack.peek().fxmlPath, fxmlPath)) {
                navigationStack.push(new RouteInfo(fxmlPath, data, primaryStage.isMaximized()));
            }
        }

        System.out.println("[DEBUG] Route: " + fxmlPath);

        loadAndDisplayScene(fxmlPath, data, maximized);
    }

    /**
     * Remplace la vue actuelle sans l'ajouter à l'historique.
     * Utile pour les redirections où on ne veut pas pouvoir revenir en arrière.
     *
     * @param fxmlPath le chemin vers le fichier FXML
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void replace(String fxmlPath) throws IOException {
        replace(fxmlPath, null);
    }

    /**
     * Remplace la vue actuelle sans l'ajouter à l'historique.
     *
     * @param fxmlPath le chemin vers le fichier FXML
     * @param data des données à passer au contrôleur (optionnel)
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void replace(String fxmlPath, Object data) throws IOException {
        replace(fxmlPath, data, true);
    }

    /**
     * Remplace la vue actuelle sans l'ajouter à l'historique.
     *
     * @param fxmlPath le chemin vers le fichier FXML
     * @param data des données à passer au contrôleur (optionnel)
     * @param maximized si true, la fenêtre sera maximisée
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void replace(String fxmlPath, Object data, boolean maximized) throws IOException {
        loadAndDisplayScene(fxmlPath, data, maximized);
    }

    /**
     * Revient à la vue précédente dans l'historique de navigation.
     *
     * @return true si le retour a été effectué, false si l'historique est vide
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static boolean back() throws IOException {
        System.out.println("[DEBUG] Back");
        System.out.println("[DEBUG] History size: " + navigationStack.size());
        System.out.println("[DEBUG] Stack content: " + navigationStack.toString());

        if (navigationStack.isEmpty()) {
            System.out.println("[DEBUG] No previous route");
            return false;
        }

        System.out.println("[DEBUG] Previous route: " + navigationStack.peek().fxmlPath);

        // Enlever la route actuelle de l'historique
        navigationStack.pop();

        // Prendre la route précédente de l'historique et la charger
        RouteInfo previousRoute = navigationStack.isEmpty() ? new RouteInfo("/HomePage.fxml", null, primaryStage.isMaximized()) : navigationStack.peek();
        loadAndDisplayScene(previousRoute.fxmlPath, previousRoute.data, previousRoute.maximized);
        return true;
    }

    /**
     * Revient à la page d'accueil et vide l'historique de navigation.
     *
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    public static void home() throws IOException {
        System.out.println("[DEBUG] Home");

        clearHistory();
        if (appControleur != null) {
            appControleur.resetGame();
        }
        replace("/HomePage.fxml");
    }

    /**
     * Vide l'historique de navigation.
     */
    public static void clearHistory() {
        navigationStack.clear();
    }

    /**
     * Retourne le nombre d'entrées dans l'historique de navigation.
     *
     * @return la taille de la pile de navigation
     */
    public static int getHistorySize() {
        return navigationStack.size();
    }

    /**
     * Vérifie si on peut revenir en arrière.
     *
     * @return true s'il y a au moins une entrée dans l'historique
     */
    public static boolean canGoBack() {
        return !navigationStack.isEmpty();
    }

    /**
     * Charge et affiche une scène FXML.
     *
     * @param fxmlPath le chemin vers le fichier FXML
     * @param data des données à passer au contrôleur (optionnel)
     * @param maximized si true, la fenêtre sera maximisée
     * @throws IOException si le fichier FXML n'est pas trouvé
     */
    private static void loadAndDisplayScene(String fxmlPath, Object data, boolean maximized) throws IOException {
        System.out.println("[DEBUG] Load and display scene: " + fxmlPath);

        FXMLLoader loader = new FXMLLoader(Router.class.getResource(fxmlPath));
        Parent view = loader.load();

        // Injecter les dépendances dans le contrôleur
        Object controller = loader.getController();
        if (controller instanceof Controleur ctrl) {
            ctrl.setJeu(jeu);
            ctrl.setAppControleur(appControleur);
        }

        // Passer les données au contrôleur s'il est DataReceiver
        if (data != null && controller instanceof DataReceiver) {
            ((DataReceiver) controller).receiveData(data);
        }

        Scene scene = new Scene(view, 1400, 900);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(maximized);
        primaryStage.setTitle("Jeu des Labyrinthes");
    }

    /**
     * Ajoute une entrée dans l'historique de navigation.
     * Utile lorsqu'il faut avoir un traitement différent du chargement de la vue.
     *
     * @param FXMLPath Le chemin vers le fichier FXML
     */
    public static void addToHistory(String FXMLPath) {
        navigationStack.push(new RouteInfo(FXMLPath, null, primaryStage.isMaximized()));
    }

    /**
     * Ajoute une entrée dans l'historique de navigation.
     * Utile lorsqu'il faut avoir un traitement différent du chargement de la vue.
     *
     * @param FXMLPath Le chemin vers le fichier FXML
     * @param data des données à passer au contrôleur (optionnel)
     */
    public static void addToHistory(String FXMLPath, Object data) {
        navigationStack.push(new RouteInfo(FXMLPath, data, primaryStage.isMaximized()));
    }

    /**
     * Ajoute une entrée dans l'historique de navigation.
     * Utile lorsqu'il faut avoir un traitement différent du chargement de la vue.
     *
     * @param FXMLPath Le chemin vers le fichier FXML
     * @param data des données à passer au contrôleur (optionnel)
     * @param maximized si true, la fenêtre sera maximisée (optionnel)
     */
    public static void addToHistory(String FXMLPath, Object data, boolean maximized) {
        navigationStack.push(new RouteInfo(FXMLPath, data, maximized));
    }

    /**
     * Interface optionnelle pour les contrôleurs qui souhaitent recevoir des données
     * lors de la navigation.
     */
    public interface DataReceiver {
        void receiveData(Object data);
    }
}
