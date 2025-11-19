package boutique.controleur;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.repository.IDepotCosmetique;
import boutique.repository.IDepotInventaire;
import boutique.service.IServiceAchat;
import boutique.service.IServiceEquipement;
import boutique.service.ResultatAchat;
import controleur.Controleur;
import controleur.HomePageControleur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Contrôleur JavaFX de la vue principale de la boutique.
 */
public class ControleurBoutique extends Controleur {

    @FXML
    private Label labelScore;
    @FXML
    private HBox conteneurCosmetiques;

    private IServiceAchat serviceAchat;
    private IServiceEquipement serviceEquipement;
    private IDepotCosmetique depotCosmetique;
    private IDepotInventaire depotInventaire;
    private String idJoueur;
    private modele.Joueur joueur;

    public void initialiser(IServiceAchat serviceAchat, IServiceEquipement serviceEquipement, IDepotCosmetique depotCosmetique, IDepotInventaire depotInventaire, modele.Joueur joueur) {
        this.serviceAchat = serviceAchat;
        this.serviceEquipement = serviceEquipement;
        this.depotCosmetique = depotCosmetique;
        this.depotInventaire = depotInventaire;
        this.joueur = joueur;
        this.idJoueur = joueur.getPseudo();
        rafraichirVue();
    }

    private void rafraichirVue() {
        if (depotInventaire == null || depotCosmetique == null || idJoueur == null) {
            labelScore.setText("0");
            conteneurCosmetiques.getChildren().clear();
            return;
        }

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        labelScore.setText(String.valueOf(joueur != null ? joueur.getScore() : inventaire.getScore()));
        conteneurCosmetiques.getChildren().clear();

        List<Cosmetique> cosmetiques = depotCosmetique.obtenirTous();
        for (Cosmetique cosmetique : cosmetiques) {
            conteneurCosmetiques.getChildren().add(creerCarteCosmetique(cosmetique, inventaire));
        }
    }

    private VBox creerCarteCosmetique(Cosmetique cosmetique, InventaireJoueur inventaire) {
        VBox carte = new VBox(12);
        carte.setAlignment(Pos.TOP_LEFT);
        carte.getStyleClass().add("cosmetic-card");
        carte.setPrefWidth(250);
        carte.setMaxWidth(250);

        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/" + cosmetique.cheminTexture())), 210, 210, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(210);
            imageView.setFitHeight(210);
            VBox imageContainer = new VBox(imageView);
            imageContainer.setAlignment(Pos.CENTER);
            carte.getChildren().add(imageContainer);
        } catch (Exception e) {
            Label placeholder = new Label("texture à venir");
            placeholder.setStyle("-fx-font-size: 60px;");
            VBox imageContainer = new VBox(placeholder);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefHeight(210);
            carte.getChildren().add(imageContainer);
        }

        VBox infos = new VBox(4);
        infos.setAlignment(Pos.TOP_LEFT);
        Label labelNom = new Label(cosmetique.nom());
        labelNom.getStyleClass().add("cosmetic-name");
        Label labelType = new Label(cosmetique.type().getNomAffichage());
        labelType.getStyleClass().add("cosmetic-type");
        infos.getChildren().addAll(labelNom, labelType);

        HBox badges = new HBox(6);
        badges.setAlignment(Pos.CENTER_LEFT);
        badges.setMinHeight(30);
        badges.setPrefHeight(30);
        boolean possede = inventaire.possedeCosmetique(cosmetique.id());
        boolean equipe = cosmetique.id().equals(inventaire.getCosmetiqueEquipe(cosmetique.type()));
        if (possede) {
            Label badgePossede = new Label("✓ Possédé");
            badgePossede.getStyleClass().add("badge-owned");
            badges.getChildren().add(badgePossede);
            if (equipe) {
                Label badgeEquipe = new Label("★ Équipé");
                badgeEquipe.getStyleClass().add("badge-equipped");
                badges.getChildren().add(badgeEquipe);
            }
        }

        VBox actionsContainer = new VBox(8);
        actionsContainer.setAlignment(Pos.TOP_LEFT);
        Label labelPrix = new Label(cosmetique.prix() + " pts");
        labelPrix.getStyleClass().add("cosmetic-price");
        Button boutonAcheter = new Button(possede ? "Possédé" : "Acheter");
        boutonAcheter.getStyleClass().add("button-filled");
        boutonAcheter.setDisable(possede);
        boutonAcheter.setMaxWidth(Double.MAX_VALUE);
        boutonAcheter.setOnAction(e -> gererAchat(cosmetique.id()));
        actionsContainer.getChildren().addAll(labelPrix, boutonAcheter);

        carte.getChildren().addAll(infos, badges, actionsContainer);
        return carte;
    }

    private void gererAchat(String idCosmetique) {
        if (serviceAchat == null) {
            afficherErreur("Achat non disponible en mode simplifié");
            return;
        }

        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);
        if (resultat.estReussi()) {
            if (joueur != null) {
                InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
                joueur.setScore(inventaire.getScore());
            }
            rafraichirVue();
            /*afficherSucces("✓ Cosmétique acheté avec succès !");*/
        } else {
            afficherErreur(resultat.getMessage());
        }
    }

    @FXML
    private void fermerBoutique() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));

        Parent homepageView = loader.load();
        HomePageControleur controleur = loader.getController();
        controleur.setJeu(this.jeu);
        controleur.setAppControleur(this.appControleur);

        Stage stage = (Stage) labelScore.getScene().getWindow();
        Scene jeuScene = new Scene(homepageView, 1400, 900);
        stage.setScene(jeuScene);
        stage.setMaximized(true);

        System.out.println("Jeu lancé !");
    }

    /*private void afficherSucces(String message) {
        Alert alerte = new Alert(Alert.AlertType.INFORMATION);
        alerte.setTitle("Succès");
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }*/

    private void afficherErreur(String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Erreur");
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
}
