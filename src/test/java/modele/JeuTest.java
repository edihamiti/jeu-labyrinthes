package modele;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

class JeuTest {
    private Jeu jeu;
    private Joueur joueur;
    private Labyrinthe labyrinthe;

    @BeforeEach
    void setUp() throws PseudoException {
        joueur = new Joueur("TestJoueur");
        labyrinthe = new Labyrinthe(5, 5, 25.0);
        jeu = new Jeu(ModeJeu.MODE_LIBRE, joueur, labyrinthe, Defi.FACILE1);
    }

    @Test
    void testConstructeur() {
        assertEquals(ModeJeu.MODE_LIBRE, jeu.getModeJeu());
        assertEquals(joueur, jeu.getJoueur());
        assertEquals(labyrinthe, jeu.getLabyrinthe());
        assertEquals(Defi.FACILE1, jeu.getDefiEnCours());
    }

    @Test
    void testSingleton() {
        Jeu instance1 = Jeu.getInstance();
        Jeu instance2 = Jeu.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testSetInstance() {
        Jeu newInstance = new Jeu();
        Jeu.setInstance(newInstance);
        assertSame(newInstance, Jeu.getInstance());
    }

    @Test
    void testVision() {
        assertEquals(Vision.VUE_LIBRE, jeu.getVision());
        jeu.setVision(Vision.VUE_LOCAL);
        assertEquals(Vision.VUE_LOCAL, jeu.getVision());
    }

    @Test
    void testModeJeu() {
        jeu.setModeJeu(ModeJeu.MODE_PROGRESSION);
        assertEquals(ModeJeu.MODE_PROGRESSION, jeu.getModeJeu());
    }

    @Test
    void testDeplacerJoueurInvalide() {
        labyrinthe.setJeuEnCours(false);
        boolean resultat = jeu.deplacerJoueur(1, 0);
        assertFalse(resultat);
    }

    @Test
    void testEstVictoireNon() {
        labyrinthe.setJeuEnCours(true);
        assertFalse(jeu.estVictoire());
    }

    @Test
    void testTerminerPartieVictoire() {
        labyrinthe.setJeuEnCours(true);
        String message = jeu.terminerPartie(true);
        assertFalse(labyrinthe.isJeuEnCours());
        assertTrue(message.contains("Vous avez trouvé la sortie"));
    }

    @Test
    void testTerminerPartieDefaite() {
        labyrinthe.setJeuEnCours(true);
        String message = jeu.terminerPartie(false);
        assertFalse(labyrinthe.isJeuEnCours());
        assertTrue(message.contains("Partie terminée"));
    }

    @Test
    void testSetLabyrintheDefi() {
        jeu.setLabyrinthe(Defi.MOYEN1);
        Labyrinthe nouveauLabyrinthe = jeu.getLabyrinthe();
        assertEquals(Defi.MOYEN1.getLargeur(), nouveauLabyrinthe.getLargeur());
        assertEquals(Defi.MOYEN1.getHauteur(), nouveauLabyrinthe.getHauteur());
    }

    @Test
    void testSetLabyrintheDimensions() {
        jeu.setLabyrinthe(10, 10, 30.0);
        Labyrinthe nouveauLabyrinthe = jeu.getLabyrinthe();
        assertEquals(10, nouveauLabyrinthe.getLargeur());
        assertEquals(10, nouveauLabyrinthe.getHauteur());
    }
}