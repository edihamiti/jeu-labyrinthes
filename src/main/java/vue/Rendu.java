package vue;

import javafx.scene.Node;
import modele.Labyrinthe;

public interface Rendu {

    public Node rendu(Labyrinthe labyrinthe);

    public void setBlockedWall(int x, int y);

}
