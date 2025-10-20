package controleur;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

import java.io.IOException;

public class JeuControleur {

    private final Image imgMur = new Image(getClass().getResourceAsStream("/img/mur.png"));
    private final Image imgChemin = new Image(getClass().getResourceAsStream("/img/chemin.png"));
    private final Image imgSortie = new Image(getClass().getResourceAsStream("/img/sortie.png"));
    private final Image imgJoueur = new Image(getClass().getResourceAsStream("/img/joueur.png"));
    @FXML
    private VBox contienLabyrinthe;
    private Labyrinthe labyrinthe;

    @FXML
    public void initialize() {
        // Pour les tests
        this.labyrinthe = new Labyrinthe(10, 10, 10);
        labyrinthe.generer();
        afficherLabyrinthe();
    }

    public void retourMenu() throws IOException {
        AppControleur.getInstance().MenuPrincipal();
    }

    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(creerCanvasLabyrinthe(this.labyrinthe.getCellules()));
    }

    private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
        int tailleCellule = 50;
        Canvas canvas = new Canvas(labyrinthe[0].length * tailleCellule, labyrinthe[1].length * tailleCellule);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (int i = 0; i < labyrinthe.length; i++) {
            for (int j = 0; j < labyrinthe[1].length; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                if (i == this.labyrinthe.getJoueurX() && j == this.labyrinthe.getJoueurY())
                    graphicsContext.drawImage(imgJoueur, x, y, tailleCellule, tailleCellule);
                else if (labyrinthe[i][j].estMur())
                    graphicsContext.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
                else if (labyrinthe[i][j].estChemin())
                    graphicsContext.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                else if (labyrinthe[i][j].estSortie())
                    graphicsContext.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);

            }
        }

        return canvas;
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

            afficherLabyrinthe();

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
