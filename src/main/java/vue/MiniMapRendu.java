package vue;

import boutique.modele.TypeCosmetique;
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
 * Classe responsable du rendu graphique du labyrinthe.
 */
public class MiniMapRendu implements Rendu {
    private Labyrinthe labyrinthe;
    private VBox contienLabyrinthe;
    private Jeu jeu;

    private Mur mur = new Mur();
    private Chemin chemin = new Chemin();
    private Sortie sortie = new Sortie();
    private Image imageJoueur;

    /**
     * Constructeur de la classe MiniMapRendu.
     *
     * @param labyrinthe        Le labyrinthe à rendre.
     * @param contienLabyrinthe Le conteneur VBox pour afficher le labyrinthe.
     * @param jeu               Le jeu contenant les informations sur les textures.
     */
    public MiniMapRendu(Labyrinthe labyrinthe, VBox contienLabyrinthe, Jeu jeu) {
        this.labyrinthe = labyrinthe;
        this.contienLabyrinthe = contienLabyrinthe;
        this.jeu = jeu;
        initTextureEquipe();
    }

    /**
     * Initialise les textures équipées par le joueur.
     */
    private void initTextureEquipe() {
        mur.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_MUR));
        chemin.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_CHEMIN));
        sortie.setImagePath(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_SORTIE));
        imageJoueur = new Image(getClass().getResourceAsStream(jeu.getBoutique().obtenirTextureEquipee(jeu.getJoueur().getPseudo(), TypeCosmetique.TEXTURE_JOUEUR)));
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

    @Override
    public void setBlockedWall(int x, int y) {

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
        int tailleCellule = 20;
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                if (labyrinthe[i][j].estChemin() || labyrinthe[i][j].estEntree()) {
                    graphicsContext.drawImage(chemin.getTexture(), x, y, tailleCellule, tailleCellule);
                } else if (labyrinthe[i][j].estSortie()) {
                    graphicsContext.drawImage(sortie.getTexture(), x, y, tailleCellule, tailleCellule);
                } else if (labyrinthe[i][j].estMur()) {
                    graphicsContext.drawImage(mur.getTexture(), x, y, tailleCellule, tailleCellule);
                } else {
                    graphicsContext.clearRect(x, y, tailleCellule, tailleCellule);
                }
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
