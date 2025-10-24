package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ModeJeuTest {
    @Test
    void testModeJeuValues() {
        ModeJeu[] modes = ModeJeu.values();
        assertEquals(2, modes.length);
        assertTrue(contains(modes, ModeJeu.MODE_PROGRESSION));
        assertTrue(contains(modes, ModeJeu.MODE_LIBRE));
    }

    @Test
    void testModeJeuValueOf() {
        assertEquals(ModeJeu.MODE_PROGRESSION, ModeJeu.valueOf("MODE_PROGRESSION"));
        assertEquals(ModeJeu.MODE_LIBRE, ModeJeu.valueOf("MODE_LIBRE"));
    }

    private boolean contains(ModeJeu[] modes, ModeJeu mode) {
        for (ModeJeu m : modes) {
            if (m == mode) {
                return true;
            }
        }
        return false;
    }
}