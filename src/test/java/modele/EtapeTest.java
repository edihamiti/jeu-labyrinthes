package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class EtapeTest {
    @Test
    void testEtapeConstructeur() {
        Defi defi = Defi.FACILE1;
        int numero = 1;
        Etape etape = new Etape(defi, numero);
        
        assertEquals(defi, etape.getDefi());
        assertEquals(numero, etape.getNumero());
    }

    @Test
    void testEtapeGetDefi() {
        Defi defi = Defi.MOYEN2;
        Etape etape = new Etape(defi, 2);
        assertEquals(defi, etape.getDefi());
    }

    @Test
    void testEtapeGetNumero() {
        int numero = 3;
        Etape etape = new Etape(Defi.DIFFICILE3, numero);
        assertEquals(numero, etape.getNumero());
    }

    @Test
    void testEtapeDefiCorrespondance() {
        for (Defi defi : Defi.values()) {
            Etape etape = new Etape(defi, defi.getEtape());
            assertEquals(defi.getEtape(), etape.getNumero());
        }
    }
}