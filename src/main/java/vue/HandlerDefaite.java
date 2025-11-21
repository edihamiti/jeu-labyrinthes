package vue;

import controleur.PopupDefaiteControleur;
import controleur.PopupVictoireControleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.Jeu;

import java.io.IOException;

public class HandlerDefaite {
    public String handleDefaite(Jeu jeu, Stage ownerStage, Runnable onRejouer, Runnable onRetourMenu) throws IOException {
        SoundManager.playSound("win.mp3");
        String result = jeu.terminerPartie(false);

        afficherPopupDefaite(jeu, ownerStage, onRejouer, onRetourMenu, result);
        return result;
    }

    private void afficherPopupDefaite(Jeu jeu, Stage ownerStage, Runnable onRejouer, Runnable onRetourMenu, String message) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopupDefaite.fxml"));
        Parent popupView = loader.load();
        PopupDefaiteControleur controleur = loader.getController();

        if (message != null && !message.isEmpty()) {
            controleur.setMessage(message);
        }


        controleur.setRejouer(onRejouer);
        controleur.setRetourMenu(onRetourMenu);
        controleur.setJeu(jeu);

        Stage popupStage = new Stage();
        popupStage.initOwner(ownerStage);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Defaite");
        popupStage.setResizable(false);

        Scene scene = new Scene(popupView);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }
}
