package modele;

public class Main {
    public static void main(String[] args) {
        System.out.println("Bienvenue dans le jeu du Labyrinthe");
        Labyrinthe lab = new Labyrinthe(10, 10, 20.0);
        lab.generer();
        lab.afficher();
        int dist = lab.calculePlusCourtChemin();
        System.out.println("Longueur du plus court chemin : " + dist);
    }
}
