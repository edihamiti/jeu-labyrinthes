package controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class ModeProgressionControleur {

    @FXML
    private GridPane grilleGrid;

    @FXML
    public void initialize() {
        String[] colonnes = {"rien", "1", "2", "3"};
        String[] lignes = {"facil", "moyen", "difficile"};

        double prefWidth = 140;
        double prefHeight = 100;

        for (int col = 0; col < colonnes.length; col++) {
            Button header = new Button(colonnes[col]);
            header.setPrefSize(prefWidth, prefHeight);
            grilleGrid.add(header, col, 0);
        }

        // Boutons OK
        for (int row = 0; row < lignes.length; row++) {
            Button rowHeader = new Button(lignes[row]);
            rowHeader.setPrefSize(prefWidth, prefHeight);
            grilleGrid.add(rowHeader, 0, row + 1);

            for (int col = 1; col <= 3; col++) {
                int niveau = col;
                Button okButton = new Button("ok");
                okButton.setPrefSize(prefWidth, prefHeight);
                grilleGrid.add(okButton, col, row + 1);
            }
        }
    }
}
