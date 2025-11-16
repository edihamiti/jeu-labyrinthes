# SOLID Principles & MVC Analysis

## Executive Summary

This document provides a comprehensive analysis of how well the "Jeu Labyrinthes" codebase adheres to the SOLID principles (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) and the MVC (Model-View-Controller) architectural pattern.

**Overall Assessment:**
- The project demonstrates **good MVC separation** with clear `modele`, `vue`, and `controleur` packages
- **Strong adherence** to some SOLID principles (especially OCP and LSP)
- **Areas for improvement** exist in SRP, ISP, and DIP

---

## 1. Single Responsibility Principle (SRP)

> *"A class should have one, and only one, reason to change."*

### ✅ Classes that Adhere Well

#### 1.1 `CalculateurScore`
**Location:** `modele/CalculateurScore.java`

**Strength:** This class has a single, well-defined responsibility: calculating scores based on game challenges and completion time.

```java
public class CalculateurScore {
    public static int calculerScore(Defi defi, Duration duration) {
        // Single responsibility: score calculation logic
    }
}
```

**Why it's good:** The class encapsulates only score calculation logic with time-based penalties. It doesn't handle game state, UI, or persistence.

#### 1.2 `SoundManager`
**Location:** `vue/SoundManager.java`

**Strength:** Dedicated solely to managing sound playback.

```java
public class SoundManager {
    public static void playSound(String soundName) {
        // Single responsibility: sound playback
    }
}
```

**Why it's good:** Clear separation of audio concerns from game logic and rendering.

#### 1.3 `GameTimer`
**Location:** `modele/GameTimer.java`

**Strength:** Focuses exclusively on timing functionality.

**Why it's good:** Encapsulates all timing logic in one place, making it reusable and testable.

#### 1.4 `VisionFactory`
**Location:** `vue/visionsLabyrinthe/VisionFactory.java`

**Strength:** Single responsibility of creating vision strategy instances.

```java
public class VisionFactory {
    private static final Map<Vision, VisionLabyrinthe> strategies = new HashMap<>();
    
    public static VisionLabyrinthe getStrategy(Vision vision) {
        // Factory responsibility: object creation
    }
}
```

### ⚠️ Classes with SRP Violations

#### 1.5 `Jeu` Class
**Location:** `modele/Jeu.java`

**Issues:**
1. **Game state management** (labyrinthe, joueur, modeJeu)
2. **Timer management** (startTimer, endTimer, resetTimer, getDuration)
3. **Persistence operations** (through Sauvegarde instance)
4. **Score calculation coordination** (terminerPartie method)
5. **Player movement logic** (deplacerJoueur)
6. **Console I/O** (Scanner usage in initialiser method)

**Evidence:**
```java
public class Jeu {
    Sauvegarde sauvegarde = new Sauvegarde();  // Persistence concern
    private Joueur joueur;                      // State management
    private Labyrinthe labyrinthe;             // State management
    private GameTimer gameTimer;               // Timing concern
    
    public void initialiser(...) {
        Scanner scanner = new Scanner(System.in);  // I/O concern
        // ... game initialization
    }
    
    public boolean deplacerJoueur(int dx, int dy) {
        gameTimer.startTimer();  // Side effect: timer management
        // ... movement logic
    }
}
```

**Recommendation:**
- Extract `GameSession` class for state management
- Create `GameCoordinator` for orchestrating game flow
- Move I/O operations to appropriate controllers
- Timer management should be delegated completely

**Suggested Refactoring:**
```java
// Separate concerns
public class GameSession {
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private ModeJeu modeJeu;
    private Vision vision;
    private Defi defiEnCours;
}

public class GameCoordinator {
    private GameSession session;
    private GameTimer timer;
    private Sauvegarde sauvegarde;
    
    public void startGame() { }
    public void endGame() { }
}
```

#### 1.6 `Labyrinthe` Class
**Location:** `modele/Labyrinthe.java`

**Issues:**
1. **Maze structure** (cellules array)
2. **Player position** (joueurX, joueurY properties)
3. **Game state** (jeuEnCours property)
4. **Pathfinding coordination** (calculePlusCourtChemin delegates to Pathfinder)

**Evidence:**
```java
public class Labyrinthe {
    private Cellule[][] cellules;           // Structure concern
    private final IntegerProperty joueurX;  // Player state concern
    private final IntegerProperty joueurY;  // Player state concern
    private final BooleanProperty jeuEnCours; // Game flow concern
}
```

**Recommendation:**
- Extract `PlayerPosition` class to manage player coordinates
- Consider separating `GameState` from maze structure
- This would make Labyrinthe focus purely on maze topology

**Suggested Refactoring:**
```java
public class PlayerPosition {
    private final IntegerProperty x;
    private final IntegerProperty y;
    
    public boolean canMoveTo(int newX, int newY, Labyrinthe maze) { }
    public void moveTo(int newX, int newY) { }
}

public class Labyrinthe {
    private Cellule[][] cellules;  // Focus on maze structure only
    // Remove player position and game state
}
```

#### 1.7 `JeuControleur` Class
**Location:** `controleur/JeuControleur.java`

**Issues:**
1. **UI event handling** (keyboard input)
2. **Rendering coordination** (afficherJeu, afficherLabyrinthe, afficherMinimap)
3. **Navigation** (retourMenu, retourModeProgression)
4. **Game parameter configuration** (setParametresLab)
5. **Popup management** (afficherPopupTouches)
6. **Window resizing** (scene property listeners)

**Evidence:**
```java
public class JeuControleur extends Controleur {
    @FXML
    public void initialize() {
        // Event handling
        newScene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP, Z -> deplacerHaut();
                // ...
            }
        });
        
        // Window resize handling
        newScene.widthProperty().addListener(...);
        newScene.heightProperty().addListener(...);
    }
    
    // Rendering responsibilities
    public void afficherJeu() { }
    public void afficherLabyrinthe() { }
    
    // Configuration responsibilities
    public void setParametresLab(...) { }
}
```

**Recommendation:**
- Extract `InputHandler` for keyboard event management
- Create `RenderCoordinator` for display orchestration
- Separate `NavigationManager` for screen transitions
- Use `ConfigurationService` for game setup

#### 1.8 `Sauvegarde` Class
**Location:** `modele/Sauvegarde.java`

