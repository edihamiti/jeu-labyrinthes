package controleur;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import modele.Jeu;
import modele.Labyrinthe;
import modele.ModeJeu;
import modele.Vision;
import vue.LabyrintheRendu;
import vue.LocaleRendu;
import vue.MiniMapRendu;
import vue.Rendu;

import java.io.IOException;

/**
 * Contrôleur pour la gestion du jeu de labyrinthe.
 */
public class JeuControleur {
    @FXML
    public VBox minimap;
    public VBox overlayMinimap;
    @FXML
    private VBox contienLabyrinthe;

    private Rendu renduLabyrinthe;
    private MiniMapRendu renduMinimap;
    //private LocaleRendu renduLocale;

    /**
     * Initialise le contrôleur et configure les événements de déplacement du joueur.
     */
    @FXML
    public void initialize() {
        // Pour les tests
        Jeu.getInstance().setLabyrinthe(new Labyrinthe(10, 10, 10));
        Jeu.getInstance().getLabyrinthe().generer();

        setRenduLabyrinthe();

        Jeu.getInstance().getLabyrinthe().joueurXProperty().addListener((obs, oldVal, newVal) -> afficherJeu());
        Jeu.getInstance().getLabyrinthe().joueurYProperty().addListener((obs, oldVal, newVal) -> afficherJeu());

        afficherJeu();

        contienLabyrinthe.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Écouter les événements clavier
                newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                    try {
                        switch (event.getCode()) {
                            case UP:
                            case Z:
                                deplacerHaut();
                                event.consume();
                                break;
                            case RIGHT:
                            case D:
                                deplacerDroite();
                                event.consume();
                                break;
                            case DOWN:
                            case S:
                                deplacerBas();
                                event.consume();
                                break;
                            case LEFT:
                            case Q:
                                deplacerGauche();
                                event.consume();
                                break;
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                });

                newScene.widthProperty().addListener((obsWidth, oldWidth, newWidth) -> afficherJeu());
                newScene.heightProperty().addListener((obsHeight, oldHeight, newHeight) -> afficherJeu());
            }
        });
    }

    /**
     * Retourne au menu principal de l'application.
     *
     * @throws IOException si le chargement de la vue échoue
     */
    public void retourMenu() throws IOException {
        FXMLLoader loader;
        if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
            loader = new FXMLLoader(getClass().getResource("/ModeProgression.fxml"));
        } else {
            loader = new FXMLLoader(getClass().getResource("/ModeLibreParametres.fxml"));
        }
        Parent jeuView = loader.load();
        Stage stage = (Stage) minimap.getScene().getWindow();
        Scene jeuScene = new Scene(jeuView, 1400, 900);
        stage.setScene(jeuScene);
        stage.show();
        stage.setMaximized(true);
    }

    public void afficherJeu() {
        if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
            if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LOCAL)) {
                afficherMinimap();
            }
        }
        afficherLabyrinthe();
    }

    /**
     * Affiche le labyrinthe dans l'interface utilisateur.
     */
    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(renduLabyrinthe.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherLocale() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(renduLabyrinthe.rendu(Jeu.getInstance().getLabyrinthe()));
    }

    public void afficherMinimap() {
        minimap.getChildren().clear();
        minimap.getChildren().add(renduMinimap.rendu(Jeu.getInstance().getLabyrinthe()));
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
        if (Jeu.getInstance().getLabyrinthe().peutDeplacer(nouveauX, nouveauY)) {
            Jeu.getInstance().getLabyrinthe().setJoueurX(nouveauX);
            Jeu.getInstance().getLabyrinthe().setJoueurY(nouveauY);
            playSound("move.mp3");

            if (Jeu.getInstance().getLabyrinthe().estSurSortie(nouveauX, nouveauY)) {
                victoire();
            }
        } else {
            playSound("block.mp3");
            renduLabyrinthe.setBlockedWall(nouveauX, nouveauY);
            renduLabyrinthe.setBlockedWall(nouveauX, nouveauY);
        }
    }

    private void playSound(String sound) {
        AudioClip audio = new AudioClip(getClass().getResource("/sounds/" + sound).toExternalForm());
        audio.play();
    }

    /**
     * Gère la victoire du joueur lorsqu'il atteint la sortie du labyrinthe.
     *
     * @throws IOException si une erreur survient lors du retour au menu principal
     */
    private void victoire() throws IOException {
        playSound("win.mp3");
        String text = Jeu.getInstance().terminerPartie(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire");
        alert.setHeaderText("Félicitations");
        alert.setContentText(text);
        alert.showAndWait();
        retourMenu();
    }

    public void setRenduLabyrinthe() {
        if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
            if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LOCAL)) {
                overlayMinimap.setVisible(true);
                this.renduMinimap = new MiniMapRendu(Jeu.getInstance().getLabyrinthe(), minimap);
                this.renduLabyrinthe = new LocaleRendu(Jeu.getInstance().getLabyrinthe(), contienLabyrinthe);
            } else {
                overlayMinimap.setVisible(false);
                this.renduLabyrinthe = new LabyrintheRendu(Jeu.getInstance().getLabyrinthe(), contienLabyrinthe);
            }
        } else {
            overlayMinimap.setVisible(false);
            this.renduLabyrinthe = new LabyrintheRendu(Jeu.getInstance().getLabyrinthe(), contienLabyrinthe);
        }

    }

    /**
     * Définit les paramètres du labyrinthe et initialise le renduLabyrinthe.
     *
     * @param largeur         la largeur du labyrinthe
     * @param hauteur         la hauteur du labyrinthe
     * @param pourcentageMurs le pourcentage de murs dans le labyrinthe
     */
    public void setParametresLab(int largeur, int hauteur, double pourcentageMurs) {
        Jeu.getInstance().setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs));
        Jeu.getInstance().getLabyrinthe().generer();

        setRenduLabyrinthe();

        Jeu.getInstance().getLabyrinthe().joueurXProperty().addListener((obs, oldVal, newVal) -> {
            afficherJeu();
        });
        Jeu.getInstance().getLabyrinthe().joueurYProperty().addListener((obs, oldVal, newVal) -> {
            afficherJeu();
        });

        afficherJeu();
    }
}