# Before & After: MVC Refactoring

## Visual Comparison

### BEFORE: Mixed Concerns ❌

```
┌──────────────────────────────────────────────────┐
│              JeuControleur                       │
│  (Controller but doing too much!)                │
│                                                  │
│  - Loading images (View concern)                 │
│  - Creating Canvas (View concern)                │
│  - Drawing cells (View concern)                  │
│  - Handling clicks (✓ correct)                   │
│  - Calling model (✓ correct)                     │
│  - Manually refreshing view after changes (⚠️)   │
└────────────────┬─────────────────────────────────┘
                 │
                 ↓
┌──────────────────────────────────────────────────┐
│              Labyrinthe                          │
│  (Model but with UI code!)                       │
│                                                  │
│  - Maze generation (✓ correct)                   │
│  - Player position: int x, y (⚠️ not observable) │
│  - afficherAvecJoueur() (❌ console output!)     │
│  - lireTouche() (❌ console input!)              │
│  - Game logic (✓ correct)                        │
└──────────────────────────────────────────────────┘

No separate View layer!
```

**Problems:**
- 🔴 Controller knows HOW to render (Canvas, images)
- 🔴 Model knows HOW to display (console output)
- 🔴 Model knows HOW to get input (console input)
- 🔴 No automatic updates
- 🔴 Tight coupling

---

### AFTER: Proper Separation ✅

```
┌──────────────────────────────────────────────────┐
│                 USER CLICKS                      │
└────────────────┬─────────────────────────────────┘
                 │
                 ↓
┌──────────────────────────────────────────────────┐
│              Jeu.fxml                            │
│  (View: FXML definition)                         │
│  <Button onAction="#deplacerHaut"/>              │
└────────────────┬─────────────────────────────────┘
                 │
                 ↓
┌──────────────────────────────────────────────────┐
│          JeuControleur                           │
│  (Controller: Coordination ONLY)                 │
│                                                  │
│  @FXML deplacerHaut() {                          │
│      jeu.deplacerJoueur(x, y)  ← delegates       │
│  }                                               │
│                                                  │
│  // Setup observers                              │
│  labyrinthe.joueurXProperty()                    │
│      .addListener(() -> afficherLabyrinthe())    │
└────────────────┬─────────────────────────────────┘
                 │ delegates
                 ↓
┌──────────────────────────────────────────────────┐
│              Jeu + Labyrinthe                    │
│  (Model: Business Logic ONLY)                    │
│                                                  │
│  - IntegerProperty joueurX (observable!)         │
│  - IntegerProperty joueurY (observable!)         │
│  - BooleanProperty jeuEnCours (observable!)      │
│  - deplacerJoueur(x, y) ← business logic         │
│  - estVictoire() ← business logic                │
│  - NO console I/O                                │
│  - NO rendering code                             │
└────────────────┬─────────────────────────────────┘
                 │ notifies via Properties
                 ↓
┌──────────────────────────────────────────────────┐
│          JeuControleur (listener)                │
│  afficherLabyrinthe() {                          │
│      renderer.render(labyrinthe) ← delegates     │
│  }                                               │
└────────────────┬─────────────────────────────────┘
                 │ delegates
                 ↓
┌──────────────────────────────────────────────────┐
│          LabyrintheRenderer                      │
│  (View: Rendering ONLY)                          │
│                                                  │
│  render(Labyrinthe) {                            │
│      - Load images                               │
│      - Create Canvas                             │
│      - Draw cells                                │
│      - Draw player                               │
│  }                                               │
│  - NO business logic                             │
│  - NO model manipulation                         │
└──────────────────────────────────────────────────┘
```

**Improvements:**
- ✅ Controller coordinates ONLY
- ✅ Model has pure business logic ONLY
- ✅ View renders ONLY
- ✅ Automatic updates via Properties
- ✅ Loose coupling

---

## Code Comparison

### Example 1: Model Changes

#### BEFORE ❌
```java
// Labyrinthe.java - Model had UI code!
public void afficherAvecJoueur() {
    for (int j = 0; j < hauteurMax; j++) {
        for (int i = 0; i < largeurMax; i++) {
            if (i == joueurX && j == joueurY) {
                System.out.print("P"); // ❌ Console output in Model!
            } else if (cellules[i][j].estMur()) {
                System.out.print("#");
            }
            // ... more printing
        }
        System.out.println();
    }
}

private int joueurX;  // ⚠️ Not observable
private int joueurY;  // ⚠️ Not observable
```

