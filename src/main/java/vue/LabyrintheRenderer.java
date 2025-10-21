package vue;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import modele.Cellules.Cellule;
import modele.Labyrinthe;

/**
 * View layer: Responsible for rendering the labyrinth
 * Separates rendering logic from controller
 */
public class LabyrintheRenderer {
    
    private final Image imgMur;
    private final Image imgChemin;
    private final Image imgSortie;
    private final Image imgJoueur;
    private final int tailleCellule;
    
    /**
     * Constructor that loads all image resources
     */
    public LabyrintheRenderer() {
        this(50); // Default cell size
    }
    
    /**
     * Constructor with custom cell size
     * @param tailleCellule size of each cell in pixels
     */
    public LabyrintheRenderer(int tailleCellule) {
        this.tailleCellule = tailleCellule;
        this.imgMur = new Image(getClass().getResourceAsStream("/img/mur.png"));
        this.imgChemin = new Image(getClass().getResourceAsStream("/img/chemin.png"));
        this.imgSortie = new Image(getClass().getResourceAsStream("/img/sortie.png"));
        this.imgJoueur = new Image(getClass().getResourceAsStream("/img/joueur.png"));
    }
    
    /**
     * Creates a Canvas with the labyrinth rendered on it
     * @param labyrinthe the labyrinth model to render
     * @return Canvas with the rendered labyrinth
     */
    public Canvas render(Labyrinthe labyrinthe) {
        Cellule[][] cellules = labyrinthe.getCellules();
        int largeurCanvas = cellules[0].length * tailleCellule;
        int hauteurCanvas = cellules.length * tailleCellule;
        
        Canvas canvas = new Canvas(largeurCanvas, hauteurCanvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // Render all cells
        for (int i = 0; i < cellules.length; i++) {
            for (int j = 0; j < cellules[i].length; j++) {
                renderCell(gc, cellules[i][j], i, j, labyrinthe.getJoueurX(), labyrinthe.getJoueurY());
            }
        }
        
        return canvas;
    }
    
    /**
     * Renders a single cell at the specified position
     */
    private void renderCell(GraphicsContext gc, Cellule cellule, int i, int j, int joueurX, int joueurY) {
        double x = j * tailleCellule;
        double y = i * tailleCellule;
        
        // Draw player on top if at this position
        if (i == joueurX && j == joueurY) {
            gc.drawImage(imgJoueur, x, y, tailleCellule, tailleCellule);
        } else if (cellule.estMur()) {
            gc.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
        } else if (cellule.estSortie()) {
            gc.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
        } else if (cellule.estChemin() || cellule.estEntree()) {
            gc.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
        }
    }
}
