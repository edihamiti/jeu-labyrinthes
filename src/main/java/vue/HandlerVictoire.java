package vue;

import controleur.PopupVictoireControleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Jeu;

import java.io.IOException;

public class HandlerVictoire {
    public String handleVictoire(Jeu jeu, Stage ownerStage, Runnable onRejouer, Runnable onRetourMenu) throws IOException {
        SoundManager.playSound("win.mp3");
        String result = "";

        if (jeu.getJoueur() != null && jeu.getDefiEnCours() != null) {
            result += jeu.terminerPartie(true);
        }

        afficherPopupVictoire(jeu, ownerStage, onRejouer, onRetourMenu);
        return result;
    }

    private void afficherPopupVictoire(Jeu jeu, Stage ownerStage, Runnable onRejouer, Runnable onRetourMenu) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupVictoire.fxml"));
        Parent popupView = loader.load();
        PopupVictoireControleur controleur = loader.getController();

        if (jeu.getJoueur() != null) {
            controleur.setScore(String.valueOf(jeu.getJoueur().getScore()));
        }

        controleur.setRejouer(onRejouer);
        controleur.setRetourMenu(onRetourMenu);

        Stage popupStage = new Stage();
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Victoire");
        popupStage.setResizable(false);

        Scene scene = new Scene(popupView);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
