package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique du labyrinthe.
 */
public class LabyrintheRendu {
    private final Image imgMur = new Image(getClass().getResourceAsStream("/img/mur.png"));
    private final Image imgChemin = new Image(getClass().getResourceAsStream("/img/chemin.png"));
    private final Image imgSortie = new Image(getClass().getResourceAsStream("/img/sortie.png"));
    private final Image imgJoueur = new Image(getClass().getResourceAsStream("/img/joueur.png"));
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;

    /**
     * Constructeur de la classe LabyrintheRendu.
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param contienLabyrinthe   Le conteneur VBox pour afficher le labyrinthe.
     */
    public LabyrintheRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
    }

    /**
     * Rend le labyrinthe sous forme de Canvas.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return Le Canvas représentant le labyrinthe.
     */
    public Canvas rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        return creerCanvasLabyrinthe(labyrinthe.getCellules());
    }

    /**
     * Affiche le labyrinthe dans le conteneur VBox.
     */
    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(creerCanvasLabyrinthe(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant le labyrinthe.
     *
     * @param labyrinthe Le labyrinthe sous forme de matrice de cellules.
     * @return Le Canvas représentant le labyrinthe.
     */
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

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setContienLabyrinthe(VBox contienLabyrinthe) {
        this.contienLabyrinthe = contienLabyrinthe;
    }
}
