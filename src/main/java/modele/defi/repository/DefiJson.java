package modele.defi.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import modele.defi.Defi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Classe utilitaire pour la gestion des défis au format JSON.
 *
 * Permet de charger les défis depuis un fichier JSON et de créer le dépôt
 * correspondant ({@link DefisRepo}). Le fichier JSON est attendu dans un
 * répertoire donné ou par défaut dans "src/main/resources".
 *
 * Utilise la bibliothèque Gson pour la sérialisation et désérialisation.
 *
 * Exemple d'utilisation :
 * DefiJson defiJson = new DefiJson();
 * DefisRepo repo = defiJson.charger();
 */
public class DefiJson {

    private final String repertoire;
    private final Gson gson;


    /**
     * Constructeur principal.
     *
     * @param repertoire chemin du répertoire pour le fichier JSON
     */
    public DefiJson(String repertoire) {
        this.repertoire = repertoire;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }


    /**
     * Constructeur par défaut.
     * Utilise le répertoire "src/main/resources".
     */
    public DefiJson() {
        this("src/main/resources");
    }


    /**
     * Crée le répertoire si celui-ci n'existe pas.
     * Gère les exceptions d'IO et affiche un message d'erreur si nécessaire.
     */
    private void creerRepertoireSiNecessaire() {
        try {
            Path chemin = Paths.get(repertoire);
            if (!Files.exists(chemin)) {
                Files.createDirectories(chemin);
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde: " + e.getMessage());
        }
    }


    /**
     * Charge les défis depuis le fichier JSON "defis.json".
     *
     * Si le fichier existe, les défis sont désérialisés en tableau d'objets
     * {@link Defi} puis ajoutés dans un {@link DefisRepo}.
     * En cas d'erreur ou si le fichier n'existe pas, un dépôt vide est retourné.
     *
     * @return dépôt de défis ({@link DefisRepo}) rempli ou vide
     */
    public DefisRepo charger() {
        try {
            Path fichier = Paths.get(repertoire, "defis.json");
            if (Files.exists(fichier)) {
                String json = Files.readString(fichier);
                Defi[] defisArray = gson.fromJson(json, Defi[].class);

                DefisRepo defisRepo = new DefisRepo();
                if (defisArray != null) {
                    for (Defi defi : defisArray) {
                        defisRepo.ajouterDefi(defi);
                    }
                }
                return defisRepo;
            }
        } catch (IOException | JsonSyntaxException e) {
            System.err.println("Erreur chargement des défis: " + e.getMessage());
        }
        return new DefisRepo();
    }


}
