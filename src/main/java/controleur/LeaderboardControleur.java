package controleur;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modele.Leaderboard;
import modele.Jeu;
import modele.Joueur;

import java.util.List;

public class LeaderboardControleur {

    @FXML
    private VBox profilsContainer;

    private Leaderboard leaderboard;

    // Appelée après que le FXML est chargé
    public void initLeaderboard() {
        leaderboard = new Leaderboard(Jeu.getInstance().getSauvegarde());
        afficherLeaderboard();
    }

    private void afficherLeaderboard() {
        if (profilsContainer == null) return;

        List<Joueur> joueurs = leaderboard.getClassementComplet();
        profilsContainer.getChildren().clear();

        // entête
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(8));
        Label rankHeader = new Label("Rang");
        rankHeader.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label pseudoHeader = new Label("Pseudo");
        pseudoHeader.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label scoreHeader = new Label("Score");
        scoreHeader.setFont(Font.font("System", FontWeight.BOLD, 14));

        rankHeader.setPrefWidth(60);
        pseudoHeader.setPrefWidth(240);
        scoreHeader.setPrefWidth(120);

        header.getChildren().addAll(rankHeader, pseudoHeader, scoreHeader);
        profilsContainer.getChildren().add(header);

        int rang = 1;
        for (Joueur j : joueurs) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(6));
            row.getStyleClass().add("leaderboard-row");

            Label rankLabel = new Label(String.valueOf(rang));
            rankLabel.setPrefWidth(60);
            Label pseudoLabel = new Label(j.getPseudo());
            pseudoLabel.setPrefWidth(240);
            Label scoreLabel = new Label(String.valueOf(j.getScore()));
            scoreLabel.setPrefWidth(120);

            row.getChildren().addAll(rankLabel, pseudoLabel, scoreLabel);
            profilsContainer.getChildren().add(row);
            rang++;
        }
    }
}
