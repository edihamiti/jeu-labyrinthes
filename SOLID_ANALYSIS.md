# SOLID Principles Analysis

## Executive Summary

This document provides a comprehensive analysis of the `jeu-labyrinthes` codebase against the five SOLID principles of object-oriented design. The analysis identifies both areas where the code adheres well to these principles and areas where improvements could be made.

**Overall Assessment:**
- The codebase shows good understanding of basic OOP concepts
- Strong adherence to Liskov Substitution Principle (LSP)
- Good application of Interface Segregation in some areas
- Opportunities for improvement in Single Responsibility Principle (SRP)
- Significant improvements needed in Dependency Inversion Principle (DIP)

---

## 1. Single Responsibility Principle (SRP)

> "A class should have one, and only one, reason to change."

### ✅ Good Examples

#### `Cellule` Hierarchy
**Location:** `src/main/java/modele/Cellules/`

The cell classes (`Chemin`, `Mur`, `Entree`, `Sortie`) each have a single, clear responsibility - representing a specific type of cell in the maze. They use method overrides to define their type without additional logic.

```java
public class Chemin extends Cellule {
    @Override
    public boolean estChemin() {
        return true;
    }
}
```

**Why it's good:** Each class knows only about its own type identification. No additional responsibilities are mixed in.

#### `Vision` and `ModeJeu` Enums
**Location:** `src/main/java/modele/`

These enums have a single responsibility - defining game modes and vision types respectively.

```java
public enum Vision {
    VUE_LIBRE,
    VUE_LOCAL
}
```

---

### ⚠️ Areas for Improvement

#### `Jeu` Class
**Location:** `src/main/java/modele/Jeu.java`

**Issues:**
1. **Game State Management** - Manages joueur, labyrinthe, defi, modeJeu, vision
2. **Persistence** - Contains `Sauvegarde` instance and calls save operations
3. **Game Logic** - Contains `deplacerJoueur()`, `estVictoire()`, `terminerPartie()`
4. **Player I/O** - Uses `Scanner` for console input in `initialiser()`
5. **Singleton Pattern** - Manages its own instance

**Recommendation:**
```java
// Separate concerns into:
// 1. GameStateManager - manages current game state
// 2. GameEngine - handles game logic (movement, victory)
// 3. GameSession - coordinates between components
// 4. Use dependency injection instead of singleton
```

**Example Refactoring:**
```java
public class GameStateManager {
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    private ModeJeu modeJeu;
    private Vision vision;
    private Defi defiEnCours;
    
    // Only getters/setters for state
}

public class GameEngine {
    private final GameStateManager stateManager;
    
    public boolean deplacerJoueur(int dx, int dy) {
        // Movement logic only
    }
    
    public boolean estVictoire() {
        // Victory check logic only
    }
}
```

#### `Labyrinthe` Class
**Location:** `src/main/java/modele/Labyrinthe.java`

**Issues:**
1. **Maze Generation** - `generer()`, `faireChemin()`, `faireCheminAlternatif()`
2. **Pathfinding** - `calculePlusCourtChemin()` (Dijkstra algorithm)
3. **Game State** - Player position (`joueurX`, `joueurY`, `jeuEnCours`)
4. **Movement Validation** - `peutDeplacer()`, `estSurSortie()`

**Recommendation:**
```java
// Separate into:
// 1. Labyrinthe - structure and data only
// 2. LabyrintheGenerator - generation algorithms
// 3. PathfindingService - pathfinding algorithms
// 4. PlayerPosition - player location (could be in Game/Player)
```

**Example Refactoring:**
```java
public class Labyrinthe {
    private final int largeur;
    private final int hauteur;
    private Cellule[][] cellules;
    
    // Structure-related methods only
    public boolean estDansLimites(int x, int y) { }
    public Cellule getCellule(int x, int y) { }
}

public class LabyrintheGenerator {
    public Cellule[][] generer(int largeur, int hauteur, double pourcentageMurs) {
        // Generation logic
    }
    
    private void faireChemin(Cellule[][] cellules, int startX, int startY) {
        // Path creation logic
    }
}

public class PathfindingService {
    public int calculePlusCourtChemin(Labyrinthe labyrinthe) {
        // Pathfinding algorithm
    }
}
```

