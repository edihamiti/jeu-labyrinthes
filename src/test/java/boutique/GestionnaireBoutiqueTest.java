package boutique;

import boutique.modele.TypeCosmetique;
import boutique.repository.IDepotInventaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class GestionnaireBoutiqueTest {

    private GestionnaireBoutique gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireBoutique();
    }

    @Test
    void ajouterScore_augmenteLeScore() {
        String idJoueur = "test_joueur_score";
        int pointsInitiaux = obtenirScoreViaReflection(idJoueur);
        int pointsAAjouter = 500;
        gestionnaire.ajouterScore(idJoueur, pointsAAjouter);
        int pointsFinaux = obtenirScoreViaReflection(idJoueur);

        assertEquals(pointsInitiaux + pointsAAjouter, pointsFinaux);
    }

    @Test
    void ajouterScore_plusieursAjoutsSuccessifs() {
        String idJoueur = "test_joueur_multiple";
        int pointsInitiaux = obtenirScoreViaReflection(idJoueur);
        gestionnaire.ajouterScore(idJoueur, 100);
        gestionnaire.ajouterScore(idJoueur, 200);
        gestionnaire.ajouterScore(idJoueur, 300);
        int pointsFinaux = obtenirScoreViaReflection(idJoueur);

        assertEquals(pointsInitiaux + 600, pointsFinaux);
    }

    @Test
    void obtenirTextureEquipee_retourneTextureParDefaut() {
        String idJoueur = "test_joueur_texture";
        String textureMur = gestionnaire.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_MUR);
        String textureJoueur = gestionnaire.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_JOUEUR);
        String textureChemin = gestionnaire.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_CHEMIN);
        String textureSortie = gestionnaire.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_SORTIE);

        assertNotNull(textureMur);
        assertNotNull(textureJoueur);
        assertNotNull(textureChemin);
        assertNotNull(textureSortie);
        assertTrue(textureMur.contains("texture_mur") || textureMur.contains("mur"));
        assertTrue(textureJoueur.contains("texture_joueur") || textureJoueur.contains("joueur"));
        assertTrue(textureChemin.contains("texture_chemin") || textureChemin.contains("chemin"));
        assertTrue(textureSortie.contains("texture_sortie") || textureSortie.contains("sortie"));
    }

    @Test
    void obtenirTextureEquipee_memePourDifferentsJoueurs() {
        String idJoueur1 = "test_joueur1";
        String idJoueur2 = "test_joueur2";
        String texture1 = gestionnaire.obtenirTextureEquipee(idJoueur1, TypeCosmetique.TEXTURE_MUR);
        String texture2 = gestionnaire.obtenirTextureEquipee(idJoueur2, TypeCosmetique.TEXTURE_MUR);

        assertEquals(texture1, texture2);
    }

    @Test
    void ajouterScore_avecZeroPoint() {
        String idJoueur = "test_joueur_zero";
        int pointsInitiaux = obtenirScoreViaReflection(idJoueur);
        gestionnaire.ajouterScore(idJoueur, 0);
        int pointsFinaux = obtenirScoreViaReflection(idJoueur);

        assertEquals(pointsInitiaux, pointsFinaux);
    }

    @Test
    void ajouterScore_avecNombreNegatif() {
        String idJoueur = "test_joueur_negatif";
        gestionnaire.ajouterScore(idJoueur, 1000);
        int pointsAvantRetrait = obtenirScoreViaReflection(idJoueur);
        gestionnaire.ajouterScore(idJoueur, -500);
        int pointsApresRetrait = obtenirScoreViaReflection(idJoueur);

        assertEquals(pointsAvantRetrait - 500, pointsApresRetrait);
    }

    private int obtenirScoreViaReflection(String idJoueur) {
        try {
            Field field = GestionnaireBoutique.class.getDeclaredField("depotInventaire");
            field.setAccessible(true);
            IDepotInventaire depot = (IDepotInventaire) field.get(gestionnaire);
            return depot.charger(idJoueur).getScore();
        } catch (Exception e) {
            fail("Impossible d'accéder au score du joueur: " + e.getMessage());
            return -1;
        }
    }
}

