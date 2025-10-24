package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PseudoExceptionTest {
    @Test
    void testPseudoExceptionMessage() {
        String message = "Pseudo invalide";
        PseudoException exception = new PseudoException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void testPseudoExceptionInheritance() {
        PseudoException exception = new PseudoException("Test");
        assertTrue(exception instanceof Exception);
    }
}