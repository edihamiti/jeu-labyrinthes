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

    /**
     * Initialise le contrôleur et configure les événements de déplacement du joueur.
     */
    @FXML
    public void initialize() {
        this.handlerVictoire = new HandlerVictoire();

        conteneurLabyrinthe.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                if (premierLancement) {
                    afficherPopupTouches();
                    premierLancement = false;
                }

                newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
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

                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> {
                    if (jeu.getLabyrinthe() != null) afficherJeu();
                });
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> {
                    if (jeu.getLabyrinthe() != null) afficherJeu();
                });
            }
        });
    }


    private void afficherPopupTouches() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupCommandes.fxml"));
            Parent popupView = loader.load();

            // Injecter les dépendances dans le contrôleur
            Object controller = loader.getController();
            if (controller instanceof Controleur) {
                ((Controleur) controller).setJeu(this.jeu);
                ((Controleur) controller).setAppControleur(this.appControleur);
            }

            Stage popupStage = new Stage();
            popupStage.initOwner(conteneurLabyrinthe.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            popupStage.setTitle("Commandes de jeu");
            popupStage.setResizable(false);

            Scene scene = new Scene(popupView);
            popupStage.setScene(scene);
            popupStage.setAlwaysOnTop(true);
            popupStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retourne au menu principal de l'application.
     *
     * @throws IOException si le chargement de la vue échoue
     */
    public void retourMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
        Parent menuView = loader.load();
        Controleur controleur = loader.getController();
        controleur.setAppControleur(this.appControleur);
        controleur.setJeu(this.jeu);
        Scene scene = new Scene(menuView, 1400, 900);
        Stage stage = (Stage) conteneurLabyrinthe.getScene().getWindow();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Jeu des Labyrinthes - Menu Principal");


        appControleur.resetGame();
    }

    private void retourModeProgression() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));
        Parent modeProgressionView = loader.load();

        // Injecter les dépendances dans le contrôleur
        Object controller = loader.getController();
        if (controller instanceof Controleur) {
            ((Controleur) controller).setJeu(this.jeu);
            ((Controleur) controller).setAppControleur(this.appControleur);
        }

        Stage stage = (Stage) conteneurLabyrinthe.getScene().getWindow();
        Scene scene = new Scene(modeProgressionView, 1400, 900);
        stage.setScene(scene);
        stage.setMaximized(true);
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
            jeu.startTimer(); //TODO: VÉRIFIER QUE JE FAIT PAS DE LA MERDE ICI
        }

        Random random = new Random();
        if (jeu.getLabyrinthe().deplacer(nouveauX, nouveauY)) {
            SoundManager.playSound("move.mp3");
            if (random.nextInt(100) > 95) SoundManager.playSound("bois.mp3");

            jeu.setNombreDeplacements(jeu.getNombreDeplacements() + 1);

            if (jeu.getLabyrinthe().estSurSortie(nouveauX, nouveauY)) {
                victoire();
            }
        } else {
            SoundManager.playSound("block.mp3");
            renduLabyrinthe.setBlockedWall(nouveauX, nouveauY);
            if (overlayMinimap.isVisible() && renduMinimap != null) {
                renduMinimap.setBlockedWall(nouveauX, nouveauY);
            }
        }
    }

//    private int calculerScore(int nombreDeplacements) { // TODO : Rebouger tout ça dans le calculateur
//        if (Jeu.getInstance().getJoueur() != null && Jeu.getInstance().getDefiEnCours() != null) {
//            Defi defi = Jeu.getInstance().getDefiEnCours();
//            int pointsBase = defi.getPoints();
//            int etape = defi.getEtape();
//
//            //étapes 1, 2 et 3
//            if (etape <= 3) {
//                return pointsBase;
//            }
//
//            //étapes 4, 5 et 6
//            int deplacementsOptimal = (int) (defi.getDistanceMin() * 1.5);
//
//            if (nombreDeplacements > deplacementsOptimal) {
//                int deplacementsSupplementaires = nombreDeplacements - deplacementsOptimal;
//                int penalite = deplacementsSupplementaires / 2; // 1 point de pénalité par 2 déplacements supplémentaires
//                return Math.max(0, pointsBase - penalite);
//            }
//            return pointsBase;
//        }
//        return 0;
//    }

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
        Vision vision = Vision.VUE_LIBRE; // Vision par défaut
        VisionLabyrinthe visionStrategy;

        if (jeu.getDefiEnCours() != null) {
            vision = jeu.getDefiEnCours().vision();
            int porteeVision = jeu.getDefiEnCours().portee();

            // Obtenir la stratégie avec la portée personnalisée si nécessaire
            visionStrategy = VisionFactory.getStrategy(vision, porteeVision);
        } else {
            visionStrategy = VisionFactory.getStrategy(vision, 0);
        }

        // Configurer le rendu principal
        this.renduLabyrinthe = visionStrategy.createRendu(jeu.getLabyrinthe(), conteneurLabyrinthe, this.getJeu());

        // Configurer la minimap si nécessaire
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
