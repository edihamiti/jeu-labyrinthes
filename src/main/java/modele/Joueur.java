package modele;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Classe représentant un joueur.
 */
public class Joueur {

    private static int count = 1;
    private final int id;
    private String pseudo;
    private HashMap<Defi, Boolean> progression;
    private int score;

    /**
     * Constructeur pour un joueur.
     *
     * @param pseudo le pseudo du joueur
     * @throws PseudoException si le pseudo est invalide
     */
    public Joueur(String pseudo) throws PseudoException {
        this.id = count++;
        if (pseudo == null || pseudo.trim().isEmpty())
            throw new PseudoException("Le pseudo ne peut pas être vide ou composé que d'espace(s)");
        else if (pseudo.length() > 15) throw new PseudoException("Le pseudo est trop long");
        else if (!pseudo.matches("[a-zA-Z0-9]+"))
            throw new PseudoException("Le pseudo ne peut contenir que des lettres et des chiffres");
        else this.pseudo = pseudo;

        this.progression = new HashMap<>();
        for (Defi defi : Defi.values()) {
            this.progression.put(defi, false);
        }
        this.score = 0;
    }

    /**
     * Constructeur pour un joueur à partir d'un objet JSON.
     *
     * @param joueur un objet JSON représentant un joueur
     */
    public Joueur(JSONObject joueur) {
        this.id = joueur.getInt("id");
        this.pseudo = joueur.getString("pseudo");
        this.score = joueur.getInt("score");
        this.progression = new HashMap<>();

        JSONObject progressionJson = joueur.getJSONObject("progression");
        for (Defi defi : Defi.values())
            progression.put(defi, (Boolean) progressionJson.get(defi.name()));
    }

    public int getId() {
        return id;
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

    /**
     * Convertit le joueur en objet JSON.
     *
     * @return un objet JSON représentant le joueur
     */
    public JSONObject toJson() {
        JSONObject joueur = new JSONObject();
        joueur.put("id", this.id);
        joueur.put("pseudo", this.pseudo);
        joueur.put("score", this.score);
        joueur.put("progression", this.progression);
        return joueur;
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", progression=" + progression +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Joueur joueur = (Joueur) o;
        return Objects.equals(pseudo, joueur.pseudo) && Objects.equals(progression, joueur.progression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo, progression);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Ajoute le score d'un défi au score total du joueur et met à jour la progression.
     *
     * @param defi le défi complété
     */
    public void ajouterScore(Defi defi) {
        if (defi != null) {
            this.score += defi.getPoints();
            this.progression.put(defi, true);
        }
    }
}
