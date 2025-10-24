package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class DefiTest {
    @Test
    void testDefiValues() {
        Defi[] defis = Defi.values();
        assertEquals(9, defis.length);
    }

    @Test
    void testDefiFacile1() {
        Defi defi = Defi.FACILE1;
        assertEquals(6, defi.getLargeur());
        assertEquals(6, defi.getHauteur());
        assertEquals(25.0, defi.getPourcentageMurs());
        assertEquals(5, defi.getPoints());
        assertEquals(1, defi.getEtape());
    }

    @Test
    void testDefiMoyen2() {
        Defi defi = Defi.MOYEN2;
        assertEquals(12, defi.getLargeur());
        assertEquals(12, defi.getHauteur());
        assertEquals(50.0, defi.getPourcentageMurs());
        assertEquals(20, defi.getPoints());
        assertEquals(2, defi.getEtape());
    }

    @Test
    void testDefiDifficile3() {
        Defi defi = Defi.DIFFICILE3;
        assertEquals(18, defi.getLargeur());
        assertEquals(18, defi.getHauteur());
        assertEquals(75.0, defi.getPourcentageMurs());
        assertEquals(55, defi.getPoints());
        assertEquals(3, defi.getEtape());
    }

    @Test
    void testProgression() {
        // Test que les défis sont bien ordonnés par étape
        for (Defi defi : Defi.values()) {
            int etape = defi.getEtape();
            assertTrue(etape >= 1 && etape <= 3, "L'étape doit être entre 1 et 3");
        }
    }

    @Test
    void testPointsProgression() {
        // Test que les points augmentent avec la difficulté dans chaque étape
        for (int etape = 1; etape <= 3; etape++) {
            int pointsFacile = getDefiParEtapeEtDifficulte(etape, "FACILE").getPoints();
            int pointsMoyen = getDefiParEtapeEtDifficulte(etape, "MOYEN").getPoints();
            int pointsDifficile = getDefiParEtapeEtDifficulte(etape, "DIFFICILE").getPoints();
            
            assertTrue(pointsFacile < pointsMoyen);
            assertTrue(pointsMoyen < pointsDifficile);
        }
    }

    private Defi getDefiParEtapeEtDifficulte(int etape, String difficulte) {
        return Defi.valueOf(difficulte + etape);
    }
}