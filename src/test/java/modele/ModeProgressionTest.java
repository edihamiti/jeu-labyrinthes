package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

class ModeProgressionTest {
    private ModeProgression modeProgression;

    @BeforeEach
    void setUp() {
        modeProgression = new ModeProgression();
    }

    /**@Test
    void testModeProgressionConstructeur() {
        List<Etape> etapes = modeProgression.getEtapes();
        assertNotNull(etapes);
        // 9 étapes initiales + 9 étapes ajoutées = 18 étapes
        assertEquals(18, etapes.size());
    }**/

    @Test
    void testGetEtapeExistante() {
        // Test pour chaque numéro d'étape de 1 à 9
        for (int i = 1; i <= 9; i++) {
            Etape etape = modeProgression.getEtape(i);
            assertNotNull(etape);
            assertEquals(i, etape.getNumero());
        }
    }

    @Test
    void testGetEtapeInexistante() {
        Etape etape = modeProgression.getEtape(10); // Numéro d'étape inexistant
        assertNull(etape);
    }

    /**@Test
    void testOrdreEtapes() {
        List<Etape> etapes = modeProgression.getEtapes();
        // Vérifie que les étapes sont dans le bon ordre
        assertEquals(Defi.FACILE1, etapes.get(9).getDefi()); // Position 9 car il y a 9 étapes initiales
        assertEquals(Defi.FACILE2, etapes.get(10).getDefi());
        assertEquals(Defi.FACILE3, etapes.get(11).getDefi());
        assertEquals(Defi.MOYEN1, etapes.get(12).getDefi());
        assertEquals(Defi.MOYEN2, etapes.get(13).getDefi());
        assertEquals(Defi.MOYEN3, etapes.get(14).getDefi());
        assertEquals(Defi.DIFFICILE1, etapes.get(15).getDefi());
        assertEquals(Defi.DIFFICILE2, etapes.get(16).getDefi());
        assertEquals(Defi.DIFFICILE3, etapes.get(17).getDefi());
    }**/
}