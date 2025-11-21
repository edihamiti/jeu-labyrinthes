package boutique.modele;

import modele.boutique.InventaireJoueur;
import modele.boutique.TypeCosmetique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class InventaireJoueurTest {

    InventaireJoueur invj;

    @BeforeEach
    void setUp() {
        invj = new InventaireJoueur(1000);
    }

    @Test
    void getScore_setScore() {
        assertEquals(1000, invj.getScore());
        invj.setScore(invj.getScore() - 200);
        assertEquals(800, invj.getScore());
    }

    @Test
    void ajouterCosmetique() {
        assertEquals(0, invj.getCosmetiquesPossedes().size());
        invj.ajouterCosmetique("mur_desert");
        assertEquals(1, invj.getCosmetiquesPossedes().size());
        invj.ajouterCosmetique("mur_desert");
        invj.ajouterCosmetique("mur_mer");
        assertEquals(2, invj.getCosmetiquesPossedes().size());
    }

    @Test
    void possedeCosmetique() {
        assertFalse(invj.possedeCosmetique("existePas"));
        invj.ajouterCosmetique("mur_desert");
        assertTrue(invj.possedeCosmetique("mur_desert"));
        invj.ajouterCosmetique("mur_mer");
        assertTrue(invj.possedeCosmetique("mur_desert"));
        assertTrue(invj.possedeCosmetique("mur_mer"));
    }

    @Test
    void getCosmetiquesPossedes() {
        assertEquals(new HashSet<>(), invj.getCosmetiquesPossedes());
        invj.ajouterCosmetique("mur_desert");
        invj.ajouterCosmetique("mur_mer");
        assertEquals(new HashSet<>(Arrays.asList("mur_desert", "mur_mer")), invj.getCosmetiquesPossedes());
    }

    @Test
    void equiperCosmetique_et_getCosmetiqueEquipe() {
        assertNull(invj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_JOUEUR));
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_JOUEUR, "joueur_1");
        assertEquals("joueur_1", invj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_JOUEUR));
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_JOUEUR, "joueur_2");
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_MUR, "mur_2");
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_SORTIE, "sortie_2");
        assertEquals("joueur_2", invj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_JOUEUR));
        assertEquals("mur_2", invj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_MUR));
        assertEquals("sortie_2", invj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_SORTIE));
    }

    @Test
    void getCosmetiquesEquipes() {
        assertEquals(new HashMap<>(), invj.getCosmetiquesEquipes());
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_JOUEUR, "joueur_2");
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_MUR, "mur_2");
        invj.equiperCosmetique(TypeCosmetique.TEXTURE_SORTIE, "sortie_2");
        HashMap<TypeCosmetique, String> attendu = new HashMap<>();
        attendu.put(TypeCosmetique.TEXTURE_JOUEUR, "joueur_2");
        attendu.put(TypeCosmetique.TEXTURE_MUR, "mur_2");
        attendu.put(TypeCosmetique.TEXTURE_SORTIE, "sortie_2");

        invj.equiperCosmetique(TypeCosmetique.TEXTURE_JOUEUR, "nouveau_joueur");
        attendu.put(TypeCosmetique.TEXTURE_JOUEUR, "nouveau_joueur");
        assertEquals(attendu, invj.getCosmetiquesEquipes());
    }
}