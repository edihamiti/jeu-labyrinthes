package modele;

import com.google.gson.annotations.SerializedName;
import defi.modele.Defi;
import defi.repository.DefiJson;
import modele.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Joueur {

    private String pseudo;
    @SerializedName("progression")
    private Map<String, Boolean> progressionJson;
    private transient HashMap<Defi, Boolean> progression;
    private int score;
    private ArrayList<Item> inventaire;

    public Joueur(String pseudo) throws PseudoException {
        if (pseudo == null || pseudo.trim().isEmpty())
            throw new PseudoException("Le pseudo ne peut pas être vide ou composé que d'espace(s)");
        else if (pseudo.length() > 15) throw new PseudoException("Le pseudo est trop long");
        else if (!pseudo.matches("[a-zA-Z0-9]+"))
            throw new PseudoException("Le pseudo ne peut contenir que des lettres et des chiffres");
        else this.pseudo = pseudo;
        this.progression = new DefiJson().charger().getMapsDefis();
        this.score = 0;
        this.inventaire = new ArrayList<>();
    }

    /**
     * Prépare la sérialisation en convertissant la progression
     */
    public void prepareForSerialization() {
        this.progressionJson = new HashMap<>();
        for (Map.Entry<Defi, Boolean> entry : progression.entrySet()) {
            progressionJson.put(entry.getKey().name(), entry.getValue());
        }
    }

    /**
     * Restaure la progression après désérialisation
     */
    public void restoreAfterDeserialization() {
        this.progression = new HashMap<>();
        for (Defi defi : new DefiJson().charger().getDefisRepo()) {
            Boolean completed = progressionJson.getOrDefault(defi.name(), false);
            if (completed == null) {
                System.err.println("Pas de sauvegarde pour " + defi.name() + " dans la sauvegarde de " + this.pseudo);
                completed = false;
                System.out.println("Ajout de " + defi.name() + " dans la sauvegarde du joueur");
            }
            progression.put(defi, completed);
        }
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public HashMap<Defi, Boolean> getProgression() {
        return progression;
    }

    public void setProgression(HashMap<Defi, Boolean> progression) {
        this.progression = progression;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ArrayList<Item> getInventaire() {
        return inventaire;
    }

    public void ajouterScore(Defi defi) {
        ajouterScore(defi.points(), defi);
    }

    public void ajouterScore(int points, Defi defi) {
        if (defi != null) {
            this.score += points;
            this.progression.put(defi, true);
        }
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "pseudo='" + pseudo + '\'' +
                ", score=" + score +
                ", progression=" + progression +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return Objects.equals(pseudo, joueur.pseudo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo);
    }
}
