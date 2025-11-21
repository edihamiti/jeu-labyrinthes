import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vue.Router;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Router.
 */
class RouterTest {
    @BeforeEach
    void setUp() {
        Router.clearHistory();
        Router.addToHistory("test", null, false);
    }

    @Test
    void testClearHistory() {
        Router.clearHistory();
        assertEquals(0, Router.getHistorySize(), "L'historique devrait être vide après clearHistory()");
    }

    @Test
    void testCanGoBackWhenEmpty() {
        Router.clearHistory();
        assertFalse(Router.canGoBack(), "canGoBack() devrait retourner false quand l'historique est vide");
    }

    @Test
    void testHistorySize() {
        assertEquals(1, Router.getHistorySize());
    }
}

