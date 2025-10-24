package vue;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique de la vue locale du joueur.
 */
public class LocaleRendu implements Rendu {
    private final Image imgMur = new Image(getClass().getResourceAsStream("/img/mur.png"));
    private final Image imgRedWall = new Image(getClass().getResourceAsStream("/img/redWall.png"));
    private final Image imgChemin = new Image(getClass().getResourceAsStream("/img/Chemin.png"));
    private final Image imgSortie = new Image(getClass().getResourceAsStream("/img/sortie.png"));
    private final Image imgJoueur = new Image(getClass().getResourceAsStream("/img/joueur.png"));
    private final int tailleCellule = 175; // Taille des cellules pour la vue locale
    private final int porteeVueLocale = 1; // Portée de la vue locale (1 case autour du joueur)
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    /**
     * Constructeur de la classe LocaleRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher la vue locale.
     */
    public LocaleRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
    }

    /**
     * Rend le labyrinthe complet et la vue locale autour du joueur.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return VBox contenant les vues du labyrinthe.
     */
    public VBox rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;

        VBox vueLocale = new VBox(10);
        vueLocale.setAlignment(Pos.CENTER);

        vueLocale.getChildren().addAll(creerCanvasVueLocale());

        VBox conteneur = new VBox(20);
        conteneur.setAlignment(Pos.CENTER);
        conteneur.getChildren().addAll(vueLocale);

        return conteneur;
    }

    /**
     * Affiche la vue locale dans le conteneur VBox.
     */
    public void afficherVueLocale() {
        contienLabyrinthe.getChildren().clear();
        contienLabyrinthe.getChildren().add(creerCanvasVueLocale());
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
        afficherVueLocale();
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
                        if (cellX == lastBlockedX && cellY == lastBlockedY) {
                            gc.drawImage(imgRedWall, x, y, tailleCellule, tailleCellule);
                        } else {
                            gc.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
                        }
                    } else if (cellules[cellX][cellY].estChemin()) {
                        gc.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estSortie()) {
                        gc.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
                    }
                }
            }
        }

        lastBlockedX = -1;
        lastBlockedY = -1;

        return canvas;
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setContienLabyrinthe(VBox contienLabyrinthe) {
        this.contienLabyrinthe = contienLabyrinthe;
    }
}
