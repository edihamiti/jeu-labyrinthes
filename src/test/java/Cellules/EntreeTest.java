package test.java.Cellules;

import main.java.Cellules.Entree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntreeTest {

    @Test
    void testEstEntree() {
        Entree entree = new Entree(12, 30);
        assertTrue(entree.estEntree());
        assertFalse(entree.estMur());
        assertFalse(entree.estChemin());
        assertFalse(entree.estSortie());
    }

}