#### `JeuControleur` Class
**Location:** `src/main/java/controleur/JeuControleur.java`

**Issues:**
1. **UI Management** - FXML bindings, scene switching
2. **Game Logic** - Movement handling, victory detection
3. **Audio** - Sound playback
4. **Rendering Coordination** - Managing different render strategies
5. **Input Handling** - Keyboard event processing

**Recommendation:**
```java
// Separate into:
// 1. JeuControleur - UI/FXML coordination only
// 2. InputHandler - keyboard/input processing
// 3. AudioManager - sound effects
// 4. RenderingCoordinator - render strategy selection
```

#### `Joueur` Class
**Location:** `src/main/java/modele/Joueur.java`

**Issues:**
1. **Player Data** - id, pseudo, score, progression
2. **Validation** - Pseudo validation logic
3. **Persistence** - JSON serialization/deserialization
4. **Static Counter** - Managing ID generation

**Recommendation:**
```java
// Separate into:
// 1. Joueur - data model only
// 2. PseudoValidator - validation logic
// 3. JoueurRepository - JSON handling
// 4. JoueurFactory - ID generation
```

**Example Refactoring:**
```java
public class Joueur {
    private final int id;
    private String pseudo;
    private int score;
    private HashMap<Defi, Boolean> progression;
    
    // Constructor and simple getters/setters only
}

public class PseudoValidator {
    public void validate(String pseudo) throws PseudoException {
        if (pseudo == null || pseudo.trim().isEmpty())
            throw new PseudoException("Le pseudo ne peut pas être vide");
        if (pseudo.length() > 15)
            throw new PseudoException("Le pseudo est trop long");
        if (!pseudo.matches("[a-zA-Z0-9]+"))
            throw new PseudoException("Caractères invalides");
    }
}

public class JoueurRepository {
    public JSONObject toJson(Joueur joueur) { }
    public Joueur fromJson(JSONObject json) { }
}
```

#### `Sauvegarde` Class
**Location:** `src/main/java/modele/Sauvegarde.java`

**Issues:**
1. **File I/O** - Reading and writing to disk
2. **Data Structure Management** - HashMap of players
3. **JSON Serialization** - Converting to/from JSON
4. **File System Operations** - Creating directories
5. **Player Factory** - Creating new players when not found

**Recommendation:**
```java
// Separate into:
// 1. PlayerRepository - in-memory player storage
// 2. PlayerPersistence - file I/O operations
// 3. PlayerSerializer - JSON conversion
```

---

## 2. Open/Closed Principle (OCP)

> "Software entities should be open for extension, but closed for modification."

### ✅ Good Examples

#### `Cellule` Hierarchy
**Location:** `src/main/java/modele/Cellules/`

The abstract `Cellule` class is **excellent** for OCP:

```java
public abstract class Cellule {
    public boolean estChemin() { return false; }
    public boolean estMur() { return false; }
    public boolean estEntree() { return false; }
    public boolean estSortie() { return false; }
}
```

**Why it's good:**
- New cell types can be added without modifying existing code
- Each subclass only overrides what it needs
- Type checking is done polymorphically

**Example of easy extension:**
```java
// New cell type requires ZERO changes to existing code
public class Piege extends Cellule {
    @Override
    public boolean estPiege() { return true; }
}
```

#### `Rendu` Interface
**Location:** `src/main/java/vue/Rendu.java`

The rendering interface allows for different rendering strategies:

```java
public interface Rendu {
    Node rendu(Labyrinthe labyrinthe);
    void setBlockedWall(int x, int y);
}
```

