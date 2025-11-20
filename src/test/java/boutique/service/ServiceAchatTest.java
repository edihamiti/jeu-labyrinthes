package boutique.service;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.repository.DepotCosmetiqueMemoire;
import boutique.repository.DepotInventaireTest;
import boutique.repository.IDepotCosmetique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceAchatTest {

    private ServiceAchat serviceAchat;
    private IDepotCosmetique depotCosmetique;
    private DepotInventaireTest depotInventaire;

    @BeforeEach
    void setUp() {
        depotCosmetique = new DepotCosmetiqueMemoire();
        depotInventaire = new DepotInventaireTest(1000);
        serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);
    }

    @Test
    void acheterCosmetique_avecSucces() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";
        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);

        assertTrue(resultat.estReussi());
        assertEquals("Achat réussi", resultat.getMessage());
        assertNotNull(resultat.getNouveauScore());

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertTrue(inventaire.possedeCosmetique(idCosmetique));
        assertTrue(depotInventaire.aEteSauvegarde(idJoueur));
    }

    @Test
    void acheterCosmetique_cosmetiqueIntrouvable() {
        String idJoueur = "joueur1";
        String idCosmetique = "inexistant";
        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);

        assertFalse(resultat.estReussi());
        assertEquals("Cosmétique introuvable", resultat.getMessage());
        assertNull(resultat.getNouveauScore());

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertEquals(1000, inventaire.getScore());
    }

    @Test
    void acheterCosmetique_dejaPossede() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";
        serviceAchat.acheterCosmetique(idJoueur, idCosmetique);
        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);

        assertFalse(resultat.estReussi());
        assertEquals("Vous possédez déjà ce cosmétique", resultat.getMessage());
        assertNull(resultat.getNouveauScore());
    }

    @Test
    void acheterCosmetique_scoreInsuffisant() {
        String idJoueur = "joueur_pauvre";
        depotInventaire = new DepotInventaireTest(50);
        serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);
        String idCosmetique = "mur_brique";
        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);

        assertFalse(resultat.estReussi());
        assertTrue(resultat.getMessage().contains("Score insuffisant"));
        assertNull(resultat.getNouveauScore());

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertFalse(inventaire.possedeCosmetique(idCosmetique));
        assertEquals(50, inventaire.getScore());
    }

    @Test
    void acheterCosmetique_scoreDecrementeCorrectement() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";
        int scoreInitial = 1000;
        Cosmetique cosmetique = depotCosmetique.obtenirParId(idCosmetique).orElseThrow();
        int prixAttendu = cosmetique.prix();

        ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, idCosmetique);

        assertTrue(resultat.estReussi());
        assertEquals(scoreInitial - prixAttendu, resultat.getNouveauScore());

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertEquals(scoreInitial - prixAttendu, inventaire.getScore());
    }

    @Test
    void acheterCosmetique_plusieursAchatsSuccessifs() {
        String idJoueur = "joueur1";
        depotInventaire = new DepotInventaireTest(10000);
        serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);

        String idCosmetique1 = "mur_brique";
        String idCosmetique2 = "sortie_stone";
        ResultatAchat resultat1 = serviceAchat.acheterCosmetique(idJoueur, idCosmetique1);
        ResultatAchat resultat2 = serviceAchat.acheterCosmetique(idJoueur, idCosmetique2);

        assertTrue(resultat1.estReussi());
        assertTrue(resultat2.estReussi());

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertTrue(inventaire.possedeCosmetique(idCosmetique1));
        assertTrue(inventaire.possedeCosmetique(idCosmetique2));
        assertEquals(2, inventaire.getCosmetiquesPossedes().size());
    }

    @Test
    void acheterCosmetique_tousLesCosmetiquesDuDepot() {
        String idJoueur = "joueur_riche";
        depotInventaire = new DepotInventaireTest(100000);
        serviceAchat = new ServiceAchat(depotCosmetique, depotInventaire);
        int compteurAchatsReussis = 0;
        for (Cosmetique cosmetique : depotCosmetique.obtenirTous()) {
            ResultatAchat resultat = serviceAchat.acheterCosmetique(idJoueur, cosmetique.id());
            if (resultat.estReussi()) {
                compteurAchatsReussis++;
            }
        }

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        assertEquals(compteurAchatsReussis, inventaire.getCosmetiquesPossedes().size());
        assertTrue(compteurAchatsReussis > 0);
    }
}


