package modele;

public class Etape {
    private Defi defi;
    private int numero;

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
