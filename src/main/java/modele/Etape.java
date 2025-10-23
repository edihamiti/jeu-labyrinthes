package modele;

/**
 * Classe représentant une étape d'un défi.
 */
public class Etape {
    private Defi defi;
    private int numero;

    /**
     * Constructeur pour une étape.
     *
     * @param defi   le défi associé à cette étape
     * @param numero le numéro de l'étape
     */
    public Etape(Defi defi, int numero) {
        this.defi = defi;
        this.numero = numero;
    }

    public Defi getDefi() {
        return defi;
    }

    public int getNumero() {
        return numero;
    }
}
