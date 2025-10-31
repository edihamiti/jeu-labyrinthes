package boutique.repository;

import boutique.modele.InventaireJoueur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Implémentation de la persistance des inventaires en fichiers JSON.
 * Sauvegarde et charge les inventaires depuis le système de fichiers.
 */
public class DepotInventaireJson implements IDepotInventaire {

    private final String repertoireSauvegarde;
    private final Gson gson;

    /**
     * Construit le dépôt avec un répertoire de sauvegarde personnalisé.
     *
     * @param repertoireSauvegarde le chemin du répertoire où sauvegarder les inventaires
     */
    public DepotInventaireJson(String repertoireSauvegarde) {
        this.repertoireSauvegarde = repertoireSauvegarde;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        creerRepertoireSiNecessaire();
    }

    /**
     * Construit le dépôt avec le répertoire de sauvegarde par défaut "sauvegardes/".
     */
    public DepotInventaireJson() {
        this("saves/inventaireJoueurs/");
    }

    private void creerRepertoireSiNecessaire() {
        try {
            Path chemin = Paths.get(repertoireSauvegarde);
            if (!Files.exists(chemin)) {
                Files.createDirectories(chemin);
            }
        } catch (IOException e) {
            System.err.println("Erreur création répertoire: " + e.getMessage());
        }
    }

    @Override
    public void sauvegarder(String idJoueur, InventaireJoueur inventaire) {
        try {
            String json = gson.toJson(inventaire);
            Path fichier = Paths.get(repertoireSauvegarde, idJoueur, "_inventaire.json");
            Files.writeString(fichier, json);
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde: " + e.getMessage());
        }
    }

    @Override
    public InventaireJoueur charger(String idJoueur) {
        try {
            Path fichier = Paths.get(repertoireSauvegarde, idJoueur, "_inventaire.json");
            if (Files.exists(fichier)) {
                String json = Files.readString(fichier);
                return gson.fromJson(json, InventaireJoueur.class);
            }
        } catch (IOException e) {
            System.err.println("Erreur chargement: " + e.getMessage());
        }

        return new InventaireJoueur(0);
    }

}
