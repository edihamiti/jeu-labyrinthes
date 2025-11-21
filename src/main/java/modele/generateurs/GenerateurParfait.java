package modele.generateurs;

import modele.Cellules.*;
import modele.Labyrinthe;

import java.util.*;


/**
 * Générateur d'un labyrinthe parfait.
 *
 * Un labyrinthe parfait est un labyrinthe sans cycles,
 * où il n'existe qu'un seul chemin possible entre deux cellules accessibles.
 *
 * Fonctionnement général :
 * - Toutes les cases sont initialisées en murs
 * - Une entrée est placée aléatoirement sur le bord gauche
 * - Le labyrinthe est creusé par exploration en profondeur (backtracking)
 * - La sortie est ensuite placée en respectant une distance minimale
 *   depuis l'entrée
 * - En mode "clé en poche", une clé est placée sur le chemin optimal
 *   vers la sortie, et la sortie se situe au-delà de la clé
 */
public class GenerateurParfait extends GenerateurLabyrinthe {
    int distanceMin;
    boolean cleEnPoche = false;


    /**
     * Constructeur standard.
     *
     * @param largeur largeur maximale du labyrinthe
     * @param hauteur hauteur maximale du labyrinthe
     * @param distanceMin distance minimale souhaitée entre l'entrée et la sortie
     */
    public GenerateurParfait(int largeur, int hauteur, int distanceMin) {
        super(largeur, hauteur);
        this.distanceMin = distanceMin;
    }


    /**
     * Constructeur avec activation du mode "clé obligatoire".
     *
     * @param largeur largeur maximale du labyrinthe
     * @param hauteur hauteur maximale du labyrinthe
     * @param distanceMin distance minimale souhaitée entre l'entrée et la sortie
     * @param cleEnPoche true pour placer une clé avant la sortie
     */
    public GenerateurParfait(int largeur, int hauteur, int distanceMin, boolean cleEnPoche) {
        super(largeur, hauteur);
        this.distanceMin = distanceMin;
        this.cleEnPoche = cleEnPoche;
    }


    /**
     * Génère le labyrinthe parfait complet.
     *
     * Étapes :
     * - Initialise toutes les cases en murs
     * - Place l'entrée aléatoirement
     * - Creuse le labyrinthe avec un backtracking
     * - Place soit uniquement la sortie, soit la clé et la sortie selon le mode
     *
     * @param lab labyrinthe à remplir
     */
    public void generer(Labyrinthe lab) {
        System.out.println("=== DEBUT GENERATION PARFAIT ===");
        System.out.println("Distance minimale requise: " + distanceMin);
        System.out.println("Mode clé activé: " + cleEnPoche);

        Cellule[][] cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        Random rand = new Random();

        int entreeX = 0;
        int entreeY = 1 + rand.nextInt(Math.max(1, hauteur - 2));
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        System.out.println("Entrée placée à: (" + entreeX + ", " + entreeY + ")");

        lab.setJoueurX(entreeX);
        lab.setJoueurY(entreeY);

        int startX = entreeX + 1;
        int startY = entreeY;

        if (startX < largeurMax - 1) {
            cellules[startX][startY] = new Chemin(startX, startY);
        }

        Stack<int[]> pile = new Stack<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];
        pile.push(new int[]{startX, startY});
        visite[startX][startY] = true;

        int[][] directions = {{0, 2}, {2, 0}, {0, -2}, {-2, 0}}; // droite, bas, gauche, haut (saut de 2)

        while (!pile.empty()) {
            int[] pos = pile.peek();
            int x = pos[0], y = pos[1];

            List<Integer> dirs = new ArrayList<>();
            for (int i = 0; i < directions.length; i++) {
                int nx = x + directions[i][0];
                int ny = y + directions[i][1];
                if (nx > 0 && nx < largeurMax - 1 && ny > 0 && ny < hauteurMax - 1 && !visite[nx][ny]) {
                    dirs.add(i);
                }
            }

            if (!dirs.isEmpty()) {
                int dir = dirs.get(rand.nextInt(dirs.size()));
                int nx = x + directions[dir][0];
                int ny = y + directions[dir][1];

                cellules[x + directions[dir][0]/2][y + directions[dir][1]/2] = new Chemin(x + directions[dir][0]/2, y + directions[dir][1]/2);
                cellules[nx][ny] = new Chemin(nx, ny);

                visite[nx][ny] = true;
                pile.push(new int[]{nx, ny});
            } else {
                pile.pop();
            }
        }

        if (cleEnPoche) {
            placerCleEtSortie(cellules, entreeX, entreeY, lab);
        } else {
            placerSortieAvecDistance(cellules, entreeX, entreeY);
        }

