package boutique.controleur;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.modele.TypeCosmetique;
import boutique.repository.IDepotCosmetique;
import boutique.repository.IDepotInventaire;
import boutique.service.IServiceEquipement;
import boutique.service.ResultatEquipement;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import vue.Router;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ControleurCasier extends Controleur {
    @FXML
    private HBox possedeContaineur;
    @FXML
    private Label labelVide;
    @FXML
    private HBox categoriesContainer;
    @FXML
    private ScrollPane scrollPaneInventaire;

    private IServiceEquipement serviceEquipement;
    private IDepotCosmetique depotCosmetique;
    private IDepotInventaire depotInventaire;
    private String idJoueur;
    private modele.Joueur joueur;
    private TypeCosmetique categorieSelectionnee = null;
    private Button boutonCategorieActif = null;

    @FXML
    public void initialize() {
        if (labelVide != null) {
            labelVide.setVisible(true);
            labelVide.setManaged(true);
        }
    }

    public void initialiser(IServiceEquipement serviceEquipement, IDepotCosmetique depotCosmetique, IDepotInventaire depotInventaire, modele.Joueur joueur) {
        this.serviceEquipement = serviceEquipement;
        this.depotCosmetique = depotCosmetique;
        this.depotInventaire = depotInventaire;
        this.joueur = joueur;
        this.idJoueur = joueur.getPseudo();
        creerCategories();
        rafraichirVue();
    }

    private void creerCategories() {
        categoriesContainer.getChildren().clear();

        for (TypeCosmetique type : TypeCosmetique.values()) {
            Button boutonCategorie = new Button(type.getNomAffichage());
            boutonCategorie.getStyleClass().add("button-outlined");
            boutonCategorie.setPrefWidth(150);
            boutonCategorie.setMinWidth(150);
            boutonCategorie.setMaxWidth(150);
            boutonCategorie.setOnAction(e -> {
                selectionnerCategorie(type, boutonCategorie);
            });
            categoriesContainer.getChildren().add(boutonCategorie);
        }
    }

    private void selectionnerCategorie(TypeCosmetique type, Button bouton) {
        if (boutonCategorieActif != null) {
            boutonCategorieActif.getStyleClass().remove("button-filled");
            boutonCategorieActif.getStyleClass().add("button-outlined");
        }

        if (categorieSelectionnee == type) {
            categorieSelectionnee = null;
            boutonCategorieActif = null;
        } else {
            categorieSelectionnee = type;
            boutonCategorieActif = bouton;
            bouton.getStyleClass().remove("button-outlined");
            bouton.getStyleClass().add("button-filled");
        }

        rafraichirVue();
    }

    private void rafraichirVue() {
        if (depotInventaire == null || depotCosmetique == null || idJoueur == null) return;
        possedeContaineur.getChildren().clear();

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        List<Cosmetique> cosmetiquesPossedes = depotCosmetique.obtenirTous().stream()
                .filter(c -> inventaire.possedeCosmetique(c.id()))
                .filter(c -> categorieSelectionnee == null || c.type() == categorieSelectionnee)
                .toList();

        if (cosmetiquesPossedes.isEmpty()) {
            scrollPaneInventaire.setVisible(false);
            scrollPaneInventaire.setManaged(false);
            labelVide.setVisible(true);
            labelVide.setManaged(true);
            if (categorieSelectionnee != null) {
                labelVide.setText("Aucun cosmétique " + categorieSelectionnee.getNomAffichage() + " dans votre inventaire.");
            } else {
                labelVide.setText("Vous ne possédez aucun cosmétique pour le moment.\nRendez-vous dans la boutique !");
            }
        } else {
            scrollPaneInventaire.setVisible(true);
            scrollPaneInventaire.setManaged(true);
            labelVide.setVisible(false);
            labelVide.setManaged(false);
            for (Cosmetique cosmetique : cosmetiquesPossedes) {
                possedeContaineur.getChildren().add(creerCarteCosmetiquePossede(cosmetique, inventaire));
            }
        }
    }

    private VBox creerCarteCosmetiquePossede(Cosmetique cosmetique, InventaireJoueur inventaire) {
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
        boolean equipe = cosmetique.id().equals(inventaire.getCosmetiqueEquipe(cosmetique.type()));
        if (equipe) {
            Label badgeEquipe = new Label("★ Équipé");
            badgeEquipe.getStyleClass().add("badge-equipped");
            badges.getChildren().add(badgeEquipe);
        }

        VBox actionsContainer = new VBox(8);
        actionsContainer.setAlignment(Pos.TOP_LEFT);
        Button boutonEquiper = new Button(equipe ? "Équipé" : "Équiper");
        boutonEquiper.getStyleClass().add("button-filled");
        boutonEquiper.setDisable(equipe);
        boutonEquiper.setMaxWidth(Double.MAX_VALUE);
        boutonEquiper.setOnAction(e -> gererEquipement(cosmetique.id()));
        actionsContainer.getChildren().add(boutonEquiper);

        carte.getChildren().addAll(infos, badges, actionsContainer);
        return carte;
    }

    private void gererEquipement(String idCosmetique) {
        if (serviceEquipement == null) {
            afficherErreur("Équipement non disponible en mode simplifié");
            return;
        }
        ResultatEquipement resultat = serviceEquipement.equiperCosmetique(idJoueur, idCosmetique);
        if (resultat.estReussi()) {
            rafraichirVue();
            /*afficherSucces("✓ Cosmétique équipé avec succès !");*/
        } else {
            afficherErreur(resultat.getMessage());
        }
    }

    @FXML
    public void fermeInventaire() throws IOException {
        Router.back();
        System.out.println("Retour à l'accueil !");
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

