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


/**
 * Repository JSON pour gérer les joueurs.
 * Permet de sauvegarder, charger et gérer les joueurs en mémoire et sur disque.
 */
public class JSONRepository implements JoueurRepository {

    private final File FICHIER;
    private final HashMap<String, Joueur> joueurs;
    private final Gson gson;


    /**
     * Construit un repository JSON avec un fichier spécifique.
     *
     * @param filePath le chemin du fichier JSON de sauvegarde
     */
    public JSONRepository(String filePath) {
        this.FICHIER = new File(filePath);
        this.joueurs = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        assurerFichierSauvegarde();
    }


    /**
     * Construit un repository JSON avec le fichier de sauvegarde par défaut.
     */
    public JSONRepository() {
        this("saves/sauvegardesJoueurs.json");
    }


    /**
     * Retourne le nombre de joueurs dans le repository.
     *
     * @return le nombre de joueurs
     */
    public int getSize() {
        return this.joueurs.size();
    }


    /**
     * Ajoute un joueur si son pseudo n'existe pas déjà.
     *
     * @param joueur le joueur à ajouter
     */
    public void addJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo())) return;
        this.joueurs.put(joueur.getPseudo(), joueur);
    }


    /**
     * Supprime un joueur du repository.
     *
     * @param joueur le joueur à supprimer
     */
    public void removeJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo()))
            this.joueurs.remove(joueur.getPseudo());
    }


    /**
     * Supprime un joueur en fonction de son pseudo.
     *
     * @param pseudo le pseudo du joueur à supprimer
     */
    public HashMap<String, Joueur> getJoueurs() {
        return joueurs;
    }

    public void removeJoueur(String pseudo) {
        if (joueurs.containsKey(pseudo))
            this.joueurs.remove(pseudo);
    }


    /**
     * Retourne un joueur correspondant au pseudo donné.
     * Si le joueur n'existe pas, il est créé.
     *
     * @param pseudo le pseudo du joueur
     * @return le joueur existant ou nouvellement créé
     * @throws PseudoException si le pseudo est invalide
     */
    public Joueur getJoueurParPseudo(String pseudo) throws PseudoException {
        Joueur existingJoueur = joueurs.get(pseudo);
        if (existingJoueur != null) return existingJoueur;
        Joueur newJoueur = new Joueur(pseudo);
        joueurs.put(pseudo, newJoueur);
        return newJoueur;
    }


    /**
     * Vérifie que le fichier de sauvegarde existe et crée les dossiers/fichier si nécessaire.
     * Si le fichier ou le dossier ne peut pas être créé, une RuntimeException est levée.
     */
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


    /**
     * Sauvegarde tous les joueurs dans le fichier JSON.
     */
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


    /**
     * Charge tous les joueurs depuis le fichier JSON.
     */
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


    /**
     * Retourne une représentation textuelle du repository et des joueurs qu'il contient.
     *
     * @return une chaîne décrivant les joueurs sauvegardés
     */
    @Override
    public String toString() {
        return "Sauvegarde{" +
                "joueurs=" + joueurs +
                '}';
    }


    /**
     * Compare ce repository à un autre objet pour vérifier l'égalité.
     * Deux repositories sont égaux si leurs collections de joueurs sont identiques.
     *
     * @param o l'objet à comparer
     * @return true si les repositories sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JSONRepository that = (JSONRepository) o;
        return Objects.equals(joueurs, that.joueurs);
    }


    /**
     * Retourne le code de hachage du repository basé sur sa collection de joueurs.
     *
     * @return le code de hachage
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(joueurs);
    }


    /**
     * Retourne tous les joueurs sous forme de liste.
     *
     * @return la liste de tous les joueurs
     */
    @Override
    public List<Joueur> getAllJoueurs() {
        return new ArrayList<>(joueurs.values());
    }
}
