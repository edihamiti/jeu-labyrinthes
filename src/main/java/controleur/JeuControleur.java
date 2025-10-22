package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import modele.Joueur;
import modele.Labyrinthe;
import vue.LabyrintheRendu;

import java.io.IOException;

public class JeuControleur {
    @FXML
    private VBox contienLabyrinthe;
    private Labyrinthe labyrinthe;
    private LabyrintheRendu rendu;

    @FXML
    public void initialize() {
        // Pour les tests
        this.labyrinthe = new Labyrinthe(10, 10, 10);
        labyrinthe.generer();

        this.rendu = new LabyrintheRendu(labyrinthe, contienLabyrinthe);

        labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        labyrinthe.joueurYProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());

        afficherLabyrinthe();

        contienLabyrinthe.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
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
            }
        });
    }

    public void retourMenu() throws IOException {
        AppControleur.getInstance().MenuPrincipal();
    }

    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(rendu.rendu(labyrinthe));
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

    private void deplacer(int nouveauX, int nouveauY) throws IOException {
        if (labyrinthe.peutDeplacer(nouveauX, nouveauY)) {
            labyrinthe.setJoueurX(nouveauX);
            labyrinthe.setJoueurY(nouveauY);
            playSound("move.mp3");

            if (labyrinthe.estSurSortie(nouveauX, nouveauY)) {
                victoire();
            }
        } else {
            playSound("block.mp3");
        }
    }

    private void playSound(String sound) {
        AudioClip audio = new AudioClip(getClass().getResource("/sounds/"+sound).toExternalForm());
        audio.play();
    }

    private void victoire() throws IOException {
        playSound("win.mp3");
        labyrinthe.setJeuEnCours(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire");
        alert.setHeaderText("Félicitations");
        alert.setContentText("Vous avez trouvé la sortie");
        alert.showAndWait();
        AppControleur.getInstance().MenuPrincipal();
    }

    public void setParametres(int largeur, int hauteur, double pourcentageMurs, Joueur joueur) {
        this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
        labyrinthe.generer();
        this.rendu = new LabyrintheRendu(labyrinthe, contienLabyrinthe);


        labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
        labyrinthe.joueurYProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());

        afficherLabyrinthe();
    }
}
