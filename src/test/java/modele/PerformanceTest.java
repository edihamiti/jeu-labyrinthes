package modele;

import modele.generateurs.GenerateurAleatoire;
import modele.generateurs.GenerateurParfait;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests de performance pour mesurer les temps de génération des labyrinthes
 * sans interface graphique.
 */
public class PerformanceTest {

    /**
     * Mesure le temps de génération d'un labyrinthe aléatoire
     */
    private long mesurerTempsAleatoire(int largeur, int hauteur, double pourcentageMurs) {
        Labyrinthe lab = new Labyrinthe(largeur, hauteur, pourcentageMurs, 0);
        GenerateurAleatoire gen = new GenerateurAleatoire(largeur, hauteur, pourcentageMurs);
        
        long debut = System.nanoTime();
        gen.generer(lab);
        long fin = System.nanoTime();
        return fin - debut;
    }

    /**
     * Mesure le temps de génération d'un labyrinthe parfait
     */
    private long mesurerTempsParfait(int largeur, int hauteur, int distanceMin) {
        Labyrinthe lab = new Labyrinthe(largeur, hauteur, 0, distanceMin);
        GenerateurParfait gen = new GenerateurParfait(largeur, hauteur, distanceMin);
        
        long debut = System.nanoTime();
        gen.generer(lab);
        long fin = System.nanoTime();
        
        return fin - debut;
    }

    /**
     * Effectue plusieurs mesures et calcule la moyenne
     */
    private double moyenneMesures(List<Long> mesures) {
        long somme = 0;
        for (long mesure : mesures) {
            somme += mesure;
        }
        return somme / (double) mesures.size();
    }

    /**
     * Convertit les nanosecondes en millisecondes
     */
    private double nsEnMs(double ns) {
        return ns / 1_000_000.0;
    }

    @Test
    public void testPerformanceAleatoire() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("  TESTS DE PERFORMANCE - GÉNÉRATEUR ALÉATOIRE");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
        
        // Tailles croissantes (n, n*2)
        int[] tailles = {10, 20, 30, 50, 75, 100, 150, 200};
        double[] pourcentages = {20.0, 30.0, 50.0};
        int nbMesures = 5; // Nombre de mesures par configuration
        
        System.out.println("Nombre de mesures par configuration : " + nbMesures);
        System.out.println();
        
        // En-tête du tableau
        System.out.printf("%-15s | %-20s | %-20s | %-20s%n", 
            "Taille (n×2n)", "Murs 20%", "Murs 30%", "Murs 50%");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");
        
        for (int n : tailles) {
            int largeur = n;
            int hauteur = n * 2;
            
            System.out.printf("%-15s | ", largeur + "×" + hauteur);
            
            for (int p = 0; p < pourcentages.length; p++) {
                double pourcentage = pourcentages[p];
                List<Long> mesures = new ArrayList<>();
                
                // Effectuer plusieurs mesures
                for (int i = 0; i < nbMesures; i++) {
                    long temps = mesurerTempsAleatoire(largeur, hauteur, pourcentage);
                    mesures.add(temps);
                }
                
                double moyenne = moyenneMesures(mesures);
                double moyenneMs = nsEnMs(moyenne);
                
                System.out.printf("%-20s", String.format("%.2f ms", moyenneMs));
                if (p < pourcentages.length - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    @Test
    public void testPerformanceParfait() {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("  TESTS DE PERFORMANCE - GÉNÉRATEUR PARFAIT");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
        
        // Tailles croissantes (n, n*2) avec distance minimale n
        int[] tailles = {10, 20, 30, 50, 75, 100, 150, 200};
        int nbMesures = 5; // Nombre de mesures par configuration
        
        System.out.println("Nombre de mesures par configuration : " + nbMesures);
        System.out.println("Distance minimale : n (où n est la largeur)");
        System.out.println();
        
        // En-tête du tableau - trois colonnes pour différentes distances
        System.out.printf("%-15s | %-20s | %-20s | %-20s%n",
            "Taille (n×2n)", "Dist = n/2", "Dist = n", "Dist = n*1.5");
        System.out.println("─────────────────────────────────────────────────────────────────────────────────");

        for (int n : tailles) {
            int largeur = n;
            int hauteur = n * 2;

            System.out.printf("%-15s | ", largeur + "×" + hauteur);

            // Trois distances différentes : n/2, n, et n*1.5
            int[] distances = {Math.max(1, n / 2), n, (int)(n * 1.5)};

            for (int d = 0; d < distances.length; d++) {
                int distanceMin = distances[d];
                List<Long> mesures = new ArrayList<>();

                // Effectuer plusieurs mesures
                for (int i = 0; i < nbMesures; i++) {
                    long temps = mesurerTempsParfait(largeur, hauteur, distanceMin);
                    mesures.add(temps);
                }

                double moyenne = moyenneMesures(mesures);
                double moyenneMs = nsEnMs(moyenne);

                System.out.printf("%-20s", String.format("%.2f ms", moyenneMs));
                if (d < distances.length - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
        
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
    }

    @Test
    public void testPerformanceComplet() {
        System.out.println("\n\n");
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║     RAPPORT COMPLET DE PERFORMANCE - GÉNÉRATION LABYRINTHES   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        testPerformanceAleatoire();
        System.out.println("\n\n");
        testPerformanceParfait();
    }
}
