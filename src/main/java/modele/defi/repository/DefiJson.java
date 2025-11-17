package modele.defi.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import modele.defi.Defi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefiJson {

    private final String repertoire;
    private final Gson gson;

    public DefiJson(String repertoire) {
        this.repertoire = repertoire;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public DefiJson() {
        this("defi/");
    }

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

    public DefisRepo charger() {
        try {
            Path fichier = Paths.get(repertoire + "defis.json");
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