**Issues:**
1. **File I/O operations** (reading/writing to disk)
2. **JSON serialization/deserialization**
3. **Player collection management** (HashMap operations)
4. **File system management** (directory creation)

**Evidence:**
```java
public class Sauvegarde {
    private final File FICHIER;
    private final HashMap<String, Joueur> joueurs;
    
    public void sauvegardeJoueurs() {
        // File I/O + JSON serialization
        try (FileWriter fileWriter = new FileWriter(FICHIER)) {
            JSONArray jsonJoueurs = new JSONArray();
            // ...
        }
    }
    
    private void assurerFichierSauvegarde() {
        // File system management
        File parent = FICHIER.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
}
```

**Recommendation:**
- Split into `JoueurRepository` (collection management) and `JoueurPersistence` (I/O operations)
- Introduce `FileSystemService` for file operations
- Use separate `JsonSerializer` for JSON handling

---

## 2. Open/Closed Principle (OCP)

> *"Software entities should be open for extension, but closed for modification."*

### ✅ Excellent Adherence

#### 2.1 `Cellule` Abstract Class Hierarchy
**Location:** `modele/Cellules/`

**Strength:** Perfect example of OCP with an abstract base class and concrete implementations.

```java
public abstract class Cellule {
    public abstract Image getTexture();
    
    public boolean estPiege() { return false; }
    public boolean estChemin() { return false; }
    public boolean estMur() { return false; }
    // ... extensible through inheritance
}

// Extensions (all closed for modification):
public class Mur extends Cellule { }
public class Chemin extends Cellule { }
public class Entree extends Cellule { }
public class Sortie extends Cellule { }
public class Piege extends Cellule { }
```

**Why it's excellent:**
- New cell types can be added without modifying existing code
- Each cell type overrides only what's necessary
- Default implementations in base class provide sensible defaults
- Easy to add new cell behaviors (e.g., `TeleportCell`, `KeyCell`, `DoorCell`)

**Extension example (no modification needed):**
```java
// Can add new cell types without changing Cellule or other cells
public class TeleportCell extends Cellule {
    @Override
    public Image getTexture() { return teleportTexture; }
    
    @Override
    public boolean estTeleport() { return true; }
}
```

#### 2.2 `GenerateurLabyrinthe` Strategy Pattern
**Location:** `modele/generateurs/`

**Strength:** Abstract base class allowing different maze generation algorithms.

```java
public abstract class GenerateurLabyrinthe {
    int largeur;
    int hauteur;
    
    public abstract void generer(Labyrinthe lab);
}

// Concrete implementations:
public class GenerateurAleatoire extends GenerateurLabyrinthe { }
public class GenerateurParfait extends GenerateurLabyrinthe { }
```

**Why it's good:**
- New generation algorithms can be added without modifying existing ones
- Each generator encapsulates its own algorithm
- Easy to add: `GenerateurRecursiveBacktracker`, `GenerateurKruskal`, etc.

#### 2.3 Vision Strategy Pattern
**Location:** `vue/visionsLabyrinthe/`

**Strength:** Excellent use of Strategy pattern with factory.

```java
public abstract class VisionLabyrinthe {
    public abstract Rendu createRendu(Labyrinthe labyrinthe, VBox container);
    public abstract Rendu createMinimapRendu(Labyrinthe labyrinthe, VBox container);
    public abstract boolean requiresMinimap();
}

// Implementations:
public class VisionLibre extends VisionLabyrinthe { }
public class VisionLocale extends VisionLabyrinthe { }
public class VisionLimitee extends VisionLabyrinthe { }
public class VisionCarte extends VisionLabyrinthe { }
```

**Why it's excellent:**
- New vision modes can be added without changing existing code
- Factory pattern (`VisionFactory`) centralizes creation
- Each vision strategy is independent
- Easy to add new modes: `VisionNightMode`, `VisionXRay`, etc.

#### 2.4 Enum with Behavior: `TypeLabyrinthe`
**Location:** `modele/TypeLabyrinthe.java`

**Assumption:** Based on usage in `JeuControleur`, this likely uses enum with methods.

**Strength:** If implemented correctly, enums with methods provide OCP compliance.

**Expected structure:**
```java
public enum TypeLabyrinthe {
    ALEATOIRE {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int l, int h, double p, int d) {
            return new GenerateurAleatoire(l, h, p, d);
        }
    },
    PARFAIT {
        @Override
        public GenerateurLabyrinthe creerGenerateur(int l, int h, double p, int d) {
            return new GenerateurParfait(l, h, p, d);
        }
    };
    
    public abstract GenerateurLabyrinthe creerGenerateur(int l, int h, double p, int d);
}
```

### ⚠️ Areas for Improvement

#### 2.5 `Rendu` Interface
**Location:** `vue/Rendu.java`

**Current limitation:**
The `Rendu` interface likely has static image fields that all implementations share:

```java
public interface Rendu {
    Canvas rendu(Labyrinthe labyrinthe);
    // Likely has static fields for images
}
```

**Recommendation:**
- Move image loading to a separate `TextureManager` or `AssetLoader`
- Make `Rendu` purely behavioral
- This would make it truly open for extension

---

## 3. Liskov Substitution Principle (LSP)

> *"Derived classes must be substitutable for their base classes."*

### ✅ Strong Adherence

#### 3.1 `Cellule` Hierarchy
**Location:** `modele/Cellules/`

**Strength:** All cell types can be used interchangeably through the `Cellule` interface.

**Evidence:**
```java
// In Labyrinthe class
private Cellule[][] cellules;

// Any Cellule subclass can be used:
cellules[i][j] = new Mur();
cellules[i][j] = new Chemin();
cellules[i][j] = new Entree();
// All support the same interface
```

**Why it works:**
- All subtypes maintain the contract of `Cellule`
- Methods like `estMur()`, `estChemin()` have consistent behavior
- No unexpected side effects when substituting one cell type for another
- Preconditions and postconditions are preserved

#### 3.2 `GenerateurLabyrinthe` Hierarchy

**Strength:** All generators can be substituted for each other.

**Evidence:**
```java
GenerateurLabyrinthe generateur = typeLab.creerGenerateur(...);
generateur.generer(jeu.getLabyrinthe());
// Works with any GenerateurLabyrinthe subclass
```

