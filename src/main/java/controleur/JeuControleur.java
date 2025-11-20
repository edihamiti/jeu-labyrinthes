package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Labyrinthe;
import modele.ModeJeu;
import modele.TypeLabyrinthe;
import modele.Vision;
import modele.generateurs.GenerateurLabyrinthe;
import vue.HandlerVictoire;
import vue.Rendu;
import vue.SoundManager;
import vue.visionsLabyrinthe.VisionFactory;
import vue.visionsLabyrinthe.VisionLabyrinthe;

import java.io.IOException;
import java.util.Random;

/**
 * Contrôleur pour la gestion du jeu de labyrinthe.
 */
public class JeuControleur extends Controleur {
    private static final int SCENE_WIDTH = 1400;
    private static final int SCENE_HEIGHT = 900;
    private static final int WOOD_SOUND_PROBABILITY = 1; // 1% de chance
    private static boolean premierLancement = true;

    @FXML
    public VBox minimap;
    @FXML
    public VBox overlayMinimap;
    @FXML
    private VBox conteneurLabyrinthe;

    private Rendu renduLabyrinthe;
    private Rendu renduMinimap;
    private GenerateurLabyrinthe generateur;
    private HandlerVictoire handlerVictoire;
    private final Random random = new Random();

    /**
     * Initialise le contrôleur et configure les événements de déplacement du joueur.
     */
    @FXML
    public void initialize() {
        this.handlerVictoire = new HandlerVictoire();
        configurerListenersScene();
    }

