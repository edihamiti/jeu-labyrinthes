package modele.boutique;

import controleur.boutique.ControleurBoutique;
import controleur.boutique.ControleurCasier;
import controleur.AppControleur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import modele.Jeu;
import modele.Joueur;
import vue.Router;

/**
 * Gestionnaire central de la boutique suivant le pattern Singleton.
 * Coordonne l'accès aux services de la boutique et gère l'ouverture de l'interface.
 */
public class GestionnaireBoutique {
    private final IServiceAchat serviceAchat;
    private final IServiceEquipement serviceEquipement;
    private final IDepotCosmetique depotCosmetique;
    private final IDepotInventaire depotInventaire;

    public GestionnaireBoutique() {
        this.depotCosmetique = new DepotCosmetiqueMemoire();
        this.depotInventaire = new DepotInventaireJson();
        this.serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);
        this.serviceEquipement = new ServiceEquipement(depotCosmetique, depotInventaire);
    }

    /**
     * Ouvre la vue Boutique sur le stage principal en initialisant son contrôleur.
     *
     * @param stagePrincipal le stage de l'application
     * @param joueur         l'objet Joueur courant
     * @param jeu            l'instance du jeu
     * @param appControleur  le contrôleur principal de l'application
     */
    public void ouvrirBoutique(Stage stagePrincipal, Joueur joueur, Jeu jeu, AppControleur appControleur) {
        if (joueur == null) {
            System.err.println("Impossible d'ouvrir la boutique : joueur null");
            return;
        }

        String idJoueur = joueur.getPseudo();
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.setScore(joueur.getScore());
        depotInventaire.sauvegarder(idJoueur, inventaire);

        try {
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/boutique/Boutique.fxml"));
            Scene scene = new Scene(chargeur.load());
            ControleurBoutique controleur = chargeur.getController();
            controleur.initialiser(serviceAchat, serviceEquipement, depotCosmetique, depotInventaire, joueur);
            controleur.setJeu(jeu);
            controleur.setAppControleur(appControleur);
            stagePrincipal.setTitle("Boutique");
            stagePrincipal.setScene(scene);
            stagePrincipal.setMaximized(true);
            Router.addToHistory("/Boutique.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la vue Inventaire (Casier) sur le stage principal en initialisant son contrôleur.
     *
     * @param stagePrincipal le stage de l'application
     * @param joueur         l'objet Joueur courant
     * @param jeu            l'instance du jeu
     * @param appControleur  le contrôleur principal de l'application
     */
    public void ouvrirInventaire(Stage stagePrincipal, Joueur joueur, Jeu jeu, AppControleur appControleur) {
        if (joueur == null) {
            System.err.println("Impossible d'ouvrir l'inventaire : joueur null");
            return;
        }

        String idJoueur = joueur.getPseudo();
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.setScore(joueur.getScore());
        depotInventaire.sauvegarder(idJoueur, inventaire);

        try {
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/boutique/InventaireCosmetique.fxml"));
            Scene scene = new Scene(chargeur.load());
            ControleurCasier controleur = chargeur.getController();
            controleur.initialiser(serviceEquipement, depotCosmetique, depotInventaire, joueur);
            controleur.setJeu(jeu);
            controleur.setAppControleur(appControleur);
            stagePrincipal.setTitle("Mon Casier");
            stagePrincipal.setScene(scene);
            stagePrincipal.setMaximized(true);
            Router.addToHistory("/InventaireCosmetique.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ajoute des points au score d'un joueur.
     *
     * @param idJoueur     l'identifiant du joueur
     * @param pointsGagnes le nombre de points à ajouter
     */
    public void ajouterScore(String idJoueur, int pointsGagnes) {
        var inventaire = depotInventaire.charger(idJoueur);
        inventaire.setScore(inventaire.getScore() + pointsGagnes);
        depotInventaire.sauvegarder(idJoueur, inventaire);
    }

    /**
     * Récupère le chemin de la texture équipée pour un type donné.
     *
     * @param idJoueur l'identifiant du joueur
     * @param type     le type de cosmétique
     * @return le chemin vers la texture équipée
     */
    public String obtenirTextureEquipee(String idJoueur, TypeCosmetique type) {
        return serviceEquipement.obtenirTextureEquipee(idJoueur, type);
    }
}