**Why it works:**
- All generators follow the same contract: `generer(Labyrinthe lab)`
- No generator violates the expectations of its base class
- Client code doesn't need to know which specific generator is being used

#### 3.3 `VisionLabyrinthe` Strategy

**Strength:** All vision strategies are perfectly substitutable.

**Evidence:**
```java
VisionLabyrinthe visionStrategy = VisionFactory.getStrategy(vision);
Rendu rendu = visionStrategy.createRendu(labyrinthe, container);
// Any vision strategy can be used interchangeably
```

**Why it works:**
- All vision strategies provide the same interface
- Behavior varies but contract remains consistent
- No unexpected exceptions or requirements

#### 3.4 `Controleur` Base Class

**Strength:** All controller subclasses maintain the base class contract.

```java
public abstract class Controleur {
    protected Jeu jeu;
    protected AppControleur appControleur;
    
    public void setJeu(Jeu jeu) { this.jeu = jeu; }
    public void setAppControleur(AppControleur appControleur) { 
        this.appControleur = appControleur; 
    }
}

// All subclasses (JeuControleur, HomePageControleur, etc.) 
// can be used wherever Controleur is expected
```

### ⚠️ Potential Violations

#### 3.5 `Joueur` Constructor Inconsistency

**Location:** `modele/Joueur.java`

**Issue:** Two constructors with different behaviors could violate LSP if used polymorphically.

```java
public class Joueur {
    // Constructor 1: Validates pseudo, can throw exception
    public Joueur(String pseudo) throws PseudoException {
        if (pseudo == null || pseudo.trim().isEmpty())
            throw new PseudoException("...");
        // ...
    }
    
    // Constructor 2: Loads from JSON, different validation logic
    public Joueur(JSONObject joueur) {
        this.id = joueur.getInt("id");  // Could throw if missing
        this.pseudo = joueur.getString("pseudo");  // No validation
        // ...
    }
}
```

**Problem:**
- Constructor from JSON doesn't validate pseudo
- Creates joueurs with potentially invalid state
- If a method expects validated joueurs, JSON-loaded ones might break assumptions

**Recommendation:**
- Ensure both constructors validate pseudo consistently
- Or use factory methods with clear contracts:
  ```java
  public static Joueur creerNouveauJoueur(String pseudo) throws PseudoException
  public static Joueur chargerDepuisJson(JSONObject json) throws PseudoException
  ```

---

## 4. Interface Segregation Principle (ISP)

> *"Clients should not be forced to depend on interfaces they don't use."*

### ✅ Good Examples

#### 4.1 `Rendu` Interface
**Location:** `vue/Rendu.java`

**Strength:** Minimal, focused interface.

```java
public interface Rendu {
    Canvas rendu(Labyrinthe labyrinthe);
}
```

**Why it's good:**
- Single method interface (functional interface)
- Implementers only need to provide rendering logic
- No forced dependencies on unneeded methods

#### 4.2 Specialized Vision Strategies

**Strength:** Each vision strategy has appropriate, minimal methods.

**Why it's good:**
- Vision strategies only implement what they need
- `requiresMinimap()` allows strategies to opt-in to minimap functionality
- No forced implementation of unused methods

### ⚠️ Violations and Issues

#### 4.3 `Cellule` Abstract Class
**Location:** `modele/Cellules/Cellule.java`

**Issue:** Contains methods that most subclasses don't need.

```java
public abstract class Cellule {
    private int x;  // Coordinates not always needed
    private int y;
    private Image textureMur;  // Only relevant for Mur?
    
    public abstract Image getTexture();
    
    // All these default to false - most cells override only one
    public boolean estPiege() { return false; }
    public boolean estChemin() { return false; }
    public boolean estMur() { return false; }
    public boolean estEntree() { return false; }
    public boolean estSortie() { return false; }
}
```

**Problems:**
1. **Fat interface**: Too many type-checking methods (estPiege, estChemin, etc.)
2. **Unused fields**: Most cells don't need x, y coordinates stored
3. **Violation of ISP**: Clients using Cellule are exposed to methods they don't need

**Evidence of violation:**
```java
// In Labyrinthe.peutDeplacer(), only need to check:
if (cellules[x][y] == null || cellules[x][y].estMur()) {
    return false;
}
// But Cellule exposes 5+ type-checking methods
```

**Recommendation:**

**Option 1: Visitor Pattern**
```java
public interface Cellule {
    Image getTexture();
    <T> T accept(CelluleVisitor<T> visitor);
}

public interface CelluleVisitor<T> {
    T visitMur(Mur mur);
    T visitChemin(Chemin chemin);
    T visitEntree(Entree entree);
    T visitSortie(Sortie sortie);
}

// Usage
boolean isWalkable = cellule.accept(new WalkabilityVisitor());
```

**Option 2: Marker Interfaces**
```java
public interface Cellule {
    Image getTexture();
}

public interface Walkable { }  // Marker interface
public interface Blocking { }  // Marker interface
public interface Goal { }      // Marker interface

public class Mur extends Cellule implements Blocking { }
public class Chemin extends Cellule implements Walkable { }
public class Sortie extends Cellule implements Walkable, Goal { }

// Usage
if (cellule instanceof Blocking) { return false; }
```

**Option 3: Single Type Method**
```java
public enum CelluleType { MUR, CHEMIN, ENTREE, SORTIE, PIEGE }

public abstract class Cellule {
    public abstract Image getTexture();
    public abstract CelluleType getType();
}

// Usage
if (cellule.getType() == CelluleType.MUR) { return false; }
```

#### 4.4 Potential Issue: `Controleur` Base Class

**Location:** `controleur/Controleur.java`

**Current state:**
```java
public abstract class Controleur {
    protected Jeu jeu;
    protected AppControleur appControleur;
    
    public void setJeu(Jeu jeu) { this.jeu = jeu; }
    public void setAppControleur(AppControleur appControleur) { 
        this.appControleur = appControleur; 
    }
}
```

**Potential issue:**
- All controllers inherit both `jeu` and `appControleur`
- Some controllers might not need both
- Example: `PopupCommandesControleur` probably doesn't need full game state

**Recommendation:**
- Consider splitting into more focused base classes:
  ```java
  public interface AppAware {
      void setAppControleur(AppControleur app);
  }
  
  public interface GameAware {
      void setJeu(Jeu jeu);
  }
  
  // Implement only what's needed
  public class JeuControleur implements AppAware, GameAware { }
  public class PopupCommandesControleur implements AppAware { }
  ```

