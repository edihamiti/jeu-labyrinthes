package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
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

            if (labyrinthe.estSurSortie(nouveauX, nouveauY)) {
                victoire();
            }
        }
    }

    private void victoire() throws IOException {
        labyrinthe.setJeuEnCours(false);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Victoire");
        alert.setHeaderText("Félicitations");
        alert.setContentText("Vous avez trouvé la sortie");
        alert.showAndWait();
        AppControleur.getInstance().MenuPrincipal();
    }
}
