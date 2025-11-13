package boutique.repository;

import boutique.modele.InventaireJoueur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepotInventaireJsonTest {

    @TempDir
    Path tempDir;

    // TODO finir ce test
    /*@Test
    void sauvegarder() {
        DepotInventaireJson depot = new DepotInventaireJson(tempDir.toString());
        InventaireJoueur inventaire = new InventaireJoueur(10);
        depot.sauvegarder("joueur1", inventaire);

        Path fichier = tempDir.resolve("joueur1_inventaire.json");
        assertTrue(Files.exists(fichier));
        try {
            String contenu = java.nio.file.Files.readString(fichier);
            Gson gson = new Gson();
            InventaireJoueur inventaireCharge = gson.fromJson(contenu, InventaireJoueur.class);
            assertEquals(10, inventaireCharge.getScore());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }*/

    @Test
    void charger() {
        DepotInventaireJson depot = new DepotInventaireJson(tempDir.toString());
        InventaireJoueur inventaireOriginal = new InventaireJoueur(20);
        depot.sauvegarder("joueur2", inventaireOriginal);

        InventaireJoueur inventaireCharge = depot.charger("joueur2");
        assertEquals(20, inventaireCharge.getScore());

        InventaireJoueur inventaireDefaut = depot.charger("inexistant");
        assertEquals(0, inventaireDefaut.getScore());
    }
}