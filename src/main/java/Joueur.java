package main.java;

public class Joueur {

    private static int count = 1;
    private final int id;
    private String pseudo;

    public Joueur(String pseudo) throws PseudoException {
        this.id = count++;

        if (pseudo == null || pseudo.trim().isEmpty()) throw new PseudoException("Le pseudo ne peut pas être vide ou compose que d'espace");
        else if (pseudo.length() > 15) throw new PseudoException("Le pseudo est trop long");
        else this.pseudo = pseudo;
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
}