#### AFTER ✅
```java
// Labyrinthe.java - Pure Model!
// ✅ NO display methods
// ✅ NO console I/O

// ✅ Observable properties
private final IntegerProperty joueurX;
private final IntegerProperty joueurY;

public IntegerProperty joueurXProperty() { 
    return joueurX; 
}

public int getJoueurX() { 
    return joueurX.get(); 
}

public void setJoueurX(int x) { 
    this.joueurX.set(x);  // ✅ Notifies observers automatically!
}
```

---

### Example 2: Controller Changes

#### BEFORE ❌
```java
// JeuControleur.java - Controller had rendering!
private final Image imgMur = new Image(...);  // ❌ Loading images
private final Image imgChemin = new Image(...);
private final Image imgSortie = new Image(...);
private final Image imgJoueur = new Image(...);

private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
    Canvas canvas = new Canvas(...);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    for (int i = 0; i < labyrinthe.length; i++) {
        for (int j = 0; j < labyrinthe[i].length; j++) {
            // ❌ Drawing logic in Controller!
            if (labyrinthe[i][j].estMur())
                gc.drawImage(imgMur, x, y, ...);
            else if (labyrinthe[i][j].estChemin())
                gc.drawImage(imgChemin, x, y, ...);
            // ...
        }
    }
    return canvas;
}

private void deplacer(int nouveauX, int nouveauY) {
    if (labyrinthe.peutDeplacer(nouveauX, nouveauY)) {
        labyrinthe.setJoueurX(nouveauX);
        labyrinthe.setJoueurY(nouveauY);
        
        afficherLabyrinthe();  // ⚠️ Manual refresh
    }
}
```

#### AFTER ✅
```java
// JeuControleur.java - Pure coordination!
private LabyrintheRenderer renderer;  // ✅ Delegates to View

@FXML
public void initialize() {
    renderer = new LabyrintheRenderer();
    
    // ✅ Setup observers - automatic updates!
    labyrinthe.joueurXProperty()
        .addListener((obs, old, newVal) -> afficherLabyrinthe());
    labyrinthe.joueurYProperty()
        .addListener((obs, old, newVal) -> afficherLabyrinthe());
}

// ✅ Delegates to View layer
public void afficherLabyrinthe() {
    contienLabyrinthe.getChildren().clear();
    contienLabyrinthe.getChildren().add(
        renderer.render(labyrinthe)  // View does rendering
    );
}

private void deplacer(int nouveauX, int nouveauY) {
    // ✅ Delegates to Model
    if (jeu.deplacerJoueur(nouveauX, nouveauY)) {
        // ✅ View updates automatically via listener!
        // No manual refresh needed!
        
        if (jeu.estVictoire()) {
            victoire();
        }
    }
}
```

---

### Example 3: New View Layer

#### BEFORE ❌
```java
// No separate View layer!
// Rendering mixed in Controller
```

#### AFTER ✅
```java
// LabyrintheRenderer.java - Dedicated View class!
public class LabyrintheRenderer {
    private final Image imgMur;
    private final Image imgChemin;
    private final Image imgSortie;
    private final Image imgJoueur;
    
    public LabyrintheRenderer() {
        // ✅ View loads its own resources
        this.imgMur = new Image(...);
        this.imgChemin = new Image(...);
        this.imgSortie = new Image(...);
        this.imgJoueur = new Image(...);
    }
    
    public Canvas render(Labyrinthe labyrinthe) {
        Cellule[][] cellules = labyrinthe.getCellules();
        Canvas canvas = new Canvas(...);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        // ✅ ALL rendering logic in View
        for (int i = 0; i < cellules.length; i++) {
            for (int j = 0; j < cellules[i].length; j++) {
                renderCell(gc, cellules[i][j], i, j, 
                    labyrinthe.getJoueurX(), 
                    labyrinthe.getJoueurY());
            }
        }
        return canvas;
    }
    
    private void renderCell(...) {
        // Drawing implementation
    }
}
```

