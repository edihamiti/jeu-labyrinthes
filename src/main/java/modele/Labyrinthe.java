package modele;

import modele.Cellules.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Labyrinthe {

    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int distanceMin;
    private final int largeurMax;
    private final int hauteurMax;
    private Cellule[][] cellules;
    private int joueurX;
    private int joueurY;
    private boolean jeuEnCours;
    private int nbChemins;

    public Labyrinthe(int largeur, int hauteur, double pourcentageMurs) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.pourcentageMurs = pourcentageMurs;
        this.distanceMin = 1;
        this.largeurMax = largeur + 2;
        this.hauteurMax = hauteur + 2;
        this.joueurX = 0;
        this.joueurY = 1;
        this.jeuEnCours = true;
        this.nbChemins = 0;
    }

    public Labyrinthe(Defi defi) {
        this(defi.getLargeur(), defi.getHauteur(), defi.getPourcentageMurs());
    }

    /**
    * Génère un labyrinthe complet à partir des paramètres du constructeur.
    * <p>
    * Le labyrinthe est d’abord rempli de murs, puis un chemin principal est creusé
    * à l’aide de la méthode {@link #faireChemin(Cellule[][], int, int)}.
    * Ensuite, des chemins secondaires (impasses) sont ajoutés via {@link #faireCheminAlternatif(Cellule[][], int, int)},
    * et enfin, un remplissage aléatoire transforme certains murs restants en chemins
    * en fonction d’un pourcentage de murs ajusté dynamiquement.
    * </p>
    *
    * <p>
    * Le pourcentage effectif de murs diminue automatiquement lorsque le labyrinthe
    * contient déjà beaucoup de chemins, ce qui permet de maintenir un équilibre
    * entre zones ouvertes et zones murées.
    * </p>
    *
    * Étapes :
    * <ol>
    *     <li>Initialise toutes les cellules en tant que murs</li>
    *     <li>Crée l’entrée et génère un chemin principal jusqu’à une sortie</li>
    *     <li>Ajoute aléatoirement des chemins alternatifs</li>
    *     <li>Transforme certains murs en chemins selon une probabilité adaptative</li>
    * </ol>
    */
    public void generer() {
        cellules = new Cellule[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                cellules[i][j] = new Mur(i, j);
            }
        }

        int entreeX = 0;
        int entreeY = 1;
        cellules[entreeX][entreeY] = new Entree(entreeX, entreeY);
        faireChemin(cellules, entreeX, entreeY);

        for (int i = 1; i < largeurMax - 1; i++) {
            for (int j = 1; j < hauteurMax - 1; j++) {
                if (cellules[i][j] instanceof Chemin) {
                    if (i%10==0) {
                        faireCheminAlternatif(cellules, i, j);
                    }
                }
            }
        }

        // Transformation mur en chemin aléatoirement selon pourcentageMurs;
        double pourcentageCheminsActuel = 1.0 - (((double) nbChemins / ((largeurMax - 2) * (hauteurMax - 2)) * 100.0) / 100.0);
        double probaBase = 1 - (pourcentageMurs / 100.0);
        double probaChemin = probaBase * pourcentageCheminsActuel;
        for (int i = 1; i < largeurMax-1; i++) {
            for (int j = 1; j < hauteurMax-1; j++) {
                if ((!(cellules[i][j] instanceof Entree) && !(cellules[i][j] instanceof Sortie)) && (Math.random() < probaChemin)) {
                    cellules[i][j] = new Chemin(i, j);
                }
            }
        }

    }


    /**
    * Crée le chemin principal du labyrinthe à partir d’une position de départ.
    * <p>
    * Cette méthode utilise une recherche en profondeur (DFS) avec une pile
    * pour creuser un chemin aléatoire sans repasser sur les cellules déjà visitées.
    * Si aucune direction n’est possible, l’algorithme revient en arrière
    * jusqu’à trouver un nouvel embranchement.
    * </p>
    *
    * <p>
    * Lorsque le chemin atteint une certaine longueur maximale ou qu’aucune
    * direction n’est disponible, la cellule courante devient la sortie.
    * </p>
    *
    * @param cellules la grille représentant le labyrinthe
    * @param startX   la coordonnée X de départ du chemin (souvent 0)
    * @param startY   la coordonnée Y de départ du chemin (souvent 1)
    */
    public void faireChemin(Cellule[][] cellules, int startX, int startY) {
        Random random = new Random();
        LinkedList<int[]> pile = new LinkedList<>();
        boolean[][] visite = new boolean[largeurMax][hauteurMax];

        int x = startX;
        int y = startY;

        cellules[x][y] = new Entree(x, y);
        visite[x][y] = true;
        pile.push(new int[]{x, y});

        int[][] directions = {{0,-1}, {0,1}, {-1,0}, {1,0}}; // haut, bas, gauche, droite

        while (!pile.isEmpty()) {
            int[] courant = pile.peek();
            x = courant[0];
            y = courant[1];

            // Directions possibles
            List<int[]> voisins = new ArrayList<>();
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx > 0 && ny > 0 && nx < largeurMax - 1 && ny < hauteurMax - 1 && !visite[nx][ny]) {
                    voisins.add(new int[]{nx, ny});
                }
            }

            if (!voisins.isEmpty()) {
                int[] suivant = voisins.get(random.nextInt(voisins.size()));
                int nx = suivant[0];
                int ny = suivant[1];

                cellules[nx][ny] = new Chemin(nx, ny);
                nbChemins ++;
                visite[nx][ny] = true;
                pile.push(new int[]{nx, ny});
            } else {
                pile.pop();
            }

            if (pile.size() > (largeur + hauteur)*2) {
                break;
            }
        }

        if (!pile.isEmpty()) {
            int[] derniere = pile.peek();
            cellules[derniere[0]][derniere[1]] = new Sortie(derniere[0], derniere[1]);
        }
    }

    /**
    * Crée un chemin secondaire aléatoire à partir d’une cellule existante.
    * <p>
    * Utilisé pour générer des impasses ou de petits embranchements dans le labyrinthe,
    * cette méthode creuse un court chemin dans des directions aléatoires à partir d’un
    * point du chemin principal.
    * </p>
    *
    * <p>
    * Les chemins alternatifs ne créent pas de nouvelle sortie et s’arrêtent
    * dès qu’ils ne peuvent plus progresser ou qu’ils atteignent la longueur maximale définie.
    * </p>
    *
    * @param cellules la grille du labyrinthe
    * @param startX   la coordonnée X de départ du chemin alternatif
    * @param startY   la coordonnée Y de départ du chemin alternatif
    */
    public void faireCheminAlternatif(Cellule[][] cellules, int startX, int startY) {
        Random random = new Random();
        int[][] directions = {{0,-1}, {0,1}, {-1,0}, {1,0}};

        int x = startX;
        int y = startY;
        int longueur = (largeur+hauteur)/2;

        for (int i = 0; i < longueur; i++) {
            List<int[]> possibles = new java.util.ArrayList<>();
            for (int[] d : directions) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (nx > 0 && ny > 0 && nx < largeurMax - 1 && ny < hauteurMax - 1) {
                    if (cellules[nx][ny] instanceof Mur) {
                        possibles.add(new int[]{nx, ny});
                    }
                }
            }

            if (possibles.isEmpty()) break;

            int[] suivant = possibles.get(random.nextInt(possibles.size()));
            int nx = suivant[0];
            int ny = suivant[1];

            cellules[nx][ny] = new Chemin(nx, ny);
            nbChemins ++;

            x = nx;
            y = ny;
        }
    }


    public int calculePlusCourtChemin() {
        int startX = -1;
        int startY = -1;
        int endX = -1;
        int endY = -1;

        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                if (cellules[i][j] != null && cellules[i][j].estEntree()) {
                    startX = i;
                    startY = j;
                }
                if (cellules[i][j] != null && cellules[i][j].estSortie()) {
                    endX = i;
                    endY = j;
                }
            }
        }

        if (startX == -1 || endX == -1) {
            // Pas d'entrée ou sortie
            return 0;
        }

        boolean[][] visited = new boolean[largeurMax][hauteurMax];
        int[][] prevX = new int[largeurMax][hauteurMax];
        int[][] prevY = new int[largeurMax][hauteurMax];
        int[][] dist = new int[largeurMax][hauteurMax];
        for (int i = 0; i < largeurMax; i++) {
            for (int j = 0; j < hauteurMax; j++) {
                prevX[i][j] = -1;
                prevY[i][j] = -1;
                dist[i][j] = -1;
            }
        }

        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        dist[startX][startY] = 0;

        int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] courant = queue.removeFirst();
            int cx = courant[0];
            int cy = courant[1];

            if (cx == endX && cy == endY) {
                break;
            }

            for (int[] d : direction) {
                int nx = cx + d[0];
                int ny = cy + d[1];

                if (nx < 0 || nx >= largeurMax || ny < 0 || ny >= hauteurMax) continue;
                if (visited[nx][ny]) continue;
                if (cellules[nx][ny] == null) continue;
                if (cellules[nx][ny].estMur()) continue;

                visited[nx][ny] = true;
                prevX[nx][ny] = cx;
                prevY[nx][ny] = cy;
                dist[nx][ny] = dist[cx][cy] + 1;
                queue.addLast(new int[]{nx, ny});
            }
        }

        if (dist[endX][endY] == -1) {
            // Pas de chemin trouvé
            return 0;
        }
        return dist[endX][endY];
    }

    public double getPourcentageMurs() {
        return pourcentageMurs;
    }

    public boolean isJeuEnCours() {
        return jeuEnCours;
    }

    public int getJoueurY() {
        return joueurY;
    }

    public int getJoueurX() {
        return joueurX;
    }

    public int getDistanceMin() {
        return distanceMin;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getLargeur() {
        return largeur;
    }

    public void setJeuEnCours(boolean jeuEnCours) {
        this.jeuEnCours = jeuEnCours;
    }

    public void setJoueurY(int joueurY) {
        this.joueurY = joueurY;
    }

    public void setJoueurX(int joueurX) {
        this.joueurX = joueurX;
    }

    public void setCellules(Cellule[][] cellules) {
        this.cellules = cellules;
    }



    public void afficherAvecJoueur() {
        for (int j = 0; j < hauteurMax; j++) {
            for (int i = 0; i < largeurMax; i++) {
                if (i == joueurX && j == joueurY) {
                    System.out.print("P"); // joueur
                } else if (cellules[i][j].estMur()) {
                    System.out.print("#");
                } else if (cellules[i][j].estEntree()) {
                    System.out.print("E");
                } else if (cellules[i][j].estSortie()) {
                    System.out.print("S");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
        System.out.println("\nUtilisez Z (haut), S (bas), Q (gauche), D (droite) pour vous déplacer, X pour quitter");
    }

    public boolean peutDeplacer(int x, int y) {
        if (x < 0 || x >= largeurMax || y < 0 || y >= hauteurMax) {
            return false;
        } else if (cellules[x][y] == null || cellules[x][y].estMur()) {
            return false;
        }
        return !cellules[x][y].estMur();
    }

    public boolean estSurSortie(int x, int y) {
        return cellules[x][y].estSortie();
    }

    public char lireTouche() throws IOException {
        char c = (char) System.in.read();
        while (System.in.available() > 0) {
            System.in.read();
        }
        return Character.toLowerCase(c);
    }

    public int getLargeurMax() {
        return largeurMax;
    }

    public int getHauteurMax() {
        return hauteurMax;
    }

    public Cellule[][] getCellules() {
        return cellules;
    }
}