#### 4.5 `VisionLabyrinthe` Potential Segregation Issue

**Current assumption:**
```java
public abstract class VisionLabyrinthe {
    public abstract Rendu createRendu(Labyrinthe labyrinthe, VBox container);
    public abstract Rendu createMinimapRendu(Labyrinthe labyrinthe, VBox container);
    public abstract boolean requiresMinimap();
}
```

**Issue:**
- All vision strategies must implement `createMinimapRendu()` even if they don't use minimap
- Better to segregate:

```java
public interface VisionStrategy {
    Rendu createRendu(Labyrinthe labyrinthe, VBox container);
}

public interface MinimapVisionStrategy extends VisionStrategy {
    Rendu createMinimapRendu(Labyrinthe labyrinthe, VBox container);
}

// Then:
public class VisionLibre implements VisionStrategy { }
public class VisionLimitee implements MinimapVisionStrategy { }
```

---

## 5. Dependency Inversion Principle (DIP)

> *"High-level modules should not depend on low-level modules. Both should depend on abstractions."*

### ✅ Good Examples

#### 5.1 Generator Abstraction
**Location:** `modele/generateurs/`

**Strength:** High-level code depends on `GenerateurLabyrinthe` abstraction, not concrete generators.

```java
// High-level (JeuControleur) depends on abstraction
private GenerateurLabyrinthe generateur;

public void setParametresLab(..., TypeLabyrinthe typeLab) {
    this.generateur = typeLab.creerGenerateur(...);  // Factory creates concrete
    this.generateur.generer(jeu.getLabyrinthe());    // Works with abstraction
}
```

**Why it's good:**
- Controller doesn't know about `GenerateurAleatoire` or `GenerateurParfait`
- Easy to add new generators without changing high-level code
- Dependency flows from concrete to abstract

#### 5.2 Vision Strategy Pattern
**Location:** `vue/visionsLabyrinthe/`

**Strength:** Controllers and renderers depend on abstractions.

```java
// High-level code
VisionLabyrinthe visionStrategy = VisionFactory.getStrategy(vision);
Rendu rendu = visionStrategy.createRendu(labyrinthe, container);
```

**Why it's good:**
- `JeuControleur` doesn't depend on specific vision implementations
- `VisionFactory` handles creation details
- New vision modes don't affect high-level code

#### 5.3 `Rendu` Interface

**Strength:** Rendering abstraction separates view logic from controllers.

```java
// Controller depends on abstraction
private Rendu renduLabyrinthe;

public void afficherLabyrinthe() {
    conteneurLabyrinthe.getChildren().add(renduLabyrinthe.rendu(jeu.getLabyrinthe()));
}
```

### ⚠️ Violations and Issues

#### 5.4 Direct `Sauvegarde` Instantiation in `Jeu`
**Location:** `modele/Jeu.java`

**Violation:**
```java
public class Jeu {
    Sauvegarde sauvegarde = new Sauvegarde();  // Direct instantiation of concrete class
    
    public void terminerPartie(boolean victoire) {
        // ...
        this.sauvegarde.sauvegardeJoueurs();  // Direct dependency
    }
}
```

**Problems:**
1. `Jeu` (high-level) directly depends on `Sauvegarde` (low-level)
2. Hard to test `Jeu` without file system access
3. Hard to swap persistence mechanisms (database, cloud, etc.)
4. Violates DIP - should depend on abstraction

**Recommendation:**
```java
// Define abstraction
public interface JoueurRepository {
    void sauvegarder(Joueur joueur);
    Joueur charger(String pseudo) throws PseudoException;
    Collection<Joueur> chargerTous();
}

// Concrete implementation
public class JsonJoueurRepository implements JoueurRepository {
    private final File fichier;
    // Implementation with file I/O
}

// Usage in Jeu (dependency injection)
public class Jeu {
    private JoueurRepository repository;  // Abstraction
    
    public Jeu(JoueurRepository repository) {
        this.repository = repository;
    }
}

// Benefits:
// - Easy to test with mock repository
// - Can switch to database: new DatabaseJoueurRepository()
// - Can add caching: new CachedJoueurRepository(jsonRepo)
```

#### 5.5 Direct `Pathfinder` Instantiation
**Location:** `modele/Labyrinthe.java`

**Violation:**
```java
public int calculePlusCourtChemin() {
    Pathfinder pathfinder = new Pathfinder();  // Direct instantiation
    return pathfinder.findShortestPath(cellules, largeurMax, hauteurMax);
}
```

**Problems:**
1. `Labyrinthe` directly depends on concrete `Pathfinder`
2. Can't use different pathfinding algorithms (A*, Dijkstra, etc.)
3. Hard to test with mock pathfinder

**Recommendation:**
```java
public interface PathfindingStrategy {
    int findShortestPath(Cellule[][] cellules, int width, int height);
}

public class BFSPathfinder implements PathfindingStrategy { }
public class AStarPathfinder implements PathfindingStrategy { }

public class Labyrinthe {
    private PathfindingStrategy pathfinder;
    
    public Labyrinthe(..., PathfindingStrategy pathfinder) {
        this.pathfinder = pathfinder;
    }
    
    public int calculePlusCourtChemin() {
        return pathfinder.findShortestPath(cellules, largeurMax, hauteurMax);
    }
}
```

#### 5.6 `SoundManager` Static Method
**Location:** `vue/SoundManager.java`

**Violation:**
```java
public class SoundManager {
    public static void playSound(String soundName) {
        // Direct access to file system and audio hardware
    }
}

// Usage in JeuControleur
SoundManager.playSound("move.mp3");  // Static dependency
```

**Problems:**
1. Controllers directly depend on concrete `SoundManager`
2. Impossible to test without actual audio system
3. Can't disable sounds easily
4. Can't mock for testing
5. Static methods create tight coupling

