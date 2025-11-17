package modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class ModeProgressionTest {
    private ModeProgression modeProgression;

    @BeforeEach
    void setUp() {
        modeProgression = new ModeProgression();
    }

    @Test
    void testGetEtapeInexistante() {
        Etape etape = modeProgression.getEtape(10); // Numéro d'étape inexistant
        assertNull(etape);
    }

}