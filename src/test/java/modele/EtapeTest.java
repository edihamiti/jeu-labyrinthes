package modele;

import modele.defi.Defi;
import modele.defi.repository.DefiJson;
import modele.defi.repository.DefisRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EtapeTest {
    private DefisRepo defisRepo;
    private List<Defi> defis;
    private Defi facile1;
    private Defi moyen2;
    private Defi difficile3;

    @BeforeEach
    void setUp() {
        DefiJson defiJson = new DefiJson();
        defisRepo = defiJson.charger();
        defis = defisRepo.getDefisRepo();

        facile1 = defis.stream()
                .filter(d -> "FACILE1".equals(d.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("FACILE1 non trouvé"));

        moyen2 = defis.stream()
                .filter(d -> "MOYEN2".equals(d.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MOYEN2 non trouvé"));

        difficile3 = defis.stream()
                .filter(d -> "DIFFICILE3".equals(d.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("DIFFICILE3 non trouvé"));
    }

    @Test
    void testEtapeConstructeur() {
        int numero = 1;
        Etape etape = new Etape(facile1, numero);

        assertEquals(facile1, etape.getDefi());
        assertEquals(numero, etape.getNumero());
    }

    @Test
    void testEtapeGetDefi() {
        Etape etape = new Etape(moyen2, 2);
        assertEquals(moyen2, etape.getDefi());
    }

    @Test
    void testEtapeGetNumero() {
        int numero = 3;
        Etape etape = new Etape(difficile3, numero);
        assertEquals(numero, etape.getNumero());
    }

    @Test
    void testEtapeDefiCorrespondance() {
        for (Defi defi : defis) {
            Etape etape = new Etape(defi, defi.etape());
            assertEquals(defi.etape(), etape.getNumero());
        }
    }
}
