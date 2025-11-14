package vue;

import javafx.scene.Node;
import javafx.scene.image.Image;
import modele.Labyrinthe;

public interface Rendu {

    final Image imgMur = new Image(Rendu.class.getResourceAsStream("/img/mur.png"));
    final Image imgChemin = new Image(Rendu.class.getResourceAsStream("/img/chemin.png"));
    final Image imgSortie = new Image(Rendu.class.getResourceAsStream("/img/sortie.png"));
    final Image imgJoueur = new Image(Rendu.class.getResourceAsStream("/img/joueur.png"));
    final Image imgRedWall = new Image(Rendu.class.getResourceAsStream("/img/redWall.png"));

    public Node rendu(Labyrinthe labyrinthe);

    public void setBlockedWall(int x, int y);

}