        lab.setCellules(cellules);
        System.out.println("=== FIN GENERATION PARFAIT ===");
    }


    /**
     * Place la sortie sur une cellule respectant la distance minimale et maximale
     * (distanceMin + 5) depuis l'entrée.
     * Si aucun candidat ne correspond, choisit la case la plus proche.
     *
     * @param cellules tableau des cellules du labyrinthe
     * @param entreeX coordonnée X de l'entrée
     * @param entreeY coordonnée Y de l'entrée
     */
    private void placerSortieAvecDistance(Cellule[][] cellules, int entreeX, int entreeY) {
        int[][] distances = calculerDistances(cellules, entreeX, entreeY);

        int distanceMax = distanceMin + 5;
        List<int[]> candidats = new ArrayList<>();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j].estChemin() &&
                    distances[i][j] >= distanceMin &&
                    distances[i][j] <= distanceMax) {
                    candidats.add(new int[]{i, j, distances[i][j]});
                }
            }
        }

        System.out.println("Distance min requise: " + distanceMin);
        System.out.println("Distance max autorisée: " + distanceMax);
        System.out.println("Candidats dans la plage: " + candidats.size());

        if (!candidats.isEmpty()) {
            Random rand = new Random();
            int[] choisi = candidats.get(rand.nextInt(candidats.size()));
            int sortieX = choisi[0];
            int sortieY = choisi[1];
            int distanceSortie = choisi[2];

            cellules[sortieX][sortieY] = new Sortie(sortieX, sortieY);
            System.out.println("Sortie placée à (" + sortieX + ", " + sortieY + ") avec distance: " + distanceSortie);
        } else {
            System.out.println("Aucun candidat dans la plage, recherche du meilleur compromis");
            int meilleureX = -1, meilleureY = -1, meilleureDiff = Integer.MAX_VALUE;

            for (int i = 0; i < largeurMax; i++) {
                for (int j = 0; j < hauteurMax; j++) {
                    if (cellules[i][j].estChemin() && distances[i][j] > 0) {
                        int diff = Math.abs(distances[i][j] - distanceMin);
                        if (diff < meilleureDiff) {
                            meilleureDiff = diff;
                            meilleureX = i;
                            meilleureY = j;
                        }
                    }
                }
            }

            if (meilleureX != -1 && meilleureY != -1) {
                cellules[meilleureX][meilleureY] = new Sortie(meilleureX, meilleureY);
                System.out.println("Sortie placée (fallback) à (" + meilleureX + ", " + meilleureY + ") avec distance: " + distances[meilleureX][meilleureY]);
            }
        }
    }


    /**
     * Calcule les distances depuis l'entrée vers toutes les cases accessibles
     * en utilisant un parcours en largeur (BFS).
     *
     * @param cellules tableau du labyrinthe
     * @param entreeX coordonnée X de départ
     * @param entreeY coordonnée Y de départ
     * @return matrice des distances (-1 pour les cellules non accessibles)
     */
    private int[][] calculerDistances(Cellule[][] cellules, int entreeX, int entreeY) {
        int[][] distances = new int[largeurMax][hauteurMax];
        boolean[][] visite = new boolean[largeurMax][hauteurMax];

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                distances[i][j] = -1;
            }
        }

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{entreeX, entreeY});
        distances[entreeX][entreeY] = 0;
        visite[entreeX][entreeY] = true;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int cellulesParcourues = 0;

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];
            cellulesParcourues++;

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (nx >= 0 && nx < largeurMax && ny >= 0 && ny < hauteurMax
                        && !visite[nx][ny]) {

                    Cellule cellule = cellules[nx][ny];
                    if (cellule.estChemin() || cellule.estEntree()) {
                        visite[nx][ny] = true;
                        distances[nx][ny] = distances[x][y] + 1;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        System.out.println("BFS: " + cellulesParcourues + " cellules parcourues");
        return distances;
    }

    /**
     * Mode avancé : place une clé et la sortie.
     * La clé est sur le chemin optimal vers la sortie et doit être récupérée avant.
     *
     * @param cellules tableau du labyrinthe
     * @param entreeX coordonnée X de l'entrée
     * @param entreeY coordonnée Y de l'entrée
     * @param lab labyrinthe à mettre à jour
     */
    private void placerCleEtSortie(Cellule[][] cellules, int entreeX, int entreeY, Labyrinthe lab) {

        int[][] distances = calculerDistances(cellules, entreeX, entreeY);

        int distanceMax = 0;
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j].estChemin() && distances[i][j] > distanceMax) {
                    distanceMax = distances[i][j];
                }
            }
        }


        if (distanceMax < 6) {
            placementSimple(cellules, distances, distanceMax, lab);
            return;
        }

        int distanceMinSortie = Math.max(6, (int)(distanceMax * 0.7));
        List<int[]> candidatsSortie = new ArrayList<>();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j].estChemin() && distances[i][j] >= distanceMinSortie) {
                    candidatsSortie.add(new int[]{i, j, distances[i][j]});
                }
            }
        }

        if (candidatsSortie.isEmpty()) {
            placementSimple(cellules, distances, distanceMax, lab);
            return;
        }

        Random rand = new Random();
        int[] positionSortie = candidatsSortie.get(rand.nextInt(candidatsSortie.size()));
        int sortieX = positionSortie[0];
        int sortieY = positionSortie[1];


        List<int[]> cheminOptimal = trouverCheminOptimal(cellules, entreeX, entreeY, sortieX, sortieY);

        if (cheminOptimal.size() < 4) {
            placementSimple(cellules, distances, distanceMax, lab);
            return;
        }


        int tailleChemin = cheminOptimal.size();
        int indexMinCle = Math.max(2, (int)(tailleChemin * 0.3));
        int indexMaxCle = Math.min(tailleChemin - 3, (int)(tailleChemin * 0.7));

        if (indexMinCle >= indexMaxCle) {
            indexMinCle = Math.max(1, tailleChemin / 3);
            indexMaxCle = Math.min(tailleChemin - 2, (tailleChemin * 2) / 3);
        }

        int indexCle = indexMinCle + rand.nextInt(Math.max(1, indexMaxCle - indexMinCle));
        int[] positionCle = cheminOptimal.get(indexCle);
        int cleX = positionCle[0];
        int cleY = positionCle[1];

        cellules[cleX][cleY] = new Cle(cleX, cleY);
        lab.setPositionCle(cleX, cleY);

        cellules[sortieX][sortieY] = new Sortie(sortieX, sortieY, true);
        lab.setPositionSortie(sortieX, sortieY);
    }


    /**
     * Placement simple pour les petits labyrinthes.
     * Place la sortie sur la case la plus éloignée et la clé à mi-chemin.
     *
     * @param cellules tableau du labyrinthe
     * @param distances matrice des distances depuis l'entrée
     * @param distanceMax distance maximale parcourue
     * @param lab labyrinthe à mettre à jour
     */
    private void placementSimple(Cellule[][] cellules, int[][] distances, int distanceMax, Labyrinthe lab) {
        List<int[]> tousLesChemins = new ArrayList<>();

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j].estChemin() && distances[i][j] > 0) {
                    tousLesChemins.add(new int[]{i, j, distances[i][j]});
                }
            }
        }

        if (tousLesChemins.size() < 2) {
            System.out.println("[MODE CLE] Pas assez de chemins disponibles");
            return;
        }

        tousLesChemins.sort((a, b) -> Integer.compare(b[2], a[2]));

        int[] sortie = tousLesChemins.get(0);
        cellules[sortie[0]][sortie[1]] = new Sortie(sortie[0], sortie[1], true);
        lab.setPositionSortie(sortie[0], sortie[1]);

        int indexCle = Math.min(tousLesChemins.size() / 2, tousLesChemins.size() - 1);
        int[] cle = tousLesChemins.get(indexCle);

        if (cle[0] != sortie[0] || cle[1] != sortie[1]) {
            cellules[cle[0]][cle[1]] = new Cle(cle[0], cle[1]);
            lab.setPositionCle(cle[0], cle[1]);
        } else if (tousLesChemins.size() > 2) {
            int[] cleAlt = tousLesChemins.get(1);
            cellules[cleAlt[0]][cleAlt[1]] = new Cle(cleAlt[0], cleAlt[1]);
            lab.setPositionCle(cleAlt[0], cleAlt[1]);
        }
    }


    /**
     * Trouve le chemin optimal entre deux points en reconstruisant
     * le parcours BFS.
     *
     * @param cellules tableau du labyrinthe
     * @param startX coordonnée X de départ
     * @param startY coordonnée Y de départ
     * @param endX coordonnée X d'arrivée
     * @param endY coordonnée Y d'arrivée
     * @return liste ordonnée des coordonnées du chemin optimal
     */
    private List<int[]> trouverCheminOptimal(Cellule[][] cellules, int startX, int startY, int endX, int endY) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];
        int[][][] parent = new int[largeurMax][hauteurMax][2];

        queue.add(new int[]{startX, startY});
        visite[startX][startY] = true;
        parent[startX][startY][0] = -1;
        parent[startX][startY][1] = -1;

        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];

            if (x == endX && y == endY) {
                break;
            }

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (nx >= 0 && nx < largeurMax && ny >= 0 && ny < hauteurMax
                        && !visite[nx][ny]) {

                    Cellule cellule = cellules[nx][ny];
                    if (cellule.estChemin() || cellule.estEntree() || (nx == endX && ny == endY)) {
                        visite[nx][ny] = true;
                        parent[nx][ny][0] = x;
                        parent[nx][ny][1] = y;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }

        List<int[]> chemin = new ArrayList<>();
        int currentX = endX;
        int currentY = endY;

        while (currentX != -1 && currentY != -1) {
            chemin.add(0, new int[]{currentX, currentY});
            int parentX = parent[currentX][currentY][0];
            int parentY = parent[currentX][currentY][1];
            currentX = parentX;
            currentY = parentY;
        }

        return chemin;
    }
}
