package vue;

import boutique.modele.TypeCosmetique;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import modele.Cellules.Cellule;
import modele.Cellules.Chemin;
import modele.Cellules.Mur;
import modele.Cellules.Sortie;
import modele.Jeu;
import modele.Labyrinthe;

/**
 * Classe responsable du rendu graphique de la vue locale du joueur.
 */
public class LocaleRendu implements Rendu {
    private int porteeVueLocale; // Portée de la vue locale (nombre de cases autour du joueur)
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;
    private int lastBlockedX = -1;
    private int lastBlockedY = -1;

    private Jeu jeu;

    private Mur mur = new Mur();
    private Chemin chemin = new Chemin();
    private Sortie sortie = new Sortie();
    private Image imageJoueur;

    /**
     * Constructeur de la classe LocaleRendu avec portée personnalisée.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher la vue locale.
     * @param porteeVueLocale   La portée de vision du joueur.
     */
    public LocaleRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe, int porteeVueLocale, Jeu jeu) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
        this.porteeVueLocale = porteeVueLocale;
        this.jeu = jeu;
        initTextureEquipe();
    }

    private void initTextureEquipe() {
        mur.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_MUR));
        chemin.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_CHEMIN));
        sortie.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_SORTIE));
        imageJoueur = new Image(getClass().getResourceAsStream(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_JOUEUR)));
    }

    /**
     * Constructeur de la classe LocaleRendu avec portée par défaut (1 case).
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher la vue locale.
     */
    public LocaleRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe, Jeu jeu) {
        this(labyrinthe, contienLabyrinthe, 1, jeu);
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

        double heightVBox = contienLabyrinthe.getHeight();
        double widthVBox = contienLabyrinthe.getWidth();

        if (heightVBox == 0 && contienLabyrinthe.getScene() != null) {
            heightVBox = contienLabyrinthe.getScene().getHeight() - 200;
            widthVBox = contienLabyrinthe.getScene().getWidth() * 0.5;
        }

        if (heightVBox == 0) {
            heightVBox = 600;
            widthVBox = 600;
        }

        int tailleCote = 2 * porteeVueLocale + 1;

        int tailleCelluleH = (int) (heightVBox / tailleCote);
        int tailleCelluleW = (int) (widthVBox / tailleCote);
        int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);

        if (tailleCellule < 10) {
            tailleCellule = 10;
        }

        Canvas canvas = new Canvas(tailleCote * tailleCellule, tailleCote * tailleCellule);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int overlap = Math.max(2, tailleCellule / 4);
        double halfOverlap = overlap / 2.0;

        for (int dx = -porteeVueLocale; dx <= porteeVueLocale; dx++) {
            for (int dy = -porteeVueLocale; dy <= porteeVueLocale; dy++) {
                int cellX = joueurX + dx;
                int cellY = joueurY + dy;

                double x = (dy + porteeVueLocale) * tailleCellule;
                double y = (dx + porteeVueLocale) * tailleCellule;

                if (cellX >= 0 && cellX < largeurMax && cellY >= 0 && cellY < hauteurMax) {
                    if (cellules[cellX][cellY].estChemin() || cellules[cellX][cellY].estEntree()) {
                        gc.drawImage(chemin.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estSortie()) {
                        gc.drawImage(sortie.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[cellX][cellY].estMur()) {
                        gc.drawImage(mur.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else {
                        gc.clearRect(x, y, tailleCellule, tailleCellule);
                    }
                }
            }
        }

        double joueurX_pos = porteeVueLocale * tailleCellule;
        double joueurY_pos = porteeVueLocale * tailleCellule;
        gc.drawImage(imageJoueur, joueurY_pos, joueurX_pos, tailleCellule, tailleCellule);

        for (int dx = -porteeVueLocale; dx <= porteeVueLocale; dx++) {
            for (int dy = -porteeVueLocale; dy <= porteeVueLocale; dy++) {
                int cellX = joueurX + dx;
                int cellY = joueurY + dy;

                if (cellX >= 0 && cellX < largeurMax && cellY >= 0 && cellY < hauteurMax) {
                    if (cellules[cellX][cellY].estMur()) {
                        double x = (dy + porteeVueLocale) * tailleCellule - halfOverlap;
                        double y = (dx + porteeVueLocale) * tailleCellule - halfOverlap;
                        double w = tailleCellule + overlap;
                        double h = tailleCellule + overlap;

                        if (cellX == lastBlockedX && cellY == lastBlockedY) {
                            gc.drawImage(mur.getTextureBlocked(), x, y, w, h);
                        } else {
                            gc.drawImage(mur.getTexture(), x, y, w, h);
                        }
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
