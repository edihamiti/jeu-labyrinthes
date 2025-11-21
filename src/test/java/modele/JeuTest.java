package modele;

import modele.boutique.GestionnaireBoutique;
import modele.defi.Defi;
import modele.defi.repository.DefiJson;
import modele.defi.repository.DefisRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JeuTest {
    private Jeu jeu;
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private DefisRepo defisRepo;
    private Defi facile1;
    private Defi moyen1;

    @BeforeEach
    void setUp() throws PseudoException {
        joueur = new Joueur("TestJoueur");
        labyrinthe = new Labyrinthe(5, 5, 25.0);
        GestionnaireBoutique boutique = new GestionnaireBoutique();

        DefiJson defiJson = new DefiJson();
        defisRepo = defiJson.charger();
        List<Defi> defis = defisRepo.getDefisRepo();

        facile1 = defis.stream()
                .filter(d -> "FACILE1".equals(d.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("FACILE1 non trouvé"));

        moyen1 = defis.stream()
                .filter(d -> "MOYEN1".equals(d.name()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("MOYEN1 non trouvé"));

        jeu = new Jeu(ModeJeu.MODE_LIBRE, joueur, labyrinthe, facile1, boutique);
    }

    @Test
    void testConstructeur() {
        assertEquals(ModeJeu.MODE_LIBRE, jeu.getModeJeu());
        assertEquals(joueur, jeu.getJoueur());
        assertEquals(labyrinthe, jeu.getLabyrinthe());
        assertEquals(facile1, jeu.getDefiEnCours());
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
        jeu.startTimer();
        String message = jeu.terminerPartie(true);
        assertFalse(labyrinthe.isJeuEnCours());
        assertTrue(message.contains("Vous avez trouvé la sortie"));
    }

    @Test
    void testTerminerPartieDefaite() {
        labyrinthe.setJeuEnCours(true);
        jeu.startTimer();
        String message = jeu.terminerPartie(false);
        assertFalse(labyrinthe.isJeuEnCours());
        assertTrue(message.contains("Partie terminée"));
    }

    @Test
    void testSetLabyrintheDefi() {
        jeu.setLabyrinthe(moyen1);
        Labyrinthe nouveauLabyrinthe = jeu.getLabyrinthe();
        assertEquals(moyen1.largeur(), nouveauLabyrinthe.getLargeur());
        assertEquals(moyen1.hauteur(), nouveauLabyrinthe.getHauteur());
    }

    @Test
    void testSetLabyrintheDimensions() {
        jeu.setLabyrinthe(10, 10, 30.0);
        Labyrinthe nouveauLabyrinthe = jeu.getLabyrinthe();
        assertEquals(10, nouveauLabyrinthe.getLargeur());
        assertEquals(10, nouveauLabyrinthe.getHauteur());
    }
}
