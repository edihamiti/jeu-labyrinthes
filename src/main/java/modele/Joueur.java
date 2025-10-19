package modele;

import org.json.JSONObject;

import java.util.HashMap;

public class Joueur {

    private static int count = 1;
    private final int id;
    private String pseudo;
    private HashMap<Defi, Boolean> progression;

    public Joueur(String pseudo) throws PseudoException {
        this.id = count++;
        if (pseudo == null || pseudo.trim().isEmpty())
            throw new PseudoException("Le pseudo ne peut pas être vide ou compose que d'espace");
        else if (pseudo.length() > 15) throw new PseudoException("Le pseudo est trop long");
        else if (!pseudo.matches("[a-zA-Z0-9]+"))
            throw new PseudoException("Le pseudo ne peut contenir que des lettres et des chiffres");
        else this.pseudo = pseudo;

        this.progression = new HashMap<>();
        for (Defi defi : Defi.values()) {
            this.progression.put(defi, false);
        }
    }

    public Joueur(JSONObject joueur){
        this.id = joueur.getInt("id");
        this.pseudo = joueur.getString("pseudo");
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

    public JSONObject toJson(){
        JSONObject joueur = new JSONObject();
        joueur.put("id", this.id);
        joueur.put("pseudo", this.pseudo);
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
}
