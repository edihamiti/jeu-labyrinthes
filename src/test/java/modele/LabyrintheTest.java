package modele;

import org.junit.jupiter.api.Test;

import modele.Cellules.Cellule;
import modele.Cellules.Chemin;
import modele.Cellules.Entree;
import modele.Cellules.Mur;
import modele.Cellules.Sortie;

import static org.junit.jupiter.api.Assertions.*;

class LabyrintheTest {

    @Test
    void calculePlusCourtChemin() {
        // Crée un labyrinthe de test
        Labyrinthe labyrinthe = new Labyrinthe(10, 10, 20.0);
        labyrinthe.generer();

        // Calcule le plus court chemin
        int longueurChemin = labyrinthe.calculePlusCourtChemin();


        // Vérifie que la longueur du chemin est supérieure à zéro
        assertTrue(longueurChemin > 0, "La longueur du plus court chemin doit être supérieure à zéro.");
    }

        @Test
    public void testGenerer_creeEntreeEtSortie() {
        Labyrinthe l = new Labyrinthe(12, 12, 50);
        l.generer();
        Cellule[][] cellules = l.getCellules();
        assertNotNull(cellules, "Les cellules ne doivent pas être null après génération");

        // L'entrée est positionnée en (0,1)
        assertTrue(cellules[0][1] instanceof Entree, "Une Entrée doit exister en (0,1)");

        boolean foundExit = false;
        for (int i = 0; i < l.getLargeurMax(); i++) {
            for (int j = 0; j < l.getHauteurMax(); j++) {
                if (cellules[i][j] != null && cellules[i][j].estSortie()) {
                    foundExit = true;
                }
            }
        }
        assertTrue(foundExit, "Une Sortie doit être présente après génération");
    }

    @Test
    public void testPeutDeplacer_et_estSurSortie() {
        Labyrinthe l = new Labyrinthe(3, 3, 100);
        int lm = l.getLargeurMax();
        int hm = l.getHauteurMax();

        Cellule[][] arr = new Cellule[lm][hm];
        for (int i = 0; i < lm; i++) {
            for (int j = 0; j < hm; j++) {
                arr[i][j] = new Mur(i, j);
            }
        }

        arr[0][1] = new Entree(0, 1);
        arr[1][1] = new Chemin(1, 1);
        arr[2][1] = new Chemin(2, 1);
        arr[3][1] = new Sortie(3, 1);

        l.setCellules(arr);

        assertTrue(l.peutDeplacer(1, 1), "On doit pouvoir se déplacer sur un Chemin");
        assertFalse(l.peutDeplacer(0, 0), "On ne doit pas pouvoir se déplacer sur un Mur");
        assertFalse(l.peutDeplacer(-1, -1), "Out of bounds doit retourner false");

        assertTrue(l.estSurSortie(3, 1), "La cellule (3,1) est une sortie");
    }
}