**Recommendation:**
```java
public interface AudioService {
    void playSound(String soundName);
    void setVolume(double volume);
    void mute();
}

public class JavaFXAudioService implements AudioService {
    @Override
    public void playSound(String soundName) {
        AudioClip audio = new AudioClip(
            getClass().getResource("/sounds/" + soundName).toExternalForm()
        );
        audio.play();
    }
}

// For testing
public class NoOpAudioService implements AudioService {
    @Override
    public void playSound(String soundName) {
        // Do nothing - silent
    }
}

// Usage with dependency injection
public class JeuControleur {
    private AudioService audioService;
    
    public JeuControleur(AudioService audioService) {
        this.audioService = audioService;
    }
    
    private void deplacer(...) {
        audioService.playSound("move.mp3");
    }
}
```

#### 5.7 `CalculateurScore` Static Method

**Similar issue to `SoundManager`:**
```java
int scoreObtenu = CalculateurScore.calculerScore(defiEnCours, gameTimer.getDuration());
```

**Recommendation:**
```java
public interface ScoreCalculator {
    int calculateScore(Defi defi, Duration duration);
}

public class TimeBasedScoreCalculator implements ScoreCalculator {
    @Override
    public int calculateScore(Defi defi, Duration duration) {
        // Current logic
    }
}

// Could easily add:
public class DifficultyBasedScoreCalculator implements ScoreCalculator { }
public class ComboScoreCalculator implements ScoreCalculator { }
```

#### 5.8 JavaFX Properties Coupling
**Location:** `modele/Labyrinthe.java`

**Issue:** Domain model depends on UI framework.

```java
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.BooleanProperty;

public class Labyrinthe {
    private final IntegerProperty joueurX;  // JavaFX dependency in model
    private final IntegerProperty joueurY;
    private final BooleanProperty jeuEnCours;
}
```

**Problems:**
1. Model layer (`modele/`) depends on View layer (JavaFX)
2. Can't use this model outside JavaFX (e.g., console app, Android)
3. Makes testing harder
4. Violates clean architecture principles

**Recommendation:**

**Option 1: Observer Pattern**
```java
public interface PropertyChangeListener {
    void onPropertyChanged(String propertyName, Object oldValue, Object newValue);
}

public class Labyrinthe {
    private int joueurX;
    private int joueurY;
    private boolean jeuEnCours;
    private List<PropertyChangeListener> listeners = new ArrayList<>();
    
    public void setJoueurX(int x) {
        int oldX = this.joueurX;
        this.joueurX = x;
        notifyListeners("joueurX", oldX, x);
    }
}
```

**Option 2: Adapter Pattern**
```java
// Pure domain model
public class Labyrinthe {
    private int joueurX;
    private int joueurY;
    // No JavaFX dependencies
}

// Adapter in view layer
public class JavaFXLabyrintheAdapter {
    private Labyrinthe labyrinthe;
    private IntegerProperty joueurXProperty;
    private IntegerProperty joueurYProperty;
    
    public JavaFXLabyrintheAdapter(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.joueurXProperty = new SimpleIntegerProperty(labyrinthe.getJoueurX());
        // Bind adapter to model
    }
}
```

---

## 6. MVC Pattern Adherence

### ✅ Strong Points

#### 6.1 Clear Package Separation

**Strength:** The project has excellent physical separation of MVC concerns.

```
src/main/java/
├── modele/          # Model layer
├── vue/             # View layer
└── controleur/      # Controller layer
```

**Why it's good:**
- Easy to navigate
- Clear responsibility boundaries
- New developers can quickly understand structure

#### 6.2 Model Layer (modele/)

**Good aspects:**
- Domain entities: `Joueur`, `Jeu`, `Labyrinthe`, `Defi`
- Business logic: `CalculateurScore`, `GameTimer`
- Data structures: `Cellule` hierarchy
- Algorithms: `Pathfinder`, generator strategies

**Domain-focused:** Most model classes represent game concepts well.

#### 6.3 View Layer (vue/)

**Good aspects:**
- Rendering components: `LabyrintheRendu`, `MiniMapRendu`
- Vision strategies: `VisionLibre`, `VisionLocale`, etc.
- UI utilities: `SoundManager`, `HandlerVictoire`

**Presentation-focused:** Views handle visual representation.

#### 6.4 Controller Layer (controleur/)

**Good aspects:**
- Screen controllers: `JeuControleur`, `HomePageControleur`
- Base abstraction: `Controleur`
- Application coordinator: `AppControleur`

**Mediation:** Controllers connect views to models.

### ⚠️ MVC Violations

#### 6.5 View Dependencies in Model

**Violation:** `Labyrinthe` depends on JavaFX properties.

```java
// In modele/Labyrinthe.java
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Labyrinthe {
    private final IntegerProperty joueurX;  // View framework in Model
}
```

**Impact:**
- **Breaks MVC separation**: Model should be independent of View technology
- **Testing difficulty**: Can't test model without JavaFX runtime
- **Portability**: Can't reuse model in non-JavaFX applications

**Recommendation:** Use Observer pattern or pure Java events (as discussed in DIP section).

#### 6.6 Model Logic in Controller

**Violation:** `JeuControleur.setParametresLab()` contains model initialization logic.

```java
// In controleur/JeuControleur.java
public void setParametresLab(int largeur, int hauteur, double pourcentageMurs, 
                             int distanceMin, TypeLabyrinthe typeLab) {
    jeu.setLabyrinthe(new Labyrinthe(largeur, hauteur, pourcentageMurs, distanceMin));
    this.generateur = typeLab.creerGenerateur(largeur, hauteur, pourcentageMurs, distanceMin);
    this.generateur.generer(jeu.getLabyrinthe());
    jeu.resetTimer();
    setRenduLabyrinthe();
    // ...
}
```

**Issues:**
- Controller orchestrates low-level model creation
- Business logic (maze generation) triggered from controller
- Should be delegated to model layer

**Recommendation:**
```java
// In modele/Jeu.java (Model layer)
public void initialiserLabyrinthe(int largeur, int hauteur, double pourcentageMurs,
                                 int distanceMin, TypeLabyrinthe typeLab) {
    this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs, distanceMin);
    GenerateurLabyrinthe generateur = typeLab.creerGenerateur(...);
    generateur.generer(this.labyrinthe);
    resetTimer();
}

// In controleur/JeuControleur.java (Controller layer)
public void setParametresLab(int largeur, int hauteur, double pourcentageMurs,
                            int distanceMin, TypeLabyrinthe typeLab) {
    jeu.initialiserLabyrinthe(largeur, hauteur, pourcentageMurs, distanceMin, typeLab);
    setRenduLabyrinthe();  // Only UI configuration in controller
    afficherJeu();
}
```

