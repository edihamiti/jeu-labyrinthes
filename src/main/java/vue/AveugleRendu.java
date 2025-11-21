package vue;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import modele.Cellules.Cellule;
import modele.Jeu;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique du labyrinthe.
 */
public class AveugleRendu implements Rendu {
    private Labyrinthe labyrinthe;
    private Jeu jeu;
    private VBox conteneurLabyrinthe;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    /**
     * Constructeur de la classe LabyrintheRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     * @param jeu
     */
    public AveugleRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe, Jeu jeu) {
        this.labyrinthe = labyrinthe;
        this.jeu = jeu;
        this.conteneurLabyrinthe = conteneurLabyrinthe;
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
     * Définit le mur bloqué à une position spécifique.
     *
     * @param x La coordonnée X de la cellule du mur bloqué.
     * @param y La coordonnée Y de la cellule du mur bloqué.
     */
    public void setBlockedWall(int x, int y) {
        this.lastBlockedX = x;
        this.lastBlockedY = y;
        afficherLabyrinthe();
    }

    /**
     * Affiche le labyrinthe dans le conteneur VBox.
     */
    public void afficherLabyrinthe() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(creerCanvasLabyrinthe(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant le labyrinthe.
     *
     * @param labyrinthe Le labyrinthe sous forme de matrice de cellules.
     * @return Le Canvas représentant le labyrinthe.
     */
    private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
        double heightVBox = conteneurLabyrinthe.getHeight();
        double widthVBox = conteneurLabyrinthe.getWidth();

        if (heightVBox == 0 && conteneurLabyrinthe.getScene() != null) {
            heightVBox = conteneurLabyrinthe.getScene().getHeight() - 200;
            widthVBox = conteneurLabyrinthe.getScene().getWidth() * 0.5;
        }

        if (heightVBox == 0) {
            heightVBox = 600;
            widthVBox = 600;
        }

        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        int tailleCelluleH = (int) (heightVBox / largeurMax);
        int tailleCelluleW = (int) (widthVBox / hauteurMax);
        int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);

        if (tailleCellule < 10) {
            tailleCellule = 10;
        }

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        if (lastBlockedX >= 0 && lastBlockedY >= 0) {
            graphicsContext.setFill(Paint.valueOf("#610000"));
        } else {
            graphicsContext.setFill(Paint.valueOf("#000000"));
        }


        int overlap = Math.max(2, tailleCellule / 4);

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                graphicsContext.fillRect(x, y, tailleCellule, tailleCellule);
            }
        }

        lastBlockedX = -1;
        lastBlockedY = -1;

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setConteneurLabyrinthe(VBox conteneurLabyrinthe) {
        this.conteneurLabyrinthe = conteneurLabyrinthe;
    }
}
