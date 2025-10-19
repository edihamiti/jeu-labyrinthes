package vue;

import javafx.stage.Stage;
import modele.Jeu;

public class MenuPrincipal extends Stage implements LabyrinthesObservateur {
    public MenuPrincipal() {
        this.setTitle("Le Jeu des Labyrinthes");
        this.show();
    }

    @Override
    public void update(Jeu j) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