#### 6.7 Business Logic in View

**Violation:** `LabyrintheRendu` contains rendering calculations that could be model concerns.

```java
// In vue/LabyrintheRendu.java
private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
    // Complex calculation logic
    int tailleCelluleH = (int) (heightVBox / largeurMax);
    int tailleCelluleW = (int) (widthVBox / hauteurMax);
    int tailleCellule = Math.min(tailleCelluleH, tailleCelluleW);
    
    if (tailleCellule < 10) {
        tailleCellule = 10;
    }
    // ...
}
```

**Issue:** While this is rendering logic (appropriate for View), it's mixing concerns:
- Cell size calculation (could be reusable)
- Canvas creation (view-specific)
- Iteration over maze (data access)

**Recommendation:**
```java
// Extract to separate concern
public class CellSizeCalculator {
    public int calculateOptimalCellSize(int mazeWidth, int mazeHeight, 
                                       double viewWidth, double viewHeight) {
        int cellH = (int) (viewHeight / mazeWidth);
        int cellW = (int) (viewWidth / mazeHeight);
        return Math.max(10, Math.min(cellH, cellW));
    }
}

// Use in renderer
private Canvas creerCanvasLabyrinthe(Cellule[][] labyrinthe) {
    int tailleCellule = cellSizeCalculator.calculateOptimalCellSize(...);
    // Focus on drawing
}
```

#### 6.8 Console I/O in Model

**Violation:** `Jeu.initialiser()` uses `Scanner` for input.

```java
// In modele/Jeu.java
public void initialiser(...) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Entrez le pseudo du joueur : ");
    this.joueur = new Joueur(scanner.nextLine());
    // ...
}
```

**Problems:**
- Model shouldn't handle I/O
- Makes model untestable
- Assumes console interface
- Breaks MVC - this is View responsibility

**Recommendation:**
```java
// Remove I/O from model
public void initialiser(String pseudo, int largeur, int hauteur, ...) 
        throws PseudoException {
    this.joueur = new Joueur(pseudo);
    this.labyrinthe = new Labyrinthe(largeur, hauteur, pourcentageMurs);
    // ...
}

// Handle I/O in Controller or View
```

#### 6.9 `Sauvegarde` Belongs in Model but Acts Like Repository

**Observation:** `Sauvegarde` is in `modele/` but handles persistence.

**Discussion:**
- **Current placement:** `modele/Sauvegarde.java`
- **Concern:** Persistence/Infrastructure, not pure business logic
- **Better architecture:** Separate infrastructure layer or repository pattern

**Recommendation:**
```
src/main/java/
├── modele/              # Pure domain
│   ├── Joueur.java
│   └── Jeu.java
├── repository/          # Persistence
│   ├── JoueurRepository.java (interface)
│   └── JsonJoueurRepository.java (implementation)
├── vue/                 # Presentation
└── controleur/          # Application flow
```

### 📊 MVC Summary

| Aspect | Rating | Notes |
|--------|--------|-------|
| Package structure | ✅ Excellent | Clear separation of concerns |
| Model independence | ⚠️ Needs improvement | JavaFX dependencies in model |
| View responsibility | ✅ Good | Focuses on presentation |
| Controller role | ⚠️ Mixed | Some business logic in controllers |
| Separation of concerns | ⚠️ Good overall | Some violations to address |

---

## 7. Additional Architecture Observations

### 7.1 Factory Pattern Usage

**Positive:** The codebase uses factory pattern effectively:
- `VisionFactory` for vision strategies
- `TypeLabyrinthe` enum likely acts as factory for generators
- Clean object creation separation

### 7.2 Strategy Pattern Usage

**Positive:** Excellent use of strategy pattern:
- `GenerateurLabyrinthe` strategies for maze generation
- `VisionLabyrinthe` strategies for different view modes
- Easy to extend with new strategies

### 7.3 Static Utility Classes

**Concern:** Several utility classes use static methods:
- `SoundManager.playSound()`
- `CalculateurScore.calculerScore()`

**Impact:** Creates tight coupling, difficult to test, violates DIP.

**Recommendation:** Convert to services with interfaces (as detailed in DIP section).

### 7.4 Enum with Behavior

**Positive:** If `TypeLabyrinthe` and `Vision` enums have methods, this is good design:
```java
// Assumption based on usage
public enum TypeLabyrinthe {
    ALEATOIRE, PARFAIT;
    
    public GenerateurLabyrinthe creerGenerateur(...) {
        // Factory method
    }
}
```

This encapsulates creation logic and avoids switch statements.

### 7.5 Exception Handling

**Observation:** Custom exception `PseudoException` for domain-specific errors.

**Positive:**
- Domain-specific exceptions improve error handling
- Better than generic exceptions

**Could improve:**
- Consider exception hierarchy for different domain errors
- Example: `InvalidConfigurationException`, `GameStateException`, etc.

### 7.6 Testing Structure

**Observation:** Tests are well-organized by package:
```
src/test/java/
├── modele/
│   ├── JeuTest.java
│   ├── LabyrintheTest.java
│   └── Cellules/
└── JoueurTest.java
```

**Positive:** Mirrors production structure, easy to find tests.

---

## 8. Summary and Recommendations

### 8.1 SOLID Principles Summary

| Principle | Rating | Key Issues | Priority |
|-----------|--------|-----------|----------|
| **SRP** | ⚠️ Moderate | `Jeu`, `JeuControleur`, `Labyrinthe` have multiple responsibilities | High |
| **OCP** | ✅ Good | Excellent use of inheritance and strategy pattern | Low |
| **LSP** | ✅ Good | All hierarchies maintain proper substitutability | Low |
| **ISP** | ⚠️ Moderate | `Cellule` has too many methods, some controllers might have unnecessary dependencies | Medium |
| **DIP** | ⚠️ Needs Work | Many direct instantiations, static methods, JavaFX in model | High |

### 8.2 MVC Pattern Summary

| Aspect | Rating | Key Issues | Priority |
|--------|--------|-----------|----------|
| **Package Structure** | ✅ Excellent | Clear separation | - |
| **Model Purity** | ⚠️ Needs Work | JavaFX dependencies, I/O in model | High |
| **View Focus** | ✅ Good | Proper presentation layer | Low |
| **Controller Role** | ⚠️ Moderate | Some business logic in controllers | Medium |

