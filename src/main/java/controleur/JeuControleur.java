package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import modele.Jeu;
import modele.Joueur;
import modele.Labyrinthe;
import modele.PseudoException;
import vue.LabyrintheRenderer;

import java.io.IOException;

/**
 * Controller layer: Handles user input and coordinates between Model and View
 * Follows MVC pattern:
 * - Receives user actions from View (FXML buttons)
 * - Delegates business logic to Model (Jeu, Labyrinthe)
 * - Uses View layer (LabyrintheRenderer) for rendering
 * - Observes Model changes via JavaFX properties
 */
public class JeuControleur {
    
    @FXML
    private VBox contienLabyrinthe;
    
    private Jeu jeu;
    private Labyrinthe labyrinthe;
    private Joueur joueur;
    private LabyrintheRenderer renderer;

    @FXML
    public void initialize() {
        // Initialize the View layer
        renderer = new LabyrintheRenderer();
        
        // Create default player and game for testing
        try {
            joueur = new Joueur("Joueur1");
        } catch (PseudoException e) {
            joueur = null;
        }
        this.labyrinthe = new Labyrinthe(10, 10, 10);
        this.jeu = new Jeu(labyrinthe, joueur);
        jeu.initialiser();
        
        // Setup observer pattern - listen to model changes and update view
        labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        labyrinthe.joueurYProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        
        afficherLabyrinthe();
    }
    
    /**
     * Allows initialization of controller with specific parameters
     * Called by other controllers to set up a game with custom settings
     * @param largeur labyrinth width
     * @param hauteur labyrinth height
     * @param pourcentageMurs percentage of walls
     * @param joueur player for this game
     */
    public void setParametres(int largeur, int hauteur, double pourcentageMurs, Joueur joueur) {
        // Initialize the View layer
        renderer = new LabyrintheRenderer();
        
        this.joueur = joueur;
        this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
        this.jeu = new Jeu(labyrinthe, joueur);
        jeu.initialiser();
        
        // Setup observer pattern - Model notifies Controller, Controller updates View
        labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        labyrinthe.joueurYProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        
        afficherLabyrinthe();
    }

    public void retourMenu() throws IOException {
        AppControleur.getInstance().MenuPrincipal();
    }

    /**
     * Updates the view by delegating rendering to the View layer
     * Called when the model changes (through property listeners)
     */
    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        // Delegate rendering to View layer
        contienLabyrinthe.getChildren().add(renderer.render(labyrinthe));
    }

    @FXML
    public void deplacerHaut() throws IOException {
        deplacer(labyrinthe.getJoueurX() - 1, labyrinthe.getJoueurY());
    }

    @FXML
    public void deplacerBas() throws IOException {
        deplacer(labyrinthe.getJoueurX() + 1, labyrinthe.getJoueurY());
    }

    @FXML
    public void deplacerGauche() throws IOException {
        deplacer(labyrinthe.getJoueurX(), labyrinthe.getJoueurY() - 1);
    }

    @FXML
    public void deplacerDroite() throws IOException {
        deplacer(labyrinthe.getJoueurX(), labyrinthe.getJoueurY() + 1);
    }

    /**
     * Controller method to handle player movement
     * Delegates to the model and checks for victory condition
     */
    private void deplacer(int nouveauX, int nouveauY) throws IOException {
        // Controller delegates to Model
        if (jeu.deplacerJoueur(nouveauX, nouveauY)) {
            // View update happens automatically via property listeners
            
            // Check victory condition
            if (jeu.estVictoire()) {
                victoire();
            }
        }
    }

    /**
     * Handles victory - updates model and shows UI feedback
     */
    private void victoire() throws IOException {
        // Update model state
        jeu.terminerPartie();
        
        // Display view feedback
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire");
        alert.setHeaderText("Félicitations");
        alert.setContentText("Vous avez trouvé la sortie");
        alert.showAndWait();
        
        // Navigate back to menu
        AppControleur.getInstance().MenuPrincipal();
    }

}
