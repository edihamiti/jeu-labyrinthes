package modele;

import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;

public class Sauvegarde {

    private final File FICHIER = new File("saves/sauvegardesJoueurs.json");
    private final ArrayList<Joueur> joueurs;

    public Sauvegarde() {
        joueurs = new ArrayList<>();
    }

    // Pour tester la class
    public static void main(String[] args) throws PseudoException {

        Joueur j1 = new Joueur("Enzo");
        Joueur j2 = new Joueur("Test");

        Sauvegarde sauvegarde = new Sauvegarde();
        sauvegarde.addJoueur(j1);
        sauvegarde.addJoueur(j2);
        System.out.println(sauvegarde);
        sauvegarde.sauvegardeJoueurs();

        sauvegarde.removeJoueur(j1);
        sauvegarde.removeJoueur(j2);
        System.out.println("Taille tableau : 0="+sauvegarde.joueurs.size());
        sauvegarde.chargerJoueurs();
        System.out.println(sauvegarde);

    }

    public void addJoueur(Joueur j) {
        this.joueurs.add(j);
    }
    public void removeJoueur(Joueur j){
        this.joueurs.remove(j);
    }

    public void sauvegardeJoueurs() {
        try (FileWriter fileWriter = new FileWriter(FICHIER)) {

            JSONArray jsonJoueurs = new JSONArray();

            for (Joueur jouer : this.joueurs) {
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
            for (int i = 0; i < jsonJoueurs.length(); i++) this.joueurs.add(new Joueur(jsonJoueurs.getJSONObject(i)));

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
}
