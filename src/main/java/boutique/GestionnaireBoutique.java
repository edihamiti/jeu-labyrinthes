package boutique;

import boutique.controleur.ControleurBoutique;
import boutique.controleur.ControleurCasier;
import boutique.modele.TypeCosmetique;
import boutique.repository.DepotCosmetiqueMemoire;
import boutique.repository.DepotInventaireJson;
import boutique.repository.IDepotCosmetique;
import boutique.repository.IDepotInventaire;
import boutique.service.IServiceAchat;
import boutique.service.IServiceEquipement;
import boutique.service.ServiceAchat;
import boutique.service.ServiceEquipement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Gestionnaire central de la boutique suivant le pattern Singleton.
 * Coordonne l'accès aux services de la boutique et gère l'ouverture de l'interface.
 */
public class GestionnaireBoutique {
    private static GestionnaireBoutique instance;

    private final IServiceAchat serviceAchat;
    private final IServiceEquipement serviceEquipement;
    private final IDepotCosmetique depotCosmetique;
    private final IDepotInventaire depotInventaire;

    private GestionnaireBoutique() {
        this.depotCosmetique = new DepotCosmetiqueMemoire();
        this.depotInventaire = new DepotInventaireJson();
        this.serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);
        this.serviceEquipement = new ServiceEquipement(depotCosmetique, depotInventaire);
    }

    /**
     * Récupère l'instance unique du gestionnaire de boutique.
     *
     * @return l'instance du gestionnaire
     */
    public static GestionnaireBoutique getInstance() {
        if (instance == null) instance = new GestionnaireBoutique();
        return instance;
    }

    /**
     * Ouvre la vue Boutique sur le stage principal en initialisant son contrôleur.
     *
     * @param stagePrincipal le stage de l'application
     * @param idJoueur       identifiant du joueur (String)
     */
    public void ouvrirBoutique(Stage stagePrincipal, String idJoueur) {
        try {
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/boutique/Boutique.fxml"));
            Scene scene = new Scene(chargeur.load());
            ControleurBoutique controleur = chargeur.getController();
            controleur.initialiser(serviceAchat, serviceEquipement, depotCosmetique, depotInventaire, idJoueur);
            stagePrincipal.setTitle("Boutique");
            stagePrincipal.setScene(scene);
            stagePrincipal.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ouvre la vue Inventaire (Casier) sur le stage principal en initialisant son contrôleur.
     *
     * @param stagePrincipal le stage de l'application
     * @param idJoueur       identifiant du joueur (String)
     */
    public void ouvrirInventaire(Stage stagePrincipal, String idJoueur) {
        try {
            FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/boutique/InventaireCosmetique.fxml"));
            Scene scene = new Scene(chargeur.load());
            ControleurCasier controleur = chargeur.getController();
            controleur.initialiser(serviceEquipement, depotCosmetique, depotInventaire, idJoueur);
            stagePrincipal.setTitle("Mon Casier");
            stagePrincipal.setScene(scene);
            stagePrincipal.setMaximized(true);
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