### 8.3 Priority Refactoring Recommendations

#### 🔴 High Priority

1. **Remove JavaFX dependencies from Model layer**
   - Impact: Improves testability, portability, MVC compliance
   - Classes: `Labyrinthe`, any model classes using JavaFX properties
   - Solution: Use Observer pattern or pure Java events

2. **Introduce Dependency Injection for services**
   - Impact: Improves testability, DIP compliance, flexibility
   - Classes: `Jeu` (Sauvegarde), `JeuControleur` (SoundManager)
   - Solution: Constructor injection with interfaces

3. **Extract responsibilities from `Jeu` class**
   - Impact: Improves SRP, testability, maintainability
   - Current issues: Timer management, I/O, state management mixed
   - Solution: Create `GameSession`, `GameCoordinator` classes

#### 🟡 Medium Priority

4. **Refactor `Cellule` interface**
   - Impact: Improves ISP, reduces coupling
   - Current issues: Too many type-checking methods
   - Solution: Visitor pattern or marker interfaces

5. **Move business logic from controllers to model**
   - Impact: Improves MVC compliance, reusability
   - Classes: `JeuControleur.setParametresLab()`
   - Solution: Delegate to model layer methods

6. **Replace static utility methods with services**
   - Impact: Improves DIP, testability
   - Classes: `SoundManager`, `CalculateurScore`
   - Solution: Interface-based services with dependency injection

#### 🟢 Low Priority (Nice to have)

7. **Further separate `Sauvegarde` responsibilities**
   - Current: Mix of repository and persistence concerns
   - Solution: Split into `JoueurRepository` interface and implementation

8. **Consider adding abstraction for `Pathfinder`**
   - Current: Direct instantiation in `Labyrinthe`
   - Solution: `PathfindingStrategy` interface

### 8.4 Positive Aspects to Maintain

✅ **Keep doing these well:**
- Strategy pattern for generators and vision modes
- Factory pattern usage
- Abstract class hierarchies (Cellule, GenerateurLabyrinthe)
- Package structure organization
- Test organization

### 8.5 Long-term Architecture Suggestions

For future enhancements, consider:

1. **Introduce a service layer**
   ```
   src/main/java/
   ├── modele/        # Pure domain
   ├── service/       # Business services
   ├── repository/    # Data access
   ├── vue/           # Presentation
   └── controleur/    # Application flow
   ```

2. **Use dependency injection framework**
   - Consider lightweight DI (e.g., Google Guice, Spring)
   - Or implement manual DI with factory classes

3. **Event-driven architecture**
   - Use event bus for model-view communication
   - Reduces direct dependencies
   - Example: `GameEventBus` with `PlayerMovedEvent`, `GameWonEvent`, etc.

4. **Configuration management**
   - Extract hardcoded values to configuration
   - Make game parameters configurable

---

## 9. Code Examples: Before and After

### Example 1: Dependency Injection in `Jeu`

**❌ Before (Current):**
```java
public class Jeu {
    Sauvegarde sauvegarde = new Sauvegarde();  // Tight coupling
    
    public void terminerPartie(boolean victoire) {
        // ...
        if (victoire && this.joueur != null && this.defiEnCours != null) {
            int scoreObtenu = CalculateurScore.calculerScore(defiEnCours, gameTimer.getDuration());
            this.joueur.ajouterScore(scoreObtenu, defiEnCours);
            this.sauvegarde.sauvegardeJoueurs();  // Direct dependency
        }
    }
}
```

**✅ After (Improved):**
```java
public interface JoueurRepository {
    void sauvegarder(Joueur joueur);
    Joueur charger(String pseudo) throws PseudoException;
}

public interface ScoreCalculator {
    int calculerScore(Defi defi, Duration duration);
}

public class Jeu {
    private final JoueurRepository repository;
    private final ScoreCalculator scoreCalculator;
    
    // Dependency injection via constructor
    public Jeu(JoueurRepository repository, ScoreCalculator scoreCalculator) {
        this.repository = repository;
        this.scoreCalculator = scoreCalculator;
    }
    
    public void terminerPartie(boolean victoire) {
        // ...
        if (victoire && this.joueur != null && this.defiEnCours != null) {
            int scoreObtenu = scoreCalculator.calculerScore(defiEnCours, gameTimer.getDuration());
            this.joueur.ajouterScore(scoreObtenu, defiEnCours);
            this.repository.sauvegarder(joueur);
        }
    }
}

// Testing becomes easy:
@Test
public void testTerminerPartie() {
    JoueurRepository mockRepo = mock(JoueurRepository.class);
    ScoreCalculator mockCalc = mock(ScoreCalculator.class);
    Jeu jeu = new Jeu(mockRepo, mockCalc);
    // Test without file system or actual calculation
}
```

### Example 2: Removing JavaFX from Model

**❌ Before (Current):**
```java
// In modele/Labyrinthe.java
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Labyrinthe {
    private final IntegerProperty joueurX;
    private final IntegerProperty joueurY;
    
    public Labyrinthe(...) {
        this.joueurX = new SimpleIntegerProperty(0);
        this.joueurY = new SimpleIntegerProperty(1);
    }
    
    public IntegerProperty joueurXProperty() {
        return joueurX;
    }
}
```

**✅ After (Improved):**
```java
// In modele/Labyrinthe.java (Pure Java)
public class Labyrinthe {
    private int joueurX;
    private int joueurY;
    private final List<PositionChangeListener> listeners = new ArrayList<>();
    
    public interface PositionChangeListener {
        void onPositionChanged(int oldX, int oldY, int newX, int newY);
    }
    
    public void addPositionChangeListener(PositionChangeListener listener) {
        listeners.add(listener);
    }
    
    public void setJoueurX(int x) {
        int oldX = this.joueurX;
        this.joueurX = x;
        notifyListeners(oldX, joueurY, x, joueurY);
    }
    
    private void notifyListeners(int oldX, int oldY, int newX, int newY) {
        for (PositionChangeListener listener : listeners) {
            listener.onPositionChanged(oldX, oldY, newX, newY);
        }
    }
}

// In vue/ or controleur/ (JavaFX adapter)
public class JavaFXLabyrintheAdapter {
    private final Labyrinthe labyrinthe;
    private final IntegerProperty joueurXProperty = new SimpleIntegerProperty();
    
    public JavaFXLabyrintheAdapter(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.joueurXProperty.set(labyrinthe.getJoueurX());
        
        // Bridge model changes to JavaFX properties
        labyrinthe.addPositionChangeListener((oldX, oldY, newX, newY) -> {
            joueurXProperty.set(newX);
        });
    }
    
    public IntegerProperty joueurXProperty() {
        return joueurXProperty;
    }
}
```

