package modele;

import modele.joueursRepositories.JSONRepository;
import modele.joueursRepositories.JoueurRepository;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JSONRepositoryTest {

    private JSONRepository joueurs;
    private Joueur joueur1;
    private Joueur joueur2;
    private Joueur joueur3;
    private Joueur joueur4;

    @BeforeAll
    void init() throws PseudoException {
        joueurs = new JSONRepository("saves/test/sauvegardesJoueurs.json");
        joueur1 = new Joueur("Enzo");
        joueur2 = new Joueur("Amaury");
        joueur3 = new Joueur("Edi");
        joueur4 = new Joueur("Bastien");
    }

    @Test
    @Order(1)
    void addJoueur() {
        assertEquals(0, joueurs.getSize());
        joueurs.addJoueur(joueur1);
        joueurs.addJoueur(joueur2);
        joueurs.addJoueur(joueur3);
        joueurs.addJoueur(joueur4);
        assertEquals(4, joueurs.getSize());
    }

    @Test
    @Order(2)
    void removeJoueur() {
        joueurs.removeJoueur(joueur1);
        assertEquals(3, joueurs.getSize());
        joueurs.removeJoueur("MauvaisPseudo");
        assertEquals(3, joueurs.getSize());
    }

    @Test
    @Order(3)
    void sauvegardeEtChargerJoueurs() {
        joueurs.sauvegarder();
        JoueurRepository joueursCharger = new JSONRepository("saves/test/sauvegardesJoueurs.json");
        joueursCharger.chargerJoueurs();
        assertEquals(joueurs, joueursCharger);
        joueurs.removeJoueur(joueur4);
        joueursCharger.removeJoueur(joueur4);
        assertEquals(joueurs, joueursCharger);
        joueursCharger.removeJoueur(joueur2);
        assertNotEquals(joueurs, joueursCharger);
    }

}