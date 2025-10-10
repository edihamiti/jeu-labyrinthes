import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JoueurTest {

    @Test
    void pseudoTropLongTest() {
        assertThrows(PseudoException.class, () -> new Joueur("trop de caractères"));
    }

    @Test
    void pseudoVideTest() {
        assertThrows(PseudoException.class, () -> new Joueur(""));
    }

    @Test
    void pseudoEspaceTest(){
        // Pseudo constitué que d'espace
        assertThrows(PseudoException.class, () -> new Joueur("        "));
    }

    @Test
    void pseudoValideTest() throws PseudoException {
        Joueur joueur1 = new Joueur("superPseudo");
        assertEquals("superPseudo", joueur1.getPseudo());
        Joueur joueur2 = new Joueur("Enzo");
        assertEquals("Enzo", joueur2.getPseudo());
    }

}