---

## Movement Flow Comparison

### BEFORE: Manual Updates ❌

```
1. User clicks "Haut" button
   ↓
2. JeuControleur.deplacerHaut()
   ↓
3. labyrinthe.setJoueurX(newX)
   ↓
4. Controller MANUALLY calls afficherLabyrinthe()
   ↓
5. Controller DRAWS canvas with rendering logic
   ↓
6. User sees update

Problems:
- Easy to forget manual refresh
- Controller doing rendering
- Tight coupling
```

### AFTER: Automatic Updates ✅

```
1. User clicks "Haut" button
   ↓
2. JeuControleur.deplacerHaut()
   ↓
3. jeu.deplacerJoueur(newX, newY) ← Delegate to Model
   ↓
4. labyrinthe.setJoueurX(newX)
   ↓
5. joueurXProperty fires change event ← Observable!
   ↓
6. Controller listener triggered AUTOMATICALLY
   ↓
7. afficherLabyrinthe()
   ↓
8. renderer.render(labyrinthe) ← Delegate to View
   ↓
9. User sees update

Benefits:
- ✅ Automatic updates
- ✅ Clear separation
- ✅ Loose coupling
- ✅ Easy to maintain
```

---

## Parameter Passing Comparison

### BEFORE: Confused ❌

```java
// ParametresControleur.java
public void lancerModeLibre() {
    FXMLLoader loader = new FXMLLoader(...);
    Parent jeuView = loader.load();

    // TODO: Enregistrer les valeurs du formulaire quelque part??
    // faire un Jeu.getInstance().setParametres(...) ?? 
    // aucune idée besoin d'aide là dessus  ❌ Comment asking how!

    stage.setScene(new Scene(jeuView, ...));
}
```

**Problem:** Didn't know how to pass parameters!

### AFTER: Clear Flow ✅

```java
// ParametresControleur.java
public void lancerModeLibre() {
    FXMLLoader loader = new FXMLLoader(...);
    Parent jeuView = loader.load();

    // ✅ Get controller reference
    JeuControleur jeuControleur = loader.getController();
    
    // ✅ Create model object
    Joueur joueur = new Joueur("ModeLibre");
    
    // ✅ Pass through proper channel
    jeuControleur.setParametres(
        largeur, hauteur, pourcentageMurs, joueur
    );

    stage.setScene(new Scene(jeuView, ...));
}

// JeuControleur.java
public void setParametres(int largeur, int hauteur, 
                          double pourcentageMurs, Joueur joueur) {
    // ✅ Controller creates Model with parameters
    this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
    this.jeu = new Jeu(labyrinthe, joueur);
    jeu.initialiser();
    
    // ✅ Setup observers
    labyrinthe.joueurXProperty().addListener(...);
}
```

**Solution:** Clear data flow through MVC layers!

---

## Summary Table

| Aspect | BEFORE ❌ | AFTER ✅ |
|--------|----------|----------|
| **Model Purity** | Console I/O in Model | Pure business logic |
| **Observability** | Plain fields | JavaFX Properties |
| **Updates** | Manual refresh calls | Automatic via listeners |
| **Rendering** | In Controller | Separate View class |
| **Separation** | Mixed concerns | Clear boundaries |
| **Testing** | Hard (UI coupled) | Easy (Model isolated) |
| **Flexibility** | Tight coupling | Loose coupling |
| **Maintainability** | Changes affect multiple layers | Changes isolated |
| **Parameter Passing** | Unclear (TODO comment) | Clear flow |
| **Professional** | Student project | Industry standard |

---

## The Big Picture

### BEFORE: Everything Mixed Together ❌
```
[UI Code] ← → [Business Logic] ← → [Display Code]
     ↓              ↓                    ↓
    ALL TANGLED UP - HARD TO MAINTAIN
```

### AFTER: Clean Separation ✅
```
[View]  →  [Controller]  →  [Model]
   ↑             ↓             ↓
   ←── Observable Properties ←─┘

CLEAR BOUNDARIES - EASY TO MAINTAIN
```

---

## Conclusion

**Before:** Mixed concerns, tight coupling, manual updates  
**After:** Clean MVC, loose coupling, automatic updates

Your project is now professionally structured! 🎉
