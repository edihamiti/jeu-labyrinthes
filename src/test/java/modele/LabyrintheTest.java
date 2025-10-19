package modele;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LabyrintheTest {

    @Test
    void calculePlusCourtChemin() {
        // Crée un labyrinthe de test
        Labyrinthe labyrinthe = new Labyrinthe(10, 10, 20.0, 2);
        labyrinthe.generer();

        // Calcule le plus court chemin
        int longueurChemin = labyrinthe.calculePlusCourtChemin();


        // Vérifie que la longueur du chemin est supérieure à zéro
        assertTrue(longueurChemin > 0, "La longueur du plus court chemin doit être supérieure à zéro.");
    }
}