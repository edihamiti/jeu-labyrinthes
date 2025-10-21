# MVC Refactoring Documentation

## Summary of Changes

This document describes the refactoring performed to properly implement the Model-View-Controller (MVC) pattern in the maze game project.

## Problems Identified

### 1. Model Had Presentation Logic (VIOLATION)
**Files affected:** `modele/Labyrinthe.java`, `modele/Jeu.java`

**Issues:**
- `Labyrinthe.afficherAvecJoueur()` (lines 254-272): Console output in model
- `Labyrinthe.lireTouche()` (lines 287-293): Console input in model  
- `Jeu.main()` and `Jeu.jouer()`: Entire console-based game loop in model

**Why it's wrong:** The Model should contain only business logic and data. It should have NO knowledge of how data is displayed (console, GUI, web, etc.).

### 2. No Observer Pattern (VIOLATION)
**Files affected:** `controleur/JeuControleur.java`

**Issues:**
- Controller manually called `afficherLabyrinthe()` after every model change
- No automatic UI updates when model changes

**Why it's wrong:** Changes to the model should automatically notify the view through an observer mechanism.

### 3. Controller Had Rendering Logic (VIOLATION)
**Files affected:** `controleur/JeuControleur.java`

**Issues:**
- `creerCanvasLabyrinthe()` method (lines 41-64): Canvas rendering logic in controller
- Image loading in controller

**Why it's wrong:** Controllers should coordinate between Model and View, not perform rendering. Rendering is a View concern.

### 4. Model Didn't Use JavaFX Properties (MISSED OPPORTUNITY)
**Files affected:** `modele/Labyrinthe.java`

**Issues:**
- Plain `int joueurX`, `int joueurY`, `boolean jeuEnCours` fields
- No way to observe changes automatically

**Why it's wrong:** JavaFX provides Properties that enable reactive programming and automatic UI updates.

### 5. Poor Parameter Passing (ARCHITECTURAL ISSUE)
**Files affected:** `controleur/modeLibre/ParametresControleur.java`

**Issues:**
- TODO comment asking how to pass parameters to game
- No clear data flow from user input to model

## Solutions Implemented

### 1. ✅ Removed Presentation Logic from Model

**Changes in `modele/Labyrinthe.java`:**
- **REMOVED** `afficherAvecJoueur()` method - Console output moved to separate layer
- **REMOVED** `lireTouche()` method - Input handling belongs in Controller

**Changes in `modele/Jeu.java`:**
- **REMOVED** `main()` method - Console application logic doesn't belong in model
- **REMOVED** `jouer()` method - Game loop is a controller/presentation concern
- **ADDED** `initialiser()` - Pure business logic to set up game
- **ADDED** `deplacerJoueur(int, int)` - Business logic for movement
- **ADDED** `estVictoire()` - Business logic to check win condition
- **ADDED** `terminerPartie()` - Business logic to end game

**Result:** Model now contains ONLY business logic with no knowledge of UI.

### 2. ✅ Implemented Observer Pattern with JavaFX Properties

**Changes in `modele/Labyrinthe.java`:**
```java
// BEFORE (primitive fields)
private int joueurX;
private int joueurY;
private boolean jeuEnCours;

// AFTER (observable properties)
private final IntegerProperty joueurX;
private final IntegerProperty joueurY;
private final BooleanProperty jeuEnCours;
```

**Added property accessor methods:**
```java
public IntegerProperty joueurXProperty() { return joueurX; }
public IntegerProperty joueurYProperty() { return joueurY; }
public BooleanProperty jeuEnCoursProperty() { return jeuEnCours; }
```

**Changes in `controleur/JeuControleur.java`:**
```java
// Setup automatic observers
labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
labyrinthe.joueurYProperty().addListener((obs, oldVal, newVal) -> afficherLabyrinthe());
```

**Result:** View automatically updates when model changes. No manual refresh calls needed!

### 3. ✅ Separated View Layer from Controller

**Created new file:** `vue/LabyrintheRenderer.java`

**Purpose:** Handles ALL rendering logic
- Loads images
- Creates Canvas
- Draws cells, player, walls, etc.

**Changes in `controleur/JeuControleur.java`:**
- **REMOVED** rendering logic
- **REMOVED** image loading
- **ADDED** delegation to `LabyrintheRenderer`

```java
// BEFORE (controller had rendering)
private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
    // 30+ lines of rendering code
}

// AFTER (controller delegates to View)
public void afficherLabyrinthe() {
    contienLabyrinthe.getChildren().clear();
    contienLabyrinthe.getChildren().add(renderer.render(labyrinthe));
}
```

**Result:** Clear separation between Controller (coordination) and View (rendering).

### 4. ✅ Proper Parameter Passing and Data Flow

**Changes in `controleur/modeLibre/ParametresControleur.java`:**
```java
// Load the FXML and get controller
FXMLLoader loader = new FXMLLoader(getClass().getResource("/Jeu.fxml"));
Parent jeuView = loader.load();
controleur.JeuControleur jeuControleur = loader.getController();

// Create player (Model object)
modele.Joueur joueur = new modele.Joueur("ModeLibre");

// Pass parameters through proper MVC flow
jeuControleur.setParametres(largeur, hauteur, pourcentageMurs, joueur);
```

**Added in `controleur/JeuControleur.java`:**
```java
public void setParametres(int largeur, int hauteur, double pourcentageMurs, Joueur joueur) {
    // Controller creates Model with parameters
    this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
    this.jeu = new Jeu(labyrinthe, joueur);
    jeu.initialiser();
    // ... setup observers
}
```