**Implementations:**
- `LabyrintheRendu` - Full maze view
- `LocaleRendu` - Limited view around player
- `MiniMapRendu` - Small overview map (note: doesn't fully implement interface)

**Why it's good:** New rendering strategies can be added without changing existing renderers.

---

### ⚠️ Areas for Improvement

#### `JeuControleur` - Render Strategy Selection
**Location:** `src/main/java/controleur/JeuControleur.java` (lines 197-212)

**Issue:**
```java
public void setRenduLabyrinthe() {
    if (Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION)) {
        if (Jeu.getInstance().getDefiEnCours().getVision().equals(Vision.VUE_LOCAL)) {
            overlayMinimap.setVisible(true);
            this.renduMinimap = new MiniMapRendu(...);
            this.renduLabyrinthe = new LocaleRendu(...);
        } else {
            overlayMinimap.setVisible(false);
            this.renduLabyrinthe = new LabyrintheRendu(...);
        }
    } else {
        overlayMinimap.setVisible(false);
        this.renduLabyrinthe = new LabyrintheRendu(...);
    }
}
```

**Problem:** Adding new vision types or game modes requires modifying this method (violates OCP).

**Recommendation:**
Use a factory pattern or strategy registry:

```java
public class RenduFactory {
    private final Map<RenduConfig, Supplier<Rendu>> strategies = new HashMap<>();
    
    public RenduFactory() {
        // Register strategies
        strategies.put(
            new RenduConfig(ModeJeu.MODE_PROGRESSION, Vision.VUE_LOCAL),
            () -> new LocaleRendu(labyrinthe, container)
        );
        strategies.put(
            new RenduConfig(ModeJeu.MODE_LIBRE, null),
            () -> new LabyrintheRendu(labyrinthe, container)
        );
    }
    
    public Rendu createRendu(ModeJeu mode, Vision vision) {
        RenduConfig config = new RenduConfig(mode, vision);
        Supplier<Rendu> supplier = strategies.get(config);
        if (supplier == null) {
            return new LabyrintheRendu(labyrinthe, container); // default
        }
        return supplier.get();
    }
}
```

#### `Defi` Enum with Hardcoded Values
**Location:** `src/main/java/modele/Defi.java`

**Issue:**
```java
public enum Defi {
    FACILE1(6, 6, 25.0, 5, 1, Vision.VUE_LIBRE),
    MOYEN1(6, 6, 50.0, 15, 1, Vision.VUE_LIBRE),
    // ... 9 hardcoded challenges
}
```

**Problem:**
- Cannot add new challenges without modifying the enum
- Challenges are not data-driven
- Cannot load challenges from configuration

**Recommendation:**
```java
public class Defi {
    private final String id;
    private final int largeur;
    private final int hauteur;
    private final double pourcentageMurs;
    private final int points;
    private final Vision vision;
    private final int etape;
    
    // Constructor with validation
}

public class DefiRepository {
    private final List<Defi> defis = new ArrayList<>();
    
    public void loadFromJson(String filepath) {
        // Load challenges from external file
    }
    
    public List<Defi> getDefisByEtape(int etape) {
        return defis.stream()
            .filter(d -> d.getEtape() == etape)
            .collect(Collectors.toList());
    }
}
```

#### Movement Logic in Multiple Places
**Location:** `src/main/java/modele/Jeu.java` and `src/main/java/controleur/JeuControleur.java`

**Issue:**
Movement logic is spread across both `Jeu.deplacerJoueur()` and `JeuControleur` methods (`deplacerHaut()`, `deplacerBas()`, etc.).

**Recommendation:**
```java
// Command pattern for movements
public interface MouvementCommand {
    boolean execute(Labyrinthe labyrinthe);
    void undo();
}

public class DeplacerHautCommand implements MouvementCommand {
    @Override
    public boolean execute(Labyrinthe labyrinthe) {
        return labyrinthe.deplacerJoueur(-1, 0);
    }
}

// Easy to add new movement types without modifying existing code
public class TeleportCommand implements MouvementCommand {
    private final int targetX, targetY;
    // ...
}
```

---

## 3. Liskov Substitution Principle (LSP)

> "Objects of a superclass should be replaceable with objects of a subclass without breaking the application."

### ✅ Good Examples

#### `Cellule` Subclasses
**Location:** `src/main/java/modele/Cellules/`

**Excellent LSP compliance:**

```java
// Base class defines contract
public abstract class Cellule {
    public boolean estChemin() { return false; }
    public boolean estMur() { return false; }
    public boolean estEntree() { return false; }
    public boolean estSortie() { return false; }
}

// All subclasses can substitute the parent
Cellule cellule1 = new Mur(0, 0);
Cellule cellule2 = new Chemin(0, 1);
Cellule cellule3 = new Entree(0, 2);

// Works correctly for any subclass
if (cellule.estMur()) {
    // Handle as wall
}
```

**Why it works:**
- All subclasses respect the base class contract
- No subclass weakens preconditions or strengthens postconditions
- Behavior is predictable and consistent
- Type-checking methods follow a clear pattern

**Code using LSP correctly:**
```java
// From Labyrinthe.java line 381
public boolean peutDeplacer(int x, int y) {
    if (x < 0 || x >= largeurMax || y < 0 || y >= hauteurMax) {
        return false;
    } else if (cellules[x][y] == null || cellules[x][y].estMur()) {
        return false;
    }
    return !cellules[x][y].estMur();  // Works for ANY Cellule subclass
}
```

---

### ⚠️ Areas for Improvement

#### `Rendu` Interface Implementation Inconsistency
**Location:** `src/main/java/vue/`

**Issue:**
The `Rendu` interface defines:
```java
public interface Rendu {
    Node rendu(Labyrinthe labyrinthe);
    void setBlockedWall(int x, int y);
}
```

But implementations have different return types:
- `LabyrintheRendu.rendu()` returns `Canvas`
- `LocaleRendu.rendu()` returns `VBox`
- `MiniMapRendu.rendu()` returns `Canvas` (but doesn't formally implement `Rendu`)

**Problem:**
While technically valid (both Canvas and VBox are Nodes), this makes the interface less predictable. Code that expects specific behavior may break.

**Current usage in JeuControleur.java (line 120):**
```java
contienLabyrinthe.getChildren().add(renduLabyrinthe.rendu(Jeu.getInstance().getLabyrinthe()));
```

This works because `VBox.getChildren().add(Node)` accepts any Node, but it's fragile.

**Recommendation:**
```java
// Option 1: Enforce consistent return type
public interface Rendu {
    Canvas rendu(Labyrinthe labyrinthe);
    void setBlockedWall(int x, int y);
}

// Option 2: Use more specific interfaces
public interface CanvasRendu extends Rendu {
    Canvas renduCanvas(Labyrinthe labyrinthe);
}

public interface CompositeRendu extends Rendu {
    VBox renduComposite(Labyrinthe labyrinthe);
}

// Option 3: Wrap everything in a container
public class RenduResult {
    private final Node node;
    private final boolean needsMinimap;
    // ...
}
```

#### `MiniMapRendu` Not Implementing `Rendu`
**Location:** `src/main/java/vue/MiniMapRendu.java`

**Issue:**
`MiniMapRendu` has the same method signatures as `Rendu` but doesn't implement the interface:

```java
public class MiniMapRendu {  // Should implement Rendu
    public Canvas rendu(Labyrinthe labyrinthe) { }
    // But missing setBlockedWall() method!
}
```

**Problem:**
- Cannot use `MiniMapRendu` polymorphically with other renderers
- Not substitutable where `Rendu` is expected
- Violates LSP implicitly

**Recommendation:**
```java
public class MiniMapRendu implements Rendu {
    @Override
    public Canvas rendu(Labyrinthe labyrinthe) {
        // Existing implementation
    }
    
    @Override
    public void setBlockedWall(int x, int y) {
        // Minimap might not show blocked walls, but still needs to implement
        // Option: Do nothing, or add subtle indicator
    }
}
```

---

### 💡 Best Practice Example

The `Cellule` hierarchy demonstrates **perfect LSP** because:

1. **Behavioral consistency:** All subclasses behave as cells
2. **No exceptions:** No subclass throws unexpected exceptions
3. **Postconditions respected:** All type-checking methods return boolean as expected
4. **No side effects:** Checking cell type doesn't modify state
5. **Preconditions not strengthened:** All subclasses accept the same construction parameters

**Example of correct usage:**
```java
// From LabyrintheRendu.java
for (int i = 0; i < largeurMax; i++) {
    for (int j = 0; j < hauteurMax; j++) {
        // Can treat all cells uniformly
        if (labyrinthe[i][j].estMur()) {
            graphicsContext.drawImage(imgMur, x, y, tailleCellule, tailleCellule);
        } else if (labyrinthe[i][j].estChemin() || labyrinthe[i][j].estEntree()) {
            graphicsContext.drawImage(imgChemin, x, y, tailleCellule, tailleCellule);
        } else if (labyrinthe[i][j].estSortie()) {
            graphicsContext.drawImage(imgSortie, x, y, tailleCellule, tailleCellule);
        }
    }
}
```

This code works correctly regardless of which `Cellule` subclass is in the array - perfect substitutability!

---

## 4. Interface Segregation Principle (ISP)

> "No client should be forced to depend on methods it does not use."

### ✅ Good Examples

#### Minimal `Rendu` Interface
**Location:** `src/main/java/vue/Rendu.java`

The `Rendu` interface is small and focused:

```java
public interface Rendu {
    Node rendu(Labyrinthe labyrinthe);
    void setBlockedWall(int x, int y);
}
```

**Why it's good:**
- Only 2 methods - minimal and focused
- Both methods are relevant to all rendering implementations
- No "fat interface" problem

---

### ⚠️ Areas for Improvement

#### `Rendu` Interface - Not All Methods Needed by All Implementations

**Issue:**
`MiniMapRendu` doesn't need `setBlockedWall()` functionality (minimap doesn't show blocked walls), yet if it implemented `Rendu`, it would be forced to provide this method.

**Current State:**
```java
// MiniMapRendu doesn't implement Rendu at all - avoiding the problem
public class MiniMapRendu {
    public Canvas rendu(Labyrinthe labyrinthe) { }
    // No setBlockedWall() - doesn't need it
}
```

**Recommendation:**
Split the interface into more focused contracts:

```java
// Base interface - all renderers need this
public interface LabyrintheRenderer {
    Node rendu(Labyrinthe labyrinthe);
}

// Optional capability - only for interactive renderers
public interface InteractiveRenderer extends LabyrintheRenderer {
    void setBlockedWall(int x, int y);
}

// Implementations choose what they need
public class LabyrintheRendu implements InteractiveRenderer {
    @Override
    public Canvas rendu(Labyrinthe labyrinthe) { }
    
    @Override
    public void setBlockedWall(int x, int y) { }
}

public class MiniMapRendu implements LabyrintheRenderer {
    @Override
    public Canvas rendu(Labyrinthe labyrinthe) { }
    // No need to implement setBlockedWall()
}
```

#### No Defined Interfaces for Services

**Issue:**
Many classes provide services but don't define interfaces, making them harder to test and replace:

**Missing Interfaces:**
```java
// No interface for persistence
public class Sauvegarde {
    public void sauvegardeJoueurs() { }
    public void chargerJoueurs() { }
    // Direct implementation, hard to mock or replace
}

// No interface for maze generation
public class Labyrinthe {
    public void generer() { }
    // Generation algorithm is tightly coupled to Labyrinthe
}
```

**Recommendation:**
```java
// Define focused interfaces
public interface PlayerRepository {
    void save(Collection<Joueur> joueurs);
    Collection<Joueur> load();
    Joueur findByPseudo(String pseudo);
}

public interface MazeGenerator {
    Cellule[][] generate(int largeur, int hauteur, double pourcentageMurs);
}

public interface PathfindingService {
    int calculateShortestPath(Cellule[][] cellules, int startX, int startY, int endX, int endY);
}

// Implementations
public class JsonPlayerRepository implements PlayerRepository {
    // JSON file-based implementation
}

public class DFSMazeGenerator implements MazeGenerator {
    // Depth-first search implementation
}

public class BFSPathfinding implements PathfindingService {
    // Breadth-first search implementation
}
```

**Benefits:**
- Easy to create mock implementations for testing
- Can swap implementations (e.g., JSON → Database)
- Can add new implementations without modifying existing code
- Follows Dependency Inversion Principle

---

### 💡 Additional Recommendations

#### Define Role-Based Interfaces

Instead of one large interface, define multiple small interfaces based on roles:

```java
// Current: No interfaces defined for game operations

// Recommended: Define role-based interfaces

public interface GameStateProvider {
    Labyrinthe getLabyrinthe();
    Joueur getJoueur();
    ModeJeu getModeJeu();
}

public interface GameActions {
    boolean deplacerJoueur(int dx, int dy);
    String terminerPartie(boolean victoire);
}

public interface GameConfiguration {
    void setLabyrinthe(Labyrinthe labyrinthe);
    void setJoueur(Joueur joueur);
    void setModeJeu(ModeJeu modeJeu);
}

// Controllers only depend on the interfaces they need
public class JeuControleur {
    private final GameStateProvider gameState;
    private final GameActions gameActions;
    
    // Only depends on what it needs, not entire Jeu class
}
```

---

## 5. Dependency Inversion Principle (DIP)

> "Depend on abstractions, not on concretions. High-level modules should not depend on low-level modules. Both should depend on abstractions."

### ⚠️ Significant Issues

This is the area where the codebase needs the **most improvement**. Almost all dependencies are on concrete classes rather than abstractions.

#### Singleton Pattern Creating Tight Coupling
**Location:** Throughout the codebase

**Major Issues:**

```java
// From JeuControleur.java - tight coupling to Jeu singleton
Jeu.getInstance().setLabyrinthe(new Labyrinthe(10, 10, 10));
Jeu.getInstance().getLabyrinthe().generer();
Jeu.getInstance().getModeJeu().equals(ModeJeu.MODE_PROGRESSION);
```

```java
// From Jeu.java - tight coupling to Sauvegarde
Sauvegarde sauvegarde = new Sauvegarde();  // Direct instantiation
```

```java
// From AppControleur.java
AppControleur.getInstance().setPrimaryStage(stage);
AppControleur.getInstance().MenuPrincipal();
```

**Problems:**
1. Cannot test components in isolation
2. Cannot replace implementations
3. Global state makes behavior unpredictable
4. Hard to reason about dependencies
5. Impossible to mock for unit tests

**Recommendation:**
Use Dependency Injection instead of singletons:

```java
// Define interfaces
public interface GameState {
    Labyrinthe getLabyrinthe();
    Joueur getJoueur();
    // ...
}

public interface PlayerRepository {
    void save(Collection<Joueur> joueurs);
    Joueur findByPseudo(String pseudo);
}

// Concrete implementations
public class JeuImpl implements GameState {
    private final PlayerRepository playerRepository;
    private Joueur joueur;
    private Labyrinthe labyrinthe;
    
    // Inject dependencies via constructor
    public JeuImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
}

// Controllers receive dependencies
public class JeuControleur {
    private final GameState gameState;
    private final PlayerRepository playerRepository;
    
    // Dependencies injected, not accessed globally
    public JeuControleur(GameState gameState, PlayerRepository playerRepository) {
        this.gameState = gameState;
        this.playerRepository = playerRepository;
    }
}

// Application wiring (in main/App.java)
public class App extends Application {
    @Override
    public void start(Stage stage) {
        // Create dependencies
        PlayerRepository playerRepo = new JsonPlayerRepository("saves/joueurs.json");
        GameState gameState = new JeuImpl(playerRepo);
        
        // Wire up controllers
        JeuControleur controller = new JeuControleur(gameState, playerRepo);
        
        // Continue setup...
    }
}
```

#### Direct Instantiation of Concrete Classes

**Issue 1: Controllers Creating Renderers**
```java
// From JeuControleur.java
this.renduMinimap = new MiniMapRendu(Jeu.getInstance().getLabyrinthe(), minimap);
this.renduLabyrinthe = new LocaleRendu(Jeu.getInstance().getLabyrinthe(), contienLabyrinthe);
```

**Recommendation:**
```java
public interface RenduFactory {
    Rendu createRendu(RenduType type, Labyrinthe lab, VBox container);
}

public class JeuControleur {
    private final RenduFactory renduFactory;
    
    public JeuControleur(RenduFactory factory) {
        this.renduFactory = factory;
    }
    
    public void setRenduLabyrinthe() {
        this.renduLabyrinthe = renduFactory.createRendu(
            determineRenduType(), 
            gameState.getLabyrinthe(), 
            contienLabyrinthe
        );
    }
}
```

**Issue 2: Jeu Creating Sauvegarde**
```java
// From Jeu.java
Sauvegarde sauvegarde = new Sauvegarde();  // Direct instantiation
```

**Recommendation:**
```java
public class Jeu {
    private final PlayerRepository playerRepository;
    
    public Jeu(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }
    
    public void terminerPartie(boolean victoire) {
        // Use abstraction, not concrete class
        playerRepository.save(Arrays.asList(joueur));
    }
}
```

**Issue 3: Labyrinthe with JavaFX Properties**
```java
// From Labyrinthe.java
private final IntegerProperty joueurX;
private final IntegerProperty joueurY;
private final BooleanProperty jeuEnCours;
```

**Problem:** Domain model (Labyrinthe) depends on JavaFX UI framework (concrete implementation).

**Recommendation:**
```java
// Option 1: Use observer pattern with custom interfaces
public interface PropertyChangeListener<T> {
    void onChanged(T oldValue, T newValue);
}

public class ObservableProperty<T> {
    private T value;
    private final List<PropertyChangeListener<T>> listeners = new ArrayList<>();
    
    public void addListener(PropertyChangeListener<T> listener) {
        listeners.add(listener);
    }
    
    public void set(T newValue) {
        T oldValue = this.value;
        this.value = newValue;
        listeners.forEach(l -> l.onChanged(oldValue, newValue));
    }
}

// Option 2: Separate UI state from domain model
public class Labyrinthe {
    private Cellule[][] cellules;
    // No UI properties, just pure domain model
}

public class LabyrintheViewModel {
    private final Labyrinthe labyrinthe;
    private final IntegerProperty joueurXProperty;
    private final IntegerProperty joueurYProperty;
    
    public LabyrintheViewModel(Labyrinthe labyrinthe) {
        this.labyrinthe = labyrinthe;
        this.joueurXProperty = new SimpleIntegerProperty();
        this.joueurYProperty = new SimpleIntegerProperty();
    }
}
```

#### No Abstraction for File I/O

**Issue:**
```java
// From Sauvegarde.java - direct file operations
try (FileWriter fileWriter = new FileWriter(FICHIER)) {
    fileWriter.write(jsonJoueurs.toString(4));
}
```

**Recommendation:**
```java
// Define abstraction for storage
public interface StorageService {
    String read(String path) throws IOException;
    void write(String path, String content) throws IOException;
    boolean exists(String path);
}

// File-based implementation
public class FileStorageService implements StorageService {
    @Override
    public void write(String path, String content) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        }
    }
    // ...
}

// Can easily add other implementations
public class InMemoryStorageService implements StorageService {
    private final Map<String, String> storage = new HashMap<>();
    
    @Override
    public void write(String path, String content) {
        storage.put(path, content);
    }
    // Useful for testing!
}

// Usage
public class JsonPlayerRepository implements PlayerRepository {
    private final StorageService storage;
    
    public JsonPlayerRepository(StorageService storage) {
        this.storage = storage;
    }
    
    @Override
    public void save(Collection<Joueur> joueurs) {
        String json = convertToJson(joueurs);
        storage.write("saves/joueurs.json", json);
    }
}
```

---

### 💡 Recommended Architecture

```java
// Domain Layer (abstractions)
public interface MazeRepository {
    Cellule[][] generateMaze(int width, int height, double wallPercent);
}

public interface PlayerRepository {
    Collection<Joueur> loadAll();
    void saveAll(Collection<Joueur> joueurs);
    Joueur findByPseudo(String pseudo);
}

public interface GameEngine {
    boolean movePlayer(int dx, int dy);
    boolean checkVictory();
    String endGame(boolean victory);
}

// Infrastructure Layer (implementations)
public class DFSMazeRepository implements MazeRepository { }
public class JsonPlayerRepository implements PlayerRepository { }
public class LabyrintheGameEngine implements GameEngine { }

// Application Layer (orchestration)
public class GameSession {
    private final GameEngine engine;
    private final PlayerRepository playerRepo;
    private final MazeRepository mazeRepo;
    
    public GameSession(
        GameEngine engine,
        PlayerRepository playerRepo,
        MazeRepository mazeRepo
    ) {
        this.engine = engine;
        this.playerRepo = playerRepo;
        this.mazeRepo = mazeRepo;
    }
    
    public void startNewGame(String pseudo, int width, int height) {
        Joueur joueur = playerRepo.findByPseudo(pseudo);
        Cellule[][] maze = mazeRepo.generateMaze(width, height, 0.3);
        // Initialize game...
    }
}

// Presentation Layer
public class JeuControleur {
    private final GameSession session;
    
    public JeuControleur(GameSession session) {
        this.session = session;
    }
}
```

**Benefits:**
1. **Testability:** Can inject mocks for all dependencies
2. **Flexibility:** Easy to swap implementations
3. **Maintainability:** Changes to implementations don't affect interfaces
4. **Clarity:** Dependencies are explicit, not hidden
5. **Reusability:** Components can be used in different contexts

---

## Summary of Findings

### Strengths

1. **Excellent LSP compliance** in the `Cellule` hierarchy
2. **Good use of inheritance** for cell types
3. **Interface usage** for rendering (though inconsistent)
4. **Separation of concerns** between model, view, and controller packages
5. **Enum usage** for simple type definitions (Vision, ModeJeu)

### Key Areas for Improvement

1. **Single Responsibility:** Many classes have multiple responsibilities
   - `Jeu` class does too much
   - `Labyrinthe` mixes data, generation, and pathfinding
   - `JeuControleur` handles UI, input, audio, and game logic
   - `Joueur` mixes data, validation, and serialization

2. **Dependency Inversion:** Heavy use of singletons and concrete classes
   - `Jeu.getInstance()` and `AppControleur.getInstance()` everywhere
   - Direct instantiation of concrete classes
   - No interface-based dependencies
   - Domain models depend on UI framework (JavaFX properties)

3. **Open/Closed:** Some areas resist extension
   - `Defi` enum hardcodes all challenges
   - Render strategy selection uses if/else chains
   - Movement logic scattered across classes

4. **Interface Segregation:** Missing interfaces
   - No interfaces for repositories
   - No interfaces for services
   - Some unnecessary interface methods (MiniMapRendu vs Rendu)

### Priority Recommendations

#### High Priority (Most Impact)

1. **Remove Singletons:** Replace with dependency injection
   ```java
   // Remove: Jeu.getInstance()
   // Add: Constructor injection of dependencies
   ```

2. **Extract Responsibilities:** Break up large classes
   ```java
   // Break Jeu into: GameState, GameEngine, GameSession
   // Break Labyrinthe into: Labyrinthe, MazeGenerator, Pathfinding
   ```

3. **Define Service Interfaces:** Create abstractions for all services
   ```java
   // Add: PlayerRepository, MazeGenerator, PathfindingService
   ```

#### Medium Priority (Good Improvements)

4. **Separate Domain from UI:** Remove JavaFX dependencies from domain models
   ```java
   // Move IntegerProperty/BooleanProperty to ViewModels
   ```

5. **Use Factory Pattern:** For object creation
   ```java
   // Add: RenduFactory, JoueurFactory
   ```

6. **Make Defi Data-Driven:** Load from configuration
   ```java
   // Replace: enum with class + repository
   ```

#### Low Priority (Nice to Have)

7. **Command Pattern for Movements:** Encapsulate actions
8. **Observer Pattern:** For event notifications
9. **Strategy Pattern:** For different algorithms

---

## Conclusion

The codebase demonstrates good fundamental OOP understanding, particularly in the `Cellule` hierarchy which exemplifies excellent LSP and OCP. However, there are significant opportunities to improve adherence to SOLID principles, especially around:

1. **Dependency Inversion:** Replacing singletons and concrete dependencies with interfaces and dependency injection
2. **Single Responsibility:** Breaking up classes with multiple responsibilities
3. **Open/Closed:** Using patterns to support extension without modification

Implementing these recommendations would:
- Make the code more testable
- Improve maintainability
- Enable easier feature additions
- Reduce coupling between components
- Make the codebase more professional and scalable

The good news is that the package structure (modele, vue, controleur) provides a solid foundation for these improvements, and the existing separation of concerns makes refactoring more manageable.

---

## Next Steps

If you'd like to improve SOLID compliance, I recommend:

1. **Start Small:** Begin with defining interfaces for existing services (PlayerRepository, MazeGenerator)
2. **Refactor Incrementally:** Introduce dependency injection gradually, one controller at a time
3. **Write Tests:** As you refactor, add unit tests using the new interfaces
4. **Document Patterns:** Create a design document showing the new architecture
5. **Review and Iterate:** After initial refactoring, review against SOLID principles again

Would you like specific code examples or assistance with any of these refactorings?