### Example 3: Splitting `Jeu` Responsibilities

**❌ Before (Current):**
```java
public class Jeu {
    Sauvegarde sauvegarde = new Sauvegarde();
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private ModeJeu modeJeu;
    private Vision vision;
    private Defi defiEnCours;
    private GameTimer gameTimer;
    
    // State management, timing, I/O, movement, etc. all mixed
}
```

**✅ After (Improved):**
```java
// Pure game state
public class GameSession {
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private ModeJeu modeJeu;
    private Vision vision;
    private Defi defiEnCours;
    
    // Only getters, setters, and state queries
}

// Timer management
public class GameTimeTracker {
    private GameTimer timer;
    
    public void start() { timer.startTimer(); }
    public void end() { timer.endTimer(); }
    public void reset() { timer = new GameTimer(); }
    public Duration getDuration() { return timer.getDuration(); }
}

// Orchestration
public class GameCoordinator {
    private final GameSession session;
    private final GameTimeTracker timeTracker;
    private final JoueurRepository repository;
    private final ScoreCalculator scoreCalculator;
    
    public GameCoordinator(GameSession session, GameTimeTracker timeTracker,
                          JoueurRepository repository, ScoreCalculator scoreCalculator) {
        this.session = session;
        this.timeTracker = timeTracker;
        this.repository = repository;
        this.scoreCalculator = scoreCalculator;
    }
    
    public boolean deplacerJoueur(int dx, int dy) {
        if (!session.getLabyrinthe().isJeuEnCours()) return false;
        
        timeTracker.start();
        return session.getLabyrinthe().deplacer(
            session.getLabyrinthe().getJoueurX() + dx,
            session.getLabyrinthe().getJoueurY() + dy
        );
    }
    
    public String terminerPartie(boolean victoire) {
        // Orchestrates ending game
        timeTracker.end();
        
        if (victoire && session.getJoueur() != null && session.getDefiEnCours() != null) {
            int score = scoreCalculator.calculerScore(
                session.getDefiEnCours(), 
                timeTracker.getDuration()
            );
            session.getJoueur().ajouterScore(score, session.getDefiEnCours());
            repository.sauvegarder(session.getJoueur());
        }
        
        return buildResultMessage(victoire);
    }
}
```

### Example 4: Interface Segregation for `Cellule`

**❌ Before (Current):**
```java
public abstract class Cellule {
    public boolean estPiege() { return false; }
    public boolean estChemin() { return false; }
    public boolean estMur() { return false; }
    public boolean estEntree() { return false; }
    public boolean estSortie() { return false; }
    // All cells expose all methods
}
```

**✅ After (Improved - Option with marker interfaces):**
```java
public interface Cellule {
    Image getTexture();
}

// Segregated interfaces
public interface Walkable { }
public interface Blocking { }
public interface Goal { }
public interface Hazard { }

// Implementations
public class Mur implements Cellule, Blocking {
    @Override
    public Image getTexture() { return murTexture; }
}

public class Chemin implements Cellule, Walkable {
    @Override
    public Image getTexture() { return cheminTexture; }
}

public class Sortie implements Cellule, Walkable, Goal {
    @Override
    public Image getTexture() { return sortieTexture; }
}

public class Piege implements Cellule, Walkable, Hazard {
    @Override
    public Image getTexture() { return piegeTexture; }
}

// Usage
public boolean peutDeplacer(int x, int y) {
    Cellule cellule = cellules[x][y];
    if (cellule == null || cellule instanceof Blocking) {
        return false;
    }
    return cellule instanceof Walkable;
}

public boolean estSurSortie(int x, int y) {
    return cellules[x][y] instanceof Goal;
}
```

---

## 10. Conclusion

### Overall Assessment

The "Jeu Labyrinthes" codebase demonstrates **solid foundational architecture** with clear MVC separation and good use of object-oriented design patterns. The project shows particularly strong adherence to the **Open/Closed Principle** and **Liskov Substitution Principle**, with well-designed abstractions for game generators and vision strategies.

### Key Strengths

1. ✅ **Excellent package structure** with clear MVC separation
2. ✅ **Strong use of design patterns** (Strategy, Factory, Abstract Factory)
3. ✅ **Extensible architecture** for adding new cell types, generators, and vision modes
4. ✅ **Good inheritance hierarchies** with proper substitutability
5. ✅ **Well-organized test structure** mirroring production code

### Areas for Improvement

1. ⚠️ **Dependency Inversion**: Too many direct instantiations and static dependencies
2. ⚠️ **Single Responsibility**: Several classes have multiple concerns mixed together
3. ⚠️ **MVC Purity**: JavaFX dependencies leak into model layer
4. ⚠️ **Interface Segregation**: Some interfaces could be more focused
5. ⚠️ **Testability**: Static methods and tight coupling make some classes hard to test

### Recommended Action Items

Focus on these high-impact improvements:

1. **Introduce dependency injection** for services (Sauvegarde, SoundManager, CalculateurScore)
2. **Remove JavaFX from model layer** using Observer pattern or adapters
3. **Split `Jeu` class** into focused components (GameSession, GameCoordinator, etc.)
4. **Move business logic** from controllers to model layer
5. **Refactor utility classes** to use interfaces instead of static methods

### Final Thoughts

This is a well-structured educational project that demonstrates good understanding of OOP principles and design patterns. The identified issues are common in JavaFX applications and addressing them would elevate the codebase to production-quality standards. The refactoring suggestions provided are incremental and can be implemented progressively without breaking existing functionality.

**Recommendation**: Start with high-priority items (dependency injection and removing JavaFX from model) as these have the highest impact on testability and maintainability.

---

**Document prepared by:** GitHub Copilot Analysis Agent  
**Date:** 2025-11-16  
**Repository:** edihamiti/jeu-labyrinthes  
**Analysis Scope:** SOLID Principles + MVC Architecture
