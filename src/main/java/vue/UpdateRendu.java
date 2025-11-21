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

import java.util.HashSet;
import java.util.Set;

/**
 * Classe responsable du rendu de la carte qui se met à jour progressivement pendant l'exploration.
 * Utilisée pour l'étape 6 (VUE_CARTE) : vue locale + carte globale qui se révèle au fur et à mesure.
 */
public class UpdateRendu implements Rendu {

    private final Image imgBrouillard = new Image(getClass().getResourceAsStream("/img/brouillard.png"));

    private Labyrinthe labyrinthe;
    private VBox conteneurLabyrinthe;
    private int porteeVision;
    private Jeu jeu;

    private Mur mur = new Mur();
    private Chemin chemin = new Chemin();
    private Sortie sortie = new Sortie();
    private Image imageJoueur;

    // Set pour mémoriser les cellules explorées (révélées sur la carte)
    private Set<String> cellulesExplorees = new HashSet<>();

    /**
     * Constructeur de UpdateRendu.
     *
     * @param labyrinthe          Le labyrinthe à rendre.
     * @param conteneurLabyrinthe Le conteneur VBox pour afficher la carte.
     * @param porteeVision        La portée de vision du joueur.
     * @param jeu                 Le jeu contenant les informations sur les textures.
     */
    public UpdateRendu(Labyrinthe labyrinthe, VBox conteneurLabyrinthe, int porteeVision, Jeu jeu) {
        this.labyrinthe = labyrinthe;
        this.conteneurLabyrinthe = conteneurLabyrinthe;
        this.porteeVision = porteeVision;
        this.jeu = jeu;

        // Réinitialiser les cellules explorées pour un nouveau labyrinthe
        cellulesExplorees.clear();

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
     * Rend la carte qui se met à jour au fur et à mesure de l'exploration.
     *
     * @param labyrinthe Le labyrinthe à rendre.
     * @return Le Canvas représentant la carte.
     */
    public Canvas rendu(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;

        // Mettre à jour les cellules explorées en fonction de la position du joueur
        mettreAJourCellulesExplorees();

        return creerCanvasCarte(labyrinthe.getCellules());
    }

    /**
     * Met à jour les cellules explorées en fonction de la portée de vision du joueur.
     */
    private void mettreAJourCellulesExplorees() {
        int joueurX = this.labyrinthe.getJoueurX();
        int joueurY = this.labyrinthe.getJoueurY();


        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();

        // Révéler toutes les cellules dans la portée de vision actuelle
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                int distanceX = Math.abs(i - joueurX);
                int distanceY = Math.abs(j - joueurY);

                if (distanceX <= porteeVision && distanceY <= porteeVision) {
                    // Cette cellule est visible, l'ajouter aux cellules explorées
                    String cle = i + "," + j;
                    cellulesExplorees.add(cle);
                }
            }
        }
    }

    /**
     * Crée un Canvas représentant la carte progressive.
     *
     * @param cellules Les cellules du labyrinthe.
     * @return Le Canvas représentant la carte.
     */
    private Canvas creerCanvasCarte(Cellule[][] cellules) {
        int largeurMax = this.labyrinthe.getLargeurMax();
        int hauteurMax = this.labyrinthe.getHauteurMax();
        int joueurX = this.labyrinthe.getJoueurX();
        int joueurY = this.labyrinthe.getJoueurY();

        double heightVBox = conteneurLabyrinthe.getHeight();
        double widthVBox = conteneurLabyrinthe.getWidth();

        if (heightVBox == 0 && conteneurLabyrinthe.getScene() != null) {
            heightVBox = conteneurLabyrinthe.getScene().getHeight() - 200;
            widthVBox = conteneurLabyrinthe.getScene().getWidth() * 0.3; // Plus petit pour la minimap
        }

        if (heightVBox == 0) {
            heightVBox = 300;
            widthVBox = 300;
        }

        int tailleCelluleH = (int) (heightVBox / largeurMax);
        int tailleCelluleW = (int) (widthVBox / hauteurMax);
        int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);

        if (tailleCellule < 5) {
            tailleCellule = 5;
        }

        Canvas canvas = new Canvas(hauteurMax * tailleCellule, largeurMax * tailleCellule);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                double x = j * tailleCellule;
                double y = i * tailleCellule;

                String cle = i + "," + j;

                if (i == joueurX && j == joueurY) {
                    // Toujours afficher le joueur (visible sur la carte)
                    gc.drawImage(imageJoueur, x, y, tailleCellule, tailleCellule);
                } else if (cellulesExplorees.contains(cle)) {
                    // Cette cellule a été explorée, l'afficher selon son type
                    if (cellules[i][j].estMur()) {
                        gc.drawImage(mur.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[i][j].estChemin() || cellules[i][j].estEntree()) {
                        gc.drawImage(chemin.getTexture(), x, y, tailleCellule, tailleCellule);
                    } else if (cellules[i][j].estSortie()) {
                        // NE PAS afficher la sortie sur la carte (selon les spécifications)
                        gc.drawImage(chemin.getTexture(), x, y, tailleCellule, tailleCellule);
                    }
                } else {
                    // Cellule non explorée, afficher le brouillard
                    gc.drawImage(imgBrouillard, x, y, tailleCellule, tailleCellule);
                }
            }
        }

        return canvas;
    }

    /**
     * Définit le mur bloqué à une position spécifique.
     *
     * @param x La coordonnée X de la cellule du mur bloqué.
     * @param y La coordonnée Y de la cellule du mur bloqué.
     */
    public void setBlockedWall(int x, int y) {
        // Pour l'étape 6, pas de gestion spéciale des murs bloqués sur la carte
        afficherCarte();
    }

    /**
     * Affiche la carte dans le conteneur VBox.
     */
    public void afficherCarte() {
        conteneurLabyrinthe.getChildren().clear();
        conteneurLabyrinthe.getChildren().add(creerCanvasCarte(this.labyrinthe.getCellules()));
    }

    /**
     * Réinitialise les cellules explorées (pour un nouveau labyrinthe).
     */
    public void reinitialiserExploration() {
        cellulesExplorees.clear();
    }

    public void setLabyrinthe(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
    }

    public void setPorteeVision(int porteeVision) {
        this.porteeVision = porteeVision;
    }
}
