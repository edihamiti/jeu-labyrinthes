package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import modele.*;
import modele.generateurs.GenerateurLabyrinthe;
import vue.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

/**
 * Contrôleur pour la gestion du jeu de labyrinthe.
 */
public class JeuControleur {
    private static boolean premierLancement = true;
    @FXML
    public VBox minimap;
    @FXML
    public VBox overlayMinimap;
    @FXML
    private VBox conteneurLabyrinthe;
    private Rendu renduLabyrinthe;
    private MiniMapRendu renduMinimap;
    private TypeLabyrinthe typeLab;
    private GenerateurLabyrinthe generateur;
    private LimiteeRendu renduLimitee;
    private UpdateRendu renduUpdate;  // Nouveau rendu pour l'étape 6

    /**
     * Initialise le contrôleur et configure les événements de déplacement du joueur.
     */
    @FXML
    public void initialize() {
        // Ne plus initialiser le labyrinthe ici
        // Attendre que setParametresLab() soit appelé

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
                    if (Jeu.getInstance().getLabyrinthe() != null) afficherJeu();
                });
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> {
                    if (Jeu.getInstance().getLabyrinthe() != null) afficherJeu();
                });
            }
        });
    }


    private void afficherPopupTouches() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupCommandes.fxml"));
            Parent popupView = loader.load();

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
        AppControleur.getInstance().resetGame();
        AppControleur.getInstance().MenuPrincipal();
    }

    private void retourModeProgression() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));
        Parent modeProgressionView = loader.load();
        Stage stage = (Stage) conteneurLabyrinthe.getScene().getWindow();
        Scene scene = new Scene(modeProgressionView, 1400, 900);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    public void afficherJeu() {
        if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
            if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LOCAL)) {
                afficherMinimap();
                afficherLabyrinthe();
            } else if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LIMITEE)) {
                afficherLimitee();
            } else if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_CARTE)) {
                // Étape 6: Vue locale avec carte progressive qui se met à jour
                afficherCarteProgressive();
                afficherLabyrinthe();
            } else {
                // Vue libre par défaut
                afficherLabyrinthe();
            }
        } else {
            // Mode libre
            afficherLabyrinthe();
        }
    }

    /**
     * Affiche le labyrinthe dans l'interface utilisateur.
     */
    public void afficherLabyrinthe() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(renduLabyrinthe.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherLocale() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(renduLabyrinthe.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherLimitee(){
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(renduLimitee.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherMinimap() {
        minimap.getChildren().clear();
        minimap.getChildren().add(renduMinimap.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherCarteProgressive() {
        minimap.getChildren().clear();
        minimap.getChildren().add(renduUpdate.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    @FXML
    public void deplacerHaut() throws IOException {
        deplacer(Jeu.getInstance().getLabyrinthe().getJoueurX() - 1, Jeu.getInstance().getLabyrinthe().getJoueurY());
    }

    @FXML
    public void deplacerBas() throws IOException {
        deplacer(Jeu.getInstance().getLabyrinthe().getJoueurX() + 1, Jeu.getInstance().getLabyrinthe().getJoueurY());
    }

    @FXML
    public void deplacerGauche() throws IOException {
        deplacer(Jeu.getInstance().getLabyrinthe().getJoueurX(), Jeu.getInstance().getLabyrinthe().getJoueurY() - 1);
    }

    @FXML
    public void deplacerDroite() throws IOException {
        deplacer(Jeu.getInstance().getLabyrinthe().getJoueurX(), Jeu.getInstance().getLabyrinthe().getJoueurY() + 1);
    }

    /**
     * Déplace le joueur vers une nouvelle position si le déplacement est valide.
     *
     * @param nouveauX la nouvelle position X du joueur
     * @param nouveauY la nouvelle position Y du joueur
     * @throws IOException si une erreur survient lors du déplacement
     */
    private void deplacer(int nouveauX, int nouveauY) throws IOException {
        if (Jeu.getInstance().getStart() == null) {
            Jeu.getInstance().setStart(java.time.LocalTime.now());
        }

        Random random = new Random();
        if (Jeu.getInstance().getLabyrinthe().peutDeplacer(nouveauX, nouveauY)) {
            Jeu.getInstance().getLabyrinthe().setJoueurX(nouveauX);
            Jeu.getInstance().getLabyrinthe().setJoueurY(nouveauY);
            playSound("move.mp3");
            if (random.nextInt(100) > 95) playSound("bois.mp3");

            if (Jeu.getInstance().getLabyrinthe().estSurSortie(nouveauX, nouveauY)) {
                Jeu.getInstance().setEnd(java.time.LocalTime.now());
                victoire();
            }
        } else {
            playSound("block.mp3");

            // Utiliser le bon rendu selon le mode de vision pour garder le brouillard actif
            if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION) &&
                Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LIMITEE)) {
                // Pour la vue limitée, utiliser renduLimitee pour conserver le brouillard
                renduLimitee.setBlockedWall(nouveauX, nouveauY);
            } else {
                // Pour les autres modes, utiliser renduLabyrinthe
                renduLabyrinthe.setBlockedWall(nouveauX, nouveauY);
            }
        }
    }

    private void playSound(String sound) {
        AudioClip audio = new AudioClip(getClass().getResource("/sounds/" + sound).toExternalForm());
        audio.play();
    }

    private int calculerScore(long minutes, long secondes) {
        if (Jeu.getInstance().getJoueur() != null && Jeu.getInstance().getDefiEnCours() != null) {
            int pointsBase = Jeu.getInstance().getDefiEnCours().getPoints();
            long tempsTotal = minutes * 60 + secondes;

            if (tempsTotal > 10) {
                long tempsSupplementaire = tempsTotal - 10;
                int penalite = (int) (tempsSupplementaire / 10);
                return Math.max(0, pointsBase - penalite);
            }
            return pointsBase;
        }
        return 0;
    }

    /**
     * Gère la victoire du joueur lorsqu'il atteint la sortie du labyrinthe.
     *
     * @throws IOException si une erreur survient lors du retour au menu principal
     */
    private void victoire() throws IOException {
        playSound("win.mp3");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupVictoire.fxml"));
            Parent popupView = loader.load();
            PopupVictoireControleur controleur = loader.getController();

            LocalTime debut = Jeu.getInstance().getStart();
            LocalTime fin = Jeu.getInstance().getEnd() != null ? Jeu.getInstance().getEnd() : LocalTime.now();
            Duration duree = Duration.between(debut, fin);
            long minutes = duree.toMinutes();
            long secondes = duree.toSeconds() % 60;

            controleur.setTemps(minutes + " min " + secondes + " sec");

            if (Jeu.getInstance().getJoueur() != null && Jeu.getInstance().getDefiEnCours() != null) {
                int scoreObtenu = calculerScore(minutes, secondes);

                Jeu.getInstance().getJoueur().ajouterScore(scoreObtenu, Jeu.getInstance().getDefiEnCours());
                Jeu.getInstance().getSauvegarde().sauvegardeJoueurs();
                controleur.setScore(String.valueOf(Jeu.getInstance().getJoueur().getScore()));
            }

            controleur.setRejouer(() -> {
                try {
                    if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
                        retourModeProgression();
                    } else {
                        rejouer();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });

            controleur.setRetourMenu(() -> {
                try {
                    retourMenu();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            });

            Stage popupStage = new Stage();
            popupStage.initOwner(conteneurLabyrinthe.getScene().getWindow());
            popupStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            popupStage.setTitle("Victoire");
            popupStage.setResizable(false);

            Scene scene = new Scene(popupView);
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            retourMenu();
        }
    }

    private void rejouer() throws IOException {
        Jeu.getInstance().resetTimer();
        this.generateur.generer(Jeu.getInstance().getLabyrinthe());
        setRenduLabyrinthe();
        afficherJeu();
    }

    public void setRenduLabyrinthe() {
        if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
            if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LOCAL)) {
                overlayMinimap.setVisible(true);
                this.renduMinimap = new MiniMapRendu(Jeu.getInstance().getLabyrinthe(), minimap);
                this.renduLabyrinthe = new LocaleRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);
            } else if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LIMITEE)) {
                overlayMinimap.setVisible(false);
                this.renduLimitee = new LimiteeRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);
                this.renduLabyrinthe = new LabyrintheRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);
            } else if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_CARTE)) {
                overlayMinimap.setVisible(true);
                UpdateRendu.reinitialiserExploration();
                this.renduUpdate = new UpdateRendu(Jeu.getInstance().getLabyrinthe(), minimap);
                this.renduLabyrinthe = new LocaleRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);
            } else {
                overlayMinimap.setVisible(false);
                this.renduLabyrinthe = new LabyrintheRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);
            }
        } else {
            overlayMinimap.setVisible(false);
            this.renduLabyrinthe = new LabyrintheRendu(Jeu.getInstance().getLabyrinthe(), conteneurLabyrinthe);

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
        Jeu.getInstance().setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs, distanceMin));

        // Créer et utiliser le générateur
        this.generateur = typeLab.creerGenerateur(largeur, hauteur, pourcentageMurs, distanceMin);
        this.generateur.generer(Jeu.getInstance().getLabyrinthe());

        // Réinitialiser le timer
        Jeu.getInstance().resetTimer();

        // Configurer le rendu
        setRenduLabyrinthe();

        // Ajouter les listeners pour la position du joueur
        Jeu.getInstance().getLabyrinthe().joueurXProperty().addListener((obs, oldVal, newVal) -> afficherJeu());
        Jeu.getInstance().getLabyrinthe().joueurYProperty().addListener((obs, oldVal, newVal) -> afficherJeu());

        // Afficher le jeu
        afficherJeu();
    }

}
