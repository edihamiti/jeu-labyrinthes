package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modele.Cellules.Cellule;
import modele.Cellules.Sortie;
import modele.Labyrinthe;
import modele.LabyrintheObserver;
import modele.TypeLabyrinthe;
import modele.Vision;
import modele.*;
import modele.generateurs.GenerateurLabyrinthe;
import vue.*;
import vue.visionsLabyrinthe.VisionFactory;
import vue.visionsLabyrinthe.VisionLabyrinthe;

import java.io.IOException;
import java.util.Random;

/**
 * Contrôleur pour la gestion du jeu de labyrinthe.
 */
public class JeuControleur extends Controleur implements Router.DataReceiver, LabyrintheObserver {
    private static final int WOOD_SOUND_PROBABILITY = 1; // 1% de chance
    private static boolean premierLancement = true;
    private final Random random = new Random();
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
    private HandlerDefaite handlerDefaite;
    private boolean isModeCle = false;
    private int porteeVisionCle = 0;

    /**
     * Initialise le contrôleur et configure les événements de déplacement du joueur.
     */
    @FXML
    public void initialize() {
        this.handlerVictoire = new HandlerVictoire();
        this.handlerDefaite = new HandlerDefaite();
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
        appControleur.resetGame();
        Router.home();
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
        boolean surCle = jeu.getLabyrinthe().getCellules()[x][y].estCle() && !jeu.getLabyrinthe().isCleObtenue();

        SoundManager.playSound("move.mp3");

        if (random.nextInt(100) < WOOD_SOUND_PROBABILITY) {
            SoundManager.playSound("bois.mp3");
        }

        jeu.setNombreDeplacements(jeu.getNombreDeplacements() + 1);

        if (surCle) {
            jeu.getLabyrinthe().setCleObtenue();
            SoundManager.playSound("cle.mp3");

            jeu.getLabyrinthe().getCellules()[x][y] = new modele.Cellules.Chemin(x, y);

            int sortieX = jeu.getLabyrinthe().getSortieX();
            int sortieY = jeu.getLabyrinthe().getSortieY();
            if (sortieX >= 0 && sortieY >= 0) {
                Cellule celluleSortie = jeu.getLabyrinthe().getCellules()[sortieX][sortieY];
                if (celluleSortie.estSortie()) {
                    Sortie sortie = (Sortie) celluleSortie;
                    sortie.deverrouillee();
                }
            }
        }

        if (ModeJeu.MODE_PROGRESSION == jeu.getModeJeu()) {
            if (jeu.getNombreDeplacements() >= nbDeplacementMax()) {
                defaite();
            }
        }

        if (jeu.getLabyrinthe().estSurSortie(x, y)) {
            victoire();
        }
    }

    private int nbDeplacementMax() {
        return jeu.getLabyrinthe().calculePlusCourtChemin()*2;
    }

    private void gererDeplacementInvalide(int x, int y) {
        SoundManager.playSound("block.mp3");
        renduLabyrinthe.setBlockedWall(x, y);
        if (overlayMinimap.isVisible() && renduMinimap != null) {
            renduMinimap.setBlockedWall(x, y);
        }
    }

    private void defaite() throws IOException {
        Stage ownerStage = (Stage) conteneurLabyrinthe.getScene().getWindow();

        handlerDefaite.handleDefaite(
                jeu,
                ownerStage,
                this::handleRejouer,
                this::handleRetourMenu
        );
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

    public void handleRejouer() {
        try {
            Router.back();
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

    public void setRenduLabyrinthe() {
        VisionLabyrinthe visionStrategy = obtenirStrategieVision();
        configurerRendus(visionStrategy);
    }

    private VisionLabyrinthe obtenirStrategieVision() {
        if (jeu.getDefiEnCours() != null) {
            Vision vision = jeu.getDefiEnCours().vision();
            int porteeVision = jeu.getDefiEnCours().portee();
            System.out.println("[obtenirStrategieVision] Finis");
            return VisionFactory.getStrategy(vision, porteeVision);
        } else {
            return VisionFactory.getStrategy(Vision.VUE_LIBRE, 0);
        }
    }

    private void configurerRendus(VisionLabyrinthe visionStrategy) {
        this.renduLabyrinthe = visionStrategy.createRendu(jeu.getLabyrinthe(), conteneurLabyrinthe, jeu);

        if (visionStrategy.requiresMinimap()) {
            overlayMinimap.setVisible(true);
            this.renduMinimap = visionStrategy.createMinimapRendu(jeu.getLabyrinthe(), minimap, jeu);
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
        jeu.getLabyrinthe().addObserver(this);

        // Afficher le jeu
        afficherJeu();
    }

    /**
     * Reçoit des données du Router lors de la navigation.
     * Utilisé pour passer les paramètres du labyrinthe depuis les écrans de configuration.
     *
     * @param data les données reçues (doit être une instance de ParametresLabyrinthe)
     */
    @Override
    public void receiveData(Object data) {
        if (data instanceof ParametresLabyrinthe params) {
            setParametresLab(
                    params.getLargeur(),
                    params.getHauteur(),
                    params.getPourcentageMurs(),
                    params.getDistanceMin(),
                    params.getTypeLabyrinthe()
            );
        }
    }

    @Override
    public void update() {
        afficherJeu();
    }

    /**
     * Définit les paramètres du labyrinthe avec le mode clé activé.
     * Utilise la génération de base (sans distanceMin) et configure la vision avec brouillard.
     */
    public void setParametresLabAvecCle(int largeur, int hauteur, double pourcentageMurs, int porteeVision) {
        this.isModeCle = true;
        this.porteeVisionCle = porteeVision;

        jeu.setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs, 0));

        this.generateur = new modele.generateurs.GenerateurParfait(largeur, hauteur, 0, true);
        this.generateur.generer(jeu.getLabyrinthe());

        jeu.resetTimer();

        vue.visionsLabyrinthe.VisionCle visionCle = new vue.visionsLabyrinthe.VisionCle(porteeVision);

        this.renduLabyrinthe = visionCle.createRendu(jeu.getLabyrinthe(), conteneurLabyrinthe, this.jeu);

        overlayMinimap.setVisible(false);
        this.renduMinimap = null;

        jeu.getLabyrinthe().addObserver(this);

        afficherJeu();
    }

}
