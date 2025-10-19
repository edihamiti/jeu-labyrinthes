package modele;

import org.json.JSONArray;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class Sauvegarde {

    private final File FICHIER;
    private final HashMap<String, Joueur> joueurs;

    public Sauvegarde(String filePath) {
        this.FICHIER = new File(filePath);
        joueurs = new HashMap<>();
        assurerFichierSauvegarde();
    }

    public Sauvegarde() {
        this("saves/sauvegardesJoueurs.json");
    }

    public int getSize() {
        return this.joueurs.size();
    }

    public void addJoueur(Joueur j) {
        if (joueurs.containsKey(j.getPseudo())) return;
        this.joueurs.put(j.getPseudo(), j);
    }

    public void removeJoueur(Joueur joueur) {
        if (joueurs.containsKey(joueur.getPseudo()))
            this.joueurs.remove(joueur.getPseudo());
    }

    public void removeJoueur(String pseudo) {
        if (joueurs.containsKey(pseudo))
            this.joueurs.remove(pseudo);
    }

    public Joueur getJoueurParPseudo(String pseudo) {
        return this.joueurs.get(pseudo);
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

    public void chargerJoueurs() {
        try (BufferedReader bf = new BufferedReader(new FileReader(FICHIER))) {

            StringBuilder contenu = new StringBuilder();
            String ligne;
            while ((ligne = bf.readLine()) != null) contenu.append(ligne);

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
