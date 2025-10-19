package Cellules;

import modele.Cellules.Sortie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SortieTest {

    @Test
    void testEstSortie() {
        Sortie sortie = new Sortie(12, 30);
        assertTrue(sortie.estSortie());
        assertFalse(sortie.estMur());
        assertFalse(sortie.estEntree());
        assertFalse(sortie.estChemin());
    }

}