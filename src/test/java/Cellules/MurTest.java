package Cellules;

import main.Cellules.Mur;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MurTest {

    @Test
    void testEstMur() {
        Mur mur = new Mur(12, 30);
        assertTrue(mur.estMur());
        assertFalse(mur.estChemin());
        assertFalse(mur.estEntree());
        assertFalse(mur.estSortie());
    }

}