    private void configurerListenersScene() {
        conteneurLabyrinthe.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                initialiserNouvelleScene(newScene);
            }
        });
    }

    private void initialiserNouvelleScene(Scene scene) {
        if (premierLancement) {
            afficherPopupTouches();
            premierLancement = false;
        }

        configurerGestionClavier(scene);
        configurerRedimensionnement(scene);
    }

    private void configurerGestionClavier(Scene scene) {
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            try {
                switch (event.getCode()) {
                    case UP, Z -> deplacerHaut();
                    case RIGHT, D -> deplacerDroite();
                    case DOWN, S -> deplacerBas();
                    case LEFT, Q -> deplacerGauche();
                }
                event.consume();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private void configurerRedimensionnement(Scene scene) {
        scene.widthProperty().addListener((obs, oldWidth, newWidth) -> rafraichirSiLabyrinthePresent());
        scene.heightProperty().addListener((obs, oldHeight, newHeight) -> rafraichirSiLabyrinthePresent());
    }

    private void rafraichirSiLabyrinthePresent() {
        if (jeu.getLabyrinthe() != null) {
            afficherJeu();
        }
    }


    private void afficherPopupTouches() {
        try {
            Parent popupView = chargerVueAvecDependances("/PopupCommandes.fxml");
            afficherPopup(popupView, "Commandes de jeu");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'affichage du popup: " + e.getMessage());
        }
    }

    private void afficherPopup(Parent contenu, String titre) {
        Stage popupStage = new Stage();
        popupStage.initOwner(conteneurLabyrinthe.getScene().getWindow());
        popupStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        popupStage.setTitle(titre);
        popupStage.setResizable(false);
        popupStage.setScene(new Scene(contenu));
        popupStage.setAlwaysOnTop(true);
        popupStage.show();
    }

    private Parent chargerVueAvecDependances(String cheminFxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(cheminFxml));
        Parent vue = loader.load();
        injecterDependances(loader.getController());
        return vue;
    }

    private void injecterDependances(Object controller) {
        if (controller instanceof Controleur) {
            ((Controleur) controller).setJeu(this.jeu);
            ((Controleur) controller).setAppControleur(this.appControleur);
        }
    }

    /**
     * Retourne au menu principal de l'application.
     *
     * @throws IOException si le chargement de la vue échoue
     */
    public void retourMenu() throws IOException {
        String cheminFxml = jeu.getModeJeu().equals(ModeJeu.MODE_PROGRESSION)
            ? "/ModeProgression.fxml"
            : "/ModeLibreParametres.fxml";

        chargerEtAfficherScene(cheminFxml, "Jeu des Labyrinthes");
        appControleur.resetGame();
    }

    private void retourModeProgression() throws IOException {
        chargerEtAfficherScene("/ModeProgression.fxml", "Jeu des Labyrinthes");
    }

    private void chargerEtAfficherScene(String cheminFxml, String titre) throws IOException {
        Parent vue = chargerVueAvecDependances(cheminFxml);
        Stage stage = (Stage) conteneurLabyrinthe.getScene().getWindow();
        Scene scene = new Scene(vue, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle(titre);
    }

    public void afficherJeu() {
        afficherLabyrinthe();

        // Afficher la minimap si elle est visible
        if (overlayMinimap.isVisible() && renduMinimap != null) {
            afficherMinimap();
        }
    }

    /**
     * Affiche le labyrinthe dans l'interface utilisateur.
     */
    public void afficherLabyrinthe() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(renduLabyrinthe.rendu(jeu.getLabyrinthe()));
    }

    /**
     * Affiche la minimap si nécessaire.
     */
    public void afficherMinimap() {
        minimap.getChildren().clear();
        minimap.getChildren().add(renduMinimap.rendu(jeu.getLabyrinthe()));
    }

    @FXML
    public void deplacerHaut() throws IOException {
        deplacer(jeu.getLabyrinthe().getJoueurX() - 1, jeu.getLabyrinthe().getJoueurY());
    }

    @FXML
    public void deplacerBas() throws IOException {
        deplacer(jeu.getLabyrinthe().getJoueurX() + 1, jeu.getLabyrinthe().getJoueurY());
    }

    @FXML
    public void deplacerGauche() throws IOException {
        deplacer(jeu.getLabyrinthe().getJoueurX(), jeu.getLabyrinthe().getJoueurY() - 1);
    }

    @FXML
    public void deplacerDroite() throws IOException {
        deplacer(jeu.getLabyrinthe().getJoueurX(), jeu.getLabyrinthe().getJoueurY() + 1);
    }

    /**
     * Déplace le joueur vers une nouvelle position si le déplacement est valide.
     *
     * @param nouveauX la nouvelle position X du joueur
     * @param nouveauY la nouvelle position Y du joueur
     * @throws IOException si une erreur survient lors du déplacement
     */
    private void deplacer(int nouveauX, int nouveauY) throws IOException {
        if (!jeu.isRunning()) {
            jeu.startTimer();
        }

        if (jeu.getLabyrinthe().deplacer(nouveauX, nouveauY)) {
            gererDeplacementValide(nouveauX, nouveauY);
        } else {
            gererDeplacementInvalide(nouveauX, nouveauY);
        }
    }

    private void gererDeplacementValide(int x, int y) throws IOException {
        SoundManager.playSound("move.mp3");
        if (random.nextInt(100) < WOOD_SOUND_PROBABILITY) {
            SoundManager.playSound("bois.mp3");
        }

        jeu.setNombreDeplacements(jeu.getNombreDeplacements() + 1);

        if (jeu.getLabyrinthe().estSurSortie(x, y)) {
            victoire();
        }
    }

    private void gererDeplacementInvalide(int x, int y) {
        SoundManager.playSound("block.mp3");
        renduLabyrinthe.setBlockedWall(x, y);
        if (overlayMinimap.isVisible() && renduMinimap != null) {
            renduMinimap.setBlockedWall(x, y);
        }
    }

    /**
     * Gère la victoire du joueur lorsqu'il atteint la sortie du labyrinthe.
     *
     * @throws IOException si une erreur survient lors du retour au menu principal
     */
    private void victoire() throws IOException {
        Stage ownerStage = (Stage) conteneurLabyrinthe.getScene().getWindow();

        handlerVictoire.handleVictoire(
                jeu,
                ownerStage,
                this::handleRejouer,
                this::handleRetourMenu
        );
    }

    private void handleRejouer() {
        try {
            if (jeu.getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
                retourModeProgression();
            } else {
                rejouer();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleRetourMenu() {
        try {
            retourMenu();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void rejouer() throws IOException {
        jeu.resetTimer();
        this.generateur.generer(jeu.getLabyrinthe());
        setRenduLabyrinthe();
        afficherJeu();
    }

    public void setRenduLabyrinthe() {
        VisionLabyrinthe visionStrategy = obtenirStrategieVision();
        configurerRendus(visionStrategy);
    }

    private VisionLabyrinthe obtenirStrategieVision() {
        if (jeu.getDefiEnCours() != null) {
            Vision vision = jeu.getDefiEnCours().vision();
            int porteeVision = jeu.getDefiEnCours().portee();
            return VisionFactory.getStrategy(vision, porteeVision);
        } else {
            return VisionFactory.getStrategy(Vision.VUE_LIBRE, 0);
        }
    }

    private void configurerRendus(VisionLabyrinthe visionStrategy) {
        this.renduLabyrinthe = visionStrategy.createRendu(jeu.getLabyrinthe(), conteneurLabyrinthe);

        if (visionStrategy.requiresMinimap()) {
            overlayMinimap.setVisible(true);
            this.renduMinimap = visionStrategy.createMinimapRendu(jeu.getLabyrinthe(), minimap);
        } else {
            overlayMinimap.setVisible(false);
            this.renduMinimap = null;
        }
    }

    /**
     * Définit les paramètres du labyrinthe et initialise le renduLabyrinthe.
     *
     * @param largeur         la largeur du labyrinthe
     * @param hauteur         la hauteur du labyrinthe
     * @param pourcentageMurs le pourcentage de murs dans le labyrinthe
     * @param distanceMin     la distance minimale entre le joueur et la sortie du labyrinthe
     * @param typeLab         le générateur de labyrinthe à utiliser ("aleatoire" ou "parfait")
     */
    public void setParametresLab(int largeur, int hauteur, double pourcentageMurs, int distanceMin, TypeLabyrinthe typeLab) {
        // Créer le labyrinthe
        jeu.setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs, distanceMin));

        // Créer et utiliser le générateur
        this.generateur = typeLab.creerGenerateur(largeur, hauteur, pourcentageMurs, distanceMin);
        this.generateur.generer(jeu.getLabyrinthe());

        // Réinitialiser le timer
        jeu.resetTimer();

        // Configurer le rendu
        setRenduLabyrinthe();

        // Ajouter les listeners pour la position du joueur
        jeu.getLabyrinthe().joueurXProperty().addListener((obs, oldVal, newVal) -> afficherJeu());
        jeu.getLabyrinthe().joueurYProperty().addListener((obs, oldVal, newVal) -> afficherJeu());

        // Afficher le jeu
        afficherJeu();
    }
}
