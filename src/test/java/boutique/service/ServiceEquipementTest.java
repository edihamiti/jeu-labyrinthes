package boutique.service;

import boutique.modele.Cosmetique;
import boutique.modele.InventaireJoueur;
import boutique.modele.TypeCosmetique;
import boutique.repository.DepotCosmetiqueMemoire;
import boutique.repository.DepotInventaireTest;
import boutique.repository.IDepotCosmetique;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceEquipementTest {

    private ServiceEquipement serviceEquipement;
    private IDepotCosmetique depotCosmetique;
    private DepotInventaireTest depotInventaire;

    @BeforeEach
    void setUp() {
        depotCosmetique = new DepotCosmetiqueMemoire();
        depotInventaire = new DepotInventaireTest(1000);
        serviceEquipement = new ServiceEquipement(depotCosmetique, depotInventaire);
    }

    @Test
    void equiperCosmetique_avecSucces() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        ResultatEquipement resultat = serviceEquipement.equiperCosmetique(idJoueur, idCosmetique);

        assertTrue(resultat.estReussi());
        assertEquals("Cosmétique équipé avec succès", resultat.getMessage());

        InventaireJoueur inventaireMaj = depotInventaire.charger(idJoueur);
        assertEquals(idCosmetique, inventaireMaj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_MUR));
    }

    @Test
    void equiperCosmetique_nonPossede() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";
        ResultatEquipement resultat = serviceEquipement.equiperCosmetique(idJoueur, idCosmetique);

        assertFalse(resultat.estReussi());
        assertEquals("Vous ne possédez pas ce cosmétique", resultat.getMessage());
    }

    @Test
    void equiperCosmetique_cosmetiqueIntrouvable() {
        String idJoueur = "joueur1";
        String idCosmetique = "cosmetique_existe_pas";
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        ResultatEquipement resultat = serviceEquipement.equiperCosmetique(idJoueur, idCosmetique);

        assertFalse(resultat.estReussi());
        assertEquals("Cosmétique introuvable", resultat.getMessage());
    }

    @Test
    void equiperCosmetique_remplacementCosmetiqueEquipe() {
        String idJoueur = "joueur1";
        String idCosmetique1 = "mur_brique";
        String idCosmetique2 = "mur_pierre";

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique1);
        inventaire.ajouterCosmetique(idCosmetique2);
        inventaire.equiperCosmetique(TypeCosmetique.TEXTURE_MUR, idCosmetique1);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        ResultatEquipement resultat = serviceEquipement.equiperCosmetique(idJoueur, idCosmetique2);

        assertTrue(resultat.estReussi());

        InventaireJoueur inventaireMaj = depotInventaire.charger(idJoueur);
        assertEquals(idCosmetique2, inventaireMaj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_MUR));
    }

    @Test
    void obtenirTextureEquipee_avecCosmetiqueEquipe() {
        String idJoueur = "joueur1";
        String idCosmetique = "mur_brique";

        Cosmetique cosmetique = depotCosmetique.obtenirParId(idCosmetique).orElseThrow();

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique);
        inventaire.equiperCosmetique(TypeCosmetique.TEXTURE_MUR, idCosmetique);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        String texture = serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_MUR);

        assertEquals(cosmetique.cheminTexture(), texture);
    }

    @Test
    void obtenirTextureEquipee_sansCosmetiqueEquipe() {
        String idJoueur = "joueur1";
        String texture = serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_MUR);

        assertEquals("textures/defaut_texture_mur.png", texture);
    }

    @Test
    void obtenirTextureEquipee_cosmetiqueIntrouvableDansDepot() {
        String idJoueur = "joueur1";
        String idCosmetique = "cosmetique_existe_pas";

        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique);
        inventaire.equiperCosmetique(TypeCosmetique.TEXTURE_JOUEUR, idCosmetique);
        depotInventaire.sauvegarder(idJoueur, inventaire);
        String texture = serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_JOUEUR);

        assertEquals("textures/defaut_texture_joueur.png", texture);
    }

    @Test
    void obtenirTextureEquipee_tousLesTypesParDefaut() {
        String idJoueur = "joueur1";

        assertEquals("textures/defaut_texture_mur.png", serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_MUR));
        assertEquals("textures/defaut_texture_joueur.png", serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_JOUEUR));
        assertEquals("textures/defaut_texture_chemin.png", serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_CHEMIN));
        assertEquals("textures/defaut_texture_sortie.png", serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_SORTIE));
    }

    @Test
    void equiperCosmetique_plusieursCosmetiquesDifferentsTypes() {
        String idJoueur = "joueur1";
        String idMur = "mur_brique";
        String idJoueurTexture = "joueur_rouge";
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idMur);
        inventaire.ajouterCosmetique(idJoueurTexture);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        ResultatEquipement resultat1 = serviceEquipement.equiperCosmetique(idJoueur, idMur);
        ResultatEquipement resultat2 = serviceEquipement.equiperCosmetique(idJoueur, idJoueurTexture);

        assertTrue(resultat1.estReussi());
        assertTrue(resultat2.estReussi());

        InventaireJoueur inventaireMaj = depotInventaire.charger(idJoueur);
        assertEquals(idMur, inventaireMaj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_MUR));
        assertEquals(idJoueurTexture, inventaireMaj.getCosmetiqueEquipe(TypeCosmetique.TEXTURE_JOUEUR));
    }

    @Test
    void obtenirTextureEquipee_apresEquipement() {
        String idJoueur = "joueur1";
        String idCosmetique = "sol_herbe";
        Cosmetique cosmetique = depotCosmetique.obtenirParId(idCosmetique).orElseThrow();
        InventaireJoueur inventaire = depotInventaire.charger(idJoueur);
        inventaire.ajouterCosmetique(idCosmetique);
        depotInventaire.sauvegarder(idJoueur, inventaire);

        serviceEquipement.equiperCosmetique(idJoueur, idCosmetique);
        String texture = serviceEquipement.obtenirTextureEquipee(idJoueur, TypeCosmetique.TEXTURE_CHEMIN);

        assertEquals(cosmetique.cheminTexture(), texture);
    }
}

