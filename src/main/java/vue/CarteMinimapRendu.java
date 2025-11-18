package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique de la minimap pour la VUE_CARTE.
 */
public class CarteMinimapRendu implements Rendu {
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;

    /**
     * Constructeur de la classe CarteMinimapRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher la minimap.
     */
    public CarteMinimapRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
    }

    /**
     * Rend la minimap sous forme de Canvas.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return Le Canvas représentant la minimap.
     */
    public Canvas rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        return creerCanvasCarteLabyrinthe(labyrinthe.getCellules());
    }

    @Override
    public void setBlockedWall(int x, int y) {
        // Ne fait rien pour la minimap
    }

    /**
     * Affiche la minimap dans le conteneur VBox.
     */
    public void afficherLabyrinthe() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(creerCanvasCarteLabyrinthe(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant la minimap pour la VUE_CARTE.
     * Cette version affiche une minimap légèrement différente avec un cadre doré.
     *
     * @param cellules La matrice de cellules du labyrinthe.
     * @return Le Canvas représentant la minimap.
     */
    private Canvas creerCanvasCarteLabyrinthe(Cellule[][] cellules) {
        int tailleCellule = 20;
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Dessiner le labyrinthe de base
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                gc.drawImage(cellules[i][j].getTexture(), x, y, tailleCellule, tailleCellule);
            }
        }

        // Dessiner un cadre doré pour distinguer cette minimap
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(3);
        gc.strokeRect(0, 0, hauteurMax * tailleCellule, largeurMax * tailleCellule);

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setContienLabyrinthe(VBox contienLabyrinthe) {
        this.contienLabyrinthe = contienLabyrinthe;
    }
}

