package modele.Cellules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheminTest {

    @Test
    void testEstChemin() {
        Chemin chemin = new Chemin(12, 30);
        assertTrue(chemin.estChemin());
        assertFalse(chemin.estMur());
        assertFalse(chemin.estEntree());
        assertFalse(chemin.estSortie());
    }

}