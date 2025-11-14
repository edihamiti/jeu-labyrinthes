package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VisionTest {
    @Test
    void testVisionValues() {
        Vision[] visions = Vision.values();
        assertEquals(2, visions.length);
        assertTrue(contains(visions, Vision.VUE_LIBRE));
        assertTrue(contains(visions, Vision.VUE_LOCAL));
    }

    /**@Test
    void testVisionValueOf() {
        assertEquals(Vision.VUE_LIBRE, Vision.valueOf("VUE_LIBRE"));
        assertEquals(Vision.VUE_LOCAL, Vision.valueOf("VUE_LOCAL"));
    }**/

    private boolean contains(Vision[] visions, Vision vision) {
        for (Vision v : visions) {
            if (v == vision) {
                return true;
            }
        }
        return false;
    }
}