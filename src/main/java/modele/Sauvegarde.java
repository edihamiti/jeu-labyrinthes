package modele;

import org.json.JSONArray;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

/**
 * Classe gérant la sauvegarde et le chargement des joueurs.
 */
public class Sauvegarde {

    private final File FICHIER;
    private final HashMap<String, Joueur> joueurs;

    /**
     * Constructeur de la classe Sauvegarde.
     *
     * @param filePath le chemin du fichier de sauvegarde
     */
    public Sauvegarde(String filePath) {
        this.FICHIER = new File(filePath);
        joueurs = new HashMap<>();
        assurerFichierSauvegarde();
    }

    /**
     * Constructeur par défaut de la classe Sauvegarde.
     * Utilise le chemin de fichier par défaut "saves/sauvegardesJoueurs.json".
     */
    public Sauvegarde() {
        this("saves/sauvegardesJoueurs.json");
    }

    public int getSize() {
        return this.joueurs.size();
    }

    /**
     * Ajoute un joueur à la sauvegarde.
     *
     * @param j le joueur à ajouter
     */
    public void addJoueur(Joueur j) {
        if (joueurs.containsKey(j.getPseudo())) return;
        this.joueurs.put(j.getPseudo(), j);
    }

    /**
     * Supprime un joueur de la sauvegarde.
     *
     * @param joueur le joueur à supprimer
     */
    public void removeJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo()))
            this.joueurs.remove(joueur.getPseudo());
    }

    public HashMap<String, Joueur> getJoueurs() {
        return joueurs;
    }

    /**
     * Supprime un joueur de la sauvegarde par son pseudo.
     *
     * @param pseudo le pseudo du joueur à supprimer
     */
    public void removeJoueur(String pseudo) {
        if (joueurs.containsKey(pseudo))
            this.joueurs.remove(pseudo);
    }

    /**
     * Récupère un joueur par son pseudo.
     * Si le joueur n'existe pas dans la sauvegarde, un nouveau joueur est créé et ajouté.
     *
     * @param pseudo le pseudo du joueur à récupérer
     * @return le joueur correspondant au pseudo
     * @throws RuntimeException si le pseudo est invalide lors de la création d'un nouveau joueur
     */
    public Joueur getJoueurParPseudo(String pseudo) throws PseudoException {
        Joueur existingJoueur = joueurs.get(pseudo);
        if (existingJoueur != null) return existingJoueur;
        return new Joueur(pseudo);
    }

    /**
     * Assure que le fichier de sauvegarde existe, sinon le crée.
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
     * Sauvegarde les joueurs dans le fichier de sauvegarde.
     */
    public void sauvegardeJoueurs() {
        try (FileWriter fileWriter = new FileWriter(FICHIER)) {

            JSONArray jsonJoueurs = new JSONArray();

            for (Joueur jouer : this.joueurs.values()) {
                jsonJoueurs.put(jouer.toJson());
            }

            fileWriter.write(jsonJoueurs.toString(4));
            System.out.println("Sauvegarde réussie");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Charge les joueurs depuis le fichier de sauvegarde.
     */
    public void chargerJoueurs() {
        try (BufferedReader bf = new BufferedReader(new FileReader(FICHIER))) {

            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = bf.readLine()) != null) contenu.append(ligne);

            if (contenu.isEmpty()) return;

            JSONArray jsonJoueurs = new JSONArray(contenu.toString());
            for (int i = 0; i < jsonJoueurs.length(); i++) this.addJoueur(new Joueur(jsonJoueurs.getJSONObject(i)));

            System.out.println("Joueurs chargé");

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
        Sauvegarde that = (Sauvegarde) o;
        return Objects.equals(joueurs, that.joueurs);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(joueurs);
    }
}