**Result:** Clear data flow: View → Controller → Model

## Current MVC Architecture

### Model (modele/)
**Responsibility:** Business logic and data ONLY

**Files:**
- `Joueur.java` - Player data and validation
- `Labyrinthe.java` - Maze generation, pathfinding, game state
- `Jeu.java` - Game coordination, movement rules, victory detection
- `Cellules/*.java` - Cell types and behaviors
- `Defi.java`, `Etape.java`, etc. - Game configuration

**Key Points:**
- ✅ NO UI code
- ✅ NO console I/O
- ✅ Uses JavaFX Properties for observability
- ✅ Pure business logic

### View (vue/)
**Responsibility:** Rendering and display ONLY

**Files:**
- `LabyrintheRenderer.java` - Renders maze to Canvas
- `*.fxml` files - UI layouts

**Key Points:**
- ✅ NO business logic
- ✅ NO direct model manipulation
- ✅ Only displays data provided to it

### Controller (controleur/)
**Responsibility:** Coordinate Model and View

**Files:**
- `JeuControleur.java` - Game screen controller
- `HomePageControleur.java` - Main menu controller
- `ParametresControleur.java` - Settings controller
- `AppControleur.java` - Application navigation
- etc.

**Key Points:**
- ✅ Receives user input from View
- ✅ Delegates to Model for business logic
- ✅ Observes Model changes via Properties
- ✅ Updates View when Model changes

## Data Flow Diagram

### Correct Flow (Now Implemented)
```
USER ACTION (Click button)
    ↓
VIEW (FXML/JavaFX)
    ↓
CONTROLLER (JeuControleur.deplacerHaut())
    ↓
MODEL (Jeu.deplacerJoueur())
    ↓
MODEL CHANGES (Labyrinthe.setJoueurX())
    ↓
PROPERTY NOTIFICATION (joueurXProperty fires)
    ↓
CONTROLLER LISTENER (afficherLabyrinthe())
    ↓
VIEW UPDATE (LabyrintheRenderer.render())
    ↓
USER SEES CHANGE
```

### What Was Wrong Before
```
USER ACTION → CONTROLLER → MODEL
                ↓
           CONTROLLER manually calls view refresh
                ↓
           CONTROLLER renders (mixing concerns!)
```

## Key Improvements

### 1. Separation of Concerns ✅
Each layer has a single, well-defined responsibility:
- Model = Business logic
- View = Presentation
- Controller = Coordination

### 2. Testability ✅
Model can be tested without UI:
```java
@Test
void testMovement() {
    Labyrinthe lab = new Labyrinthe(10, 10, 20);
    lab.generer();
    Jeu jeu = new Jeu(lab, joueur);
    assertTrue(jeu.deplacerJoueur(1, 1));
}
```

### 3. Flexibility ✅
Easy to add new views (console, web, mobile) without changing Model

### 4. Maintainability ✅
Changes to one layer don't affect others:
- Change rendering? Only modify `LabyrintheRenderer`
- Change game rules? Only modify Model classes
- Change UI layout? Only modify FXML files

### 5. Reactive UI ✅
JavaFX Properties enable automatic UI updates:
- No manual refresh calls needed
- UI always in sync with model
- Less error-prone

## Answers to Key Questions

### Q1: Is our data flow correct (View → Controller → Model → View)?
**Answer:** ✅ YES, NOW IT IS!

**Before:** Controller mixed rendering with logic  
**After:** Clear flow with proper separation

### Q2: Are we handling user input in the right place?
**Answer:** ✅ YES!

User input flows correctly:
1. FXML buttons trigger Controller methods (`@FXML public void deplacerHaut()`)
2. Controller methods delegate to Model (`jeu.deplacerJoueur()`)
3. Model executes business logic
4. Properties notify observers
5. View updates automatically

### Q3: How can we use JavaFX properties and bindings more effectively?
**Answer:** ✅ IMPLEMENTED!

We now use:
- `IntegerProperty` for `joueurX`, `joueurY`
- `BooleanProperty` for `jeuEnCours`
- Property listeners for automatic UI updates

**Example:**
```java
// Model changes trigger automatic updates
labyrinthe.joueurXProperty().addListener((obs, oldVal, newVal) -> {
    afficherLabyrinthe(); // View updates automatically
});
```

## Best Practices Going Forward

### DO ✅
- Keep Model classes free of JavaFX UI imports (except Properties)
- Use JavaFX Properties for all observable model state
- Delegate rendering to View classes
- Let Controllers coordinate, not contain logic
- Test Model independently of UI

### DON'T ❌
- Put System.out.println in Model
- Put business logic in Controllers
- Put rendering logic in Controllers
- Manually refresh view after every model change
- Access Model directly from View

## Files Modified

### Model Layer
- `modele/Labyrinthe.java` - Removed I/O, added Properties
- `modele/Jeu.java` - Removed console loop, added business methods

### View Layer
- **NEW:** `vue/LabyrintheRenderer.java` - Handles all rendering

### Controller Layer
- `controleur/JeuControleur.java` - Removed rendering, added observers
- `controleur/modeLibre/ParametresControleur.java` - Fixed parameter passing

### Tests
- `test/modele/LabyrintheTest.java` - Removed call to deleted method

## Conclusion

The project now follows proper MVC architecture with:
- ✅ Clean separation of concerns
- ✅ Observable model using JavaFX Properties
- ✅ Proper data flow
- ✅ Testable, maintainable code
- ✅ No UI code in Model
- ✅ No business logic in View
- ✅ Controllers that coordinate, not implement

The code is now much more professional, maintainable, and follows industry best practices!
