package modele.joueursRepositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import modele.Joueur;
import modele.PseudoException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class JSONRepository implements JoueurRepository {

    private final File FICHIER;
    private final HashMap<String, Joueur> joueurs;
    private final Gson gson;

    public JSONRepository(String filePath) {
        this.FICHIER = new File(filePath);
        this.joueurs = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        assurerFichierSauvegarde();
    }

    public JSONRepository() {
        this("saves/sauvegardesJoueurs.json");
    }

    public int getSize() {
        return this.joueurs.size();
    }

    public void addJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo())) return;
        this.joueurs.put(joueur.getPseudo(), joueur);
    }

    public void removeJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo()))
            this.joueurs.remove(joueur.getPseudo());
    }

    public HashMap<String, Joueur> getJoueurs() {
        return joueurs;
    }

    public void removeJoueur(String pseudo) {
        if (joueurs.containsKey(pseudo))
            this.joueurs.remove(pseudo);
    }

    public Joueur getJoueurParPseudo(String pseudo) throws PseudoException {
        Joueur existingJoueur = joueurs.get(pseudo);
        if (existingJoueur != null) return existingJoueur;
        Joueur newJoueur = new Joueur(pseudo);
        joueurs.put(pseudo, newJoueur);
        return newJoueur;
    }

    private void assurerFichierSauvegarde() {
        File parent = FICHIER.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs() && !parent.exists()) {
                throw new RuntimeException("Impossible de créer le dossier de sauvegarde: " + parent);
            }
        }
        if (!FICHIER.exists()) {
            try (FileWriter fw = new FileWriter(FICHIER)) {
                fw.write("[]");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sauvegarder() {
        try (FileWriter fileWriter = new FileWriter(FICHIER)) {
            List<Joueur> listeJoueurs = new ArrayList<>(this.joueurs.values());

            for (Joueur joueur : listeJoueurs) {
                joueur.prepareForSerialization();
            }

            gson.toJson(listeJoueurs, fileWriter);
            System.out.println("Sauvegarde réussie");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void chargerJoueurs() {
        try (BufferedReader bf = new BufferedReader(new FileReader(FICHIER))) {
            Type listType = new TypeToken<List<Joueur>>() {
            }.getType();
            List<Joueur> listeJoueurs = gson.fromJson(bf, listType);

            if (listeJoueurs != null) {
                for (Joueur joueur : listeJoueurs) {
                    joueur.restoreAfterDeserialization();
                    this.addJoueur(joueur);
                }
            }

            System.out.println("Joueurs chargés");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Sauvegarde{" +
                "joueurs=" + joueurs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JSONRepository that = (JSONRepository) o;
        return Objects.equals(joueurs, that.joueurs);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(joueurs);
    }
}
