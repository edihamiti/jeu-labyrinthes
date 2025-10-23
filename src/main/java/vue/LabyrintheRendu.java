package vue;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    private VBox containLabyrinthe;
    private final int tailleCellule = 50;
    private final int porteeVueLocale = 1;

    /**
     * Constructeur de la classe LabyrintheRendu.
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param containLabyrinthe   Le conteneur VBox pour afficher le labyrinthe.
     */
    public LabyrintheRendu(Labyrinthe labyrinthe, VBox containLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.containLabyrinthe = containLabyrinthe;
    }

    /**
     * Rend le labyrinthe complet et la vue locale autour du joueur.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return VBox contenant les vues du labyrinthe.
     */
    public VBox rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;

        VBox vueComplete = new VBox(10);
        vueComplete.setAlignment(Pos.CENTER);

        Text labelVueComplete = new Text("Carte du labyrinthe");
        labelVueComplete.setTextAlignment(TextAlignment.CENTER);

        vueComplete.getChildren().addAll(labelVueComplete, creerCanvasLabyrinthe(labyrinthe.getCellules()));

        VBox vueLocale = new VBox(10);
        vueLocale.setAlignment(Pos.CENTER);

        Text labelVueLocale = new Text("Vue locale (portée de " + porteeVueLocale + " case)");
        labelVueLocale.setTextAlignment(TextAlignment.CENTER);

        vueLocale.getChildren().addAll(labelVueLocale, creerCanvasVueLocale());

        VBox conteneur = new VBox(20);
        conteneur.setAlignment(Pos.CENTER);
        conteneur.getChildren().addAll(vueComplete, vueLocale);

        return conteneur;
    }

    /**
     * Affiche le labyrinthe dans le conteneur VBox.
     */
    public void afficherLabyrinthe() {
        containLabyrinthe.getChildren().clear();
        containLabyrinthe.getChildren().add(creerCanvasLabyrinthe(this.labyrinthe.getCellules()));
    }

    /**
     * Crée un Canvas représentant le labyrinthe.
     *
     * @param labyrinthe Le labyrinthe sous forme de matrice de cellules.
     * @return Le Canvas représentant le labyrinthe.
     */
    private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                //Le if en dessous avec la position du joueur (les deux premières lignes) sera ce qu'il faudra enlever pour afficher le labyrinthe sans le joueur !
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

    /**
     * Crée un Canvas représentant la vue locale autour du joueur.
     *
     * @return Le Canvas représentant la vue locale.
     */
    private Canvas creerCanvasVueLocale() {
        int joueurX = this.labyrinthe.getJoueurX();
        int joueurY = this.labyrinthe.getJoueurY();
        Cellule[][] cellules = this.labyrinthe.getCellules();
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        int tailleCote = 2 * porteeVueLocale + 1;

        Canvas canvas = new Canvas(tailleCote * tailleCellule, tailleCote * tailleCellule);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int dx = -porteeVueLocale; dx <= porteeVueLocale; dx++) {
            for (int dy = -porteeVueLocale; dy <= porteeVueLocale; dy++) {
                int cellX = joueurX + dx;
                int cellY = joueurY + dy;

                double x = (dy + porteeVueLocale) * tailleCellule;
                double y = (dx + porteeVueLocale) * tailleCellule;

                if (cellX >= 0 && cellX < largeurMax && cellY >= 0 && cellY < hauteurMax) {
                    if (dx == 0 && dy == 0) {
                        gc.drawImage(imgJoueur, x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estMur()) {
                        gc.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estChemin()) {
                        gc.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estSortie()) {
                        gc.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
                    }
                }
            }
        }

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setContainLabyrinthe(VBox containLabyrinthe) {
        this.containLabyrinthe = containLabyrinthe;
    }
}
