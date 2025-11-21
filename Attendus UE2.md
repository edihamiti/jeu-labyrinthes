# 1. Investissement dans le projet

Groupe I5 :

* **Edi HAMITI** - `153 commits` - `37.9%`
* **Enzo DEWAELE** - `111 commits` - `27.5%`
* **Amaury VANHOUTTE** - `70 commits` - `17.3%`
* **Bastien FERNANDES** - `70 commits` - `17.3%`

*pourcentage d'investissement calculé par le nombre de commits.

# 2. Structures de données pour les labyrinthes

## Structure pour les labyrinthes aléatoires

Pour la génération et la représentation des labyrinthes aléatoires, nous avons opté pour un tableau à deux dimensions
d'objets de type `Cellule`. Cette structure est définie et instanciée dans la classe `GenerateurAleatoire` ainsi que
dans la classe principale `Labyrinthe`.

Contrairement à une simple matrice de booléens ou d'entiers, notre approche se base sur du polymorphisme : chaque case
de la grille est une instance d'une classe héritant de la classe abstraite `Cellule`(soit `Mur`, `Chemin`, `Entree` ou
`Sortie`). Cela permet de potentiellement d'autres états futurs sans modifier la structure du tableau.

## Structure pour les labyrinthes parfaits et justification

Pour les labyrinthes parfaits, implémentés dans la classe `GenerateurParfait`, nous avons conservé cette même structure
de double tableau d'objets `Cellule`. Cette modélisation n'est pas celle proposée dans le sujet de TP, qui proposait
l'utilisation de deux tableau de booléens distincts (`murVerticaux`et `murHorizontaux`).

Nous pensons que cette structure est plus efficace pour plusieurs raisons :

* L'information est centralisé, elle regroupe tout en une seule entité plutôt que de devoir interroger deux tableaux
  différents pour connaître les limites d'une case.
* Pour nos algorithmes de parcours, il est plus intuitif de manipuler des noeuds (les cases `Chemin`) et leur voisin
  direct plutôt que de calculer des intersections de murs.
* Notre structure nous permet d'intégrer plus facilement des éléments de jeu différents (comme les cellules `Cle`ou une
  `Sortie` qui est conditionelle) directement dans la grille, ce qui serait impossible avec de simples tableaux de
  booléens gérant uniquement les murs.

# 3. Algorithmes de génération

## Labyrinthe Aléatoire

On commence par créer une grille en tableau 2D, car cela permet de représenter le labyrinthe de façon simple, avec un
accès direct aux cellules. Chaque cellule est d’abord un mur.

```
Créer grille[largeur][hauteur]

Pour x de 0 à largeur - 1
  Pour y de 0 à hauteur - 1
    grille[x][y] = MUR
```

On place ensuite une entrée dans une position définie. On choisit une coordonnée fixe pour simplifier, et c’est à partir
de cette case que la création du chemin principal va commencer.

```
entreeX = 0
entreeY = 1
grille[entreeX][entreeY] = ENTREE
```

Pour garantir qu’il existe un chemin entre l’entrée et la sortie, on utilise une exploration en profondeur (DFS). Cette
méthode est adaptée car elle avance le plus loin possible avant de revenir en arrière. Pour cela, on utilise une pile (
LIFO), ce qui permet de revenir automatiquement aux cellules précédentes lorsqu’on ne peut plus avancer.
Chaque cellule visitée est marquée comme chemin, donc à la fin on a forcément un chemin continu depuis l’entrée jusqu’à
la dernière cellule visitée, qui deviendra la sortie.

```
Procédure ConstruireChemin(x, y)

Créer tableau visite[][] initialisé à FAUX
Créer pile vide

visite[x][y] = VRAI
Empiler (x,y)

Tant que la pile n’est pas vide
  (cx, cy) = sommet de la pile

  chercher les voisins non visités de (cx,cy)

  si des voisins existent
    choisir un voisin au hasard (nx, ny)
    grille[nx][ny] = CHEMIN
    visite[nx][ny] = VRAI
    Empiler (nx, ny)
  sinon
    Dépiler

Fin Tant que

La dernière cellule visitée devient SORTIE
Fin Procédure
```

Appel :

```
ConstruireChemin(entreeX, entreeY)
```

Des chemins secondaires peuvent être créés pour rendre le labyrinthe moins linéaire. On choisit certaines cellules du
chemin principal et on creuse à partir d’elles tant qu’il existe des murs voisins disponibles.
Ces chemins ne remplacent pas le chemin principal, ils l’enrichissent simplement.

```
Pour chaque cellule de la grille
  si la cellule est un chemin et x mod 10 = 0
    CreerCheminSecondaire(x, y)
```

La procédure suivante crée un chemin secondaire tant qu’il est possible d’avancer. Elle reste simple : tant qu’il existe
des cellules voisines encore murées, on avance dans une direction choisie au hasard.

```
Procédure CreerCheminSecondaire(x, y)

Pour un certain nombre de pas
  chercher voisins murés
  si aucun voisin
    arrêter
  choisir un voisin au hasard (nx, ny)
  grille[nx][ny] = CHEMIN
  x = nx
  y = ny

Fin Procédure
```

Pour obtenir le pourcentage de murs demandé par l’utilisateur, on calcule combien de cases internes devraient être des
chemins. Ensuite, tant que le total actuel de chemins est insuffisant, on choisit une cellule au hasard et si c’est un
mur, on la transforme en chemin.
Cette méthode permet de contrôler le pourcentage global de murs après la construction du chemin garanti.

```
totalCellulesInternes = (largeur - 2) * (hauteur - 2)
nbCheminsVoulus = totalCellulesInternes * (100 - pourcentageMurs) / 100

Tant que nombreDeChemins < nbCheminsVoulus
  choisir (x,y) au hasard
  si grille[x][y] = MUR
    grille[x][y] = CHEMIN
    nombreDeChemins++
```

Enfin, la grille complète est retournée comme labyrinthe final.

```
Retourner grille
Fin
```

Ces méthodes sont utilisé dans 2 classes qui sont jeuControleur.java et TypeLabyrinthe.java

## Labyrinthe Parfait

On utilise une grille en tableau 2D pour représenter le labyrinthe, car cela permet d’accéder à chaque case directement
et facilement en fonction de ses coordonnées. On remplit d’abord toute la grille avec des murs.

```
Créer grille[largeur][hauteur]

Pour x de 0 à largeur - 1
  Pour y de 0 à hauteur - 1
    grille[x][y] = MUR
```

On place ensuite une entrée dans la première colonne, mais à une position verticale choisie aléatoirement. Cela permet
d’avoir des labyrinthes variés à chaque génération.

```
entreeX = 0
entreeY = nombre aléatoire entre 1 et hauteur - 2
grille[entreeX][entreeY] = ENTREE
```

Le premier chemin commence dans la cellule immédiatement à droite de l’entrée. Cette cellule est transformée en chemin,
ce qui permet au générateur de progresser vers le centre du labyrinthe.

```
startX = entreeX + 1
startY = entreeY
grille[startX][startY] = CHEMIN
```

Pour créer un labyrinthe parfait (un seul chemin entre n’importe quelles cases, aucune boucle), on utilise une
exploration en profondeur (DFS).
On utilise une pile (structure LIFO), car elle permet d’explorer une branche jusqu’au bout avant de revenir en arrière,
ce qui donne un labyrinthe sans croisements inutiles.

On utilise également un tableau 2D de booleens `visite[][]` pour mémoriser quelles cellules ont déjà été visitées.

Contrairement à un DFS classique, on se déplace de deux cellules à la fois pour forcer des couloirs séparés par des
murs. Entre chaque déplacement de deux cases, on casse la cellule intermédiaire.

```
Créer visite[][] initialisé à FAUX
Créer pile vide

Empiler (startX, startY)
visite[startX][startY] = VRAI

directions possibles = 4 directions espacées de 2 cases

Tant que la pile n’est pas vide
  (cx, cy) = sommet de la pile

  chercher parmi les 4 directions un voisin (nx, ny) situé 2 cases plus loin
  qui est dans la grille et non visité

  si un ou plusieurs voisins valides existent
    choisir une direction au hasard
    casser le mur entre les deux cellules :
      grille[cx + dx/2][cy + dy/2] = CHEMIN
    déplacer vers la nouvelle cellule :
      grille[nx][ny] = CHEMIN
      visite[nx][ny] = VRAI
      empiler (nx, ny)
  sinon
    dépiler
Fin Tant que
```

Cette méthode garantit que le labyrinthe final est parfait : aucune boucle, et toutes les zones sont atteignables.

Après avoir généré toutes les cases, il faut placer la sortie. L’objectif est que la sortie soit à une distance minimum
donnée de l’entrée.
Pour cela, on calcule d’abord la distance de chaque case au point de départ en utilisant un parcours en largeur (BFS).

On utilise une file (FIFO) car BFS explore couche par couche et permet de calculer les distances minimum en nombre de
pas depuis l’entrée.
On stocke les distances dans un tableau `distances[][]`.

```
Créer distances[][] initialisé à -1
Créer visite[][] initialisé à FAUX

Créer file vide
Distances[entreeX][entreeY] = 0
Visite[entreeX][entreeY] = VRAI
Ajouter (entreeX, entreeY) dans la file

Tant que la file n’est pas vide
  (cx, cy) = retirer premier de la file

  pour chaque direction (haut, bas, gauche, droite)
    nx = cx + dx
    ny = cy + dy

    si dans la grille et pas visité
      si cellule est CHEMIN ou ENTREE
        visite[nx][ny] = VRAI
        distances[nx][ny] = distances[cx][cy] + 1
        ajouter (nx, ny) dans file
Fin Tant que

Retourner distances
```

Une fois toutes les distances connues, on choisit une cellule qui est :

* un chemin
* située à une distance d’au moins `distanceMin`
* située à une distance d’au plus `distanceMin + 5`

On recherche toutes les cellules valides dans cette plage puis on en choisit une au hasard comme sortie.

```
distanceMax = distanceMin + 5
candidats = liste vide

Pour chaque cellule (x,y) de la grille
  si cellule est CHEMIN
    si distances[x][y] >= distanceMin et <= distanceMax
      ajouter (x,y) à candidats
```

Si une ou plusieurs cellules sont valides, on en prend une au hasard :

```
si candidats non vide
  choisir une cellule au hasard
  grille[x][y] = SORTIE
```

Sinon, si aucune ne remplit la condition, on prend la cellule dont la distance est la plus proche possible de la
distance minimale (meilleur compromis).

```
sinon
  parcourir toutes les cellules accessibles
  choisir celle dont la distance est la plus proche de distanceMin
  grille[x][y] = SORTIE
```

Une fois la sortie placée, le labyrinthe parfait est terminé et on peut le retourner en résultat.

```
Retourner grille
Fin
```

Pour la generation de labyrinthe parfait, il est utilisé aussi dans 2 classes qui sont les mêmes que pour la génération
de labyrinthe aléatoire.

# 4. Efficacité et Mesures

Pour évaluer l'efficacité de nos algorithmes de génération de labyrinthes, nous avons mis en place une classe TestPerformance
qui exécute plusieurs tests de génération avec des tailles et des pourcentages de murs variés.

![Classe TestPerformance](/TestPerfo.png)

## Conclusion sur nos résultats et la structure du TP2 

Notre structure à base de Cellule (polymorphisme) offre une meilleure
extensibilité et maintenabilité meme si cela vaut une légère surcharge mémoire.

Les deux tableaux de booléens sont plus performants en termes de
mémoire et de vitesse d'accès brute, mais beaucoup moins flexibles aux changements.

Pour un projet évolutif notre structure avec les cellules est largement préférable .


# 5. Complément et Bilan

## Complément

### 1. **Optimisation des performances graphiques**

Nous avons essayé d'optimiser les performances d'affichage au maximum pour éviter de recharger en memoire les textures
des objets du labyrinthe. Pour cela dans les classes `Mur`, `Chemin`, `Sortie` et l'image du joueur sont chargé une
seule fois lors que l'utilisateur lance un labyrinthe. Ceci nous permet de reduire la consomation de mémoire et
d'améliorer la fluidité de l'affichage du labyrinthe.

### 2. **Utilisation d'algorithmes efficaces**

Nous avons utilisé des algorithmes qui ont fait leur preuve comme le BFS (Breadth-First Search) dans la classe
`Pathfinder`, pour calculer le plus court chemin dans un labyrinthe. Pour cela, nous avons donc utilisé une `LinkedList`
pour effectuer un parcours en largeur.

### 3. **Séparation des responsabilités et architecture modulaire**

Pour gérer les différents modes de vision du labyrinthe, nous avons implémenté une interface. Dans les classes
`VisionLabyrinthe` et `VisionFactory`, chaque type de vision (`VisionLibre`, `VisionLocale`, `VisionLimitee`,
`VisionCarte`, `VisionAveugle`, `VisionCle`) est instancié dynamiquement. Cela nous permet d'ajouter facilement de
nouveaux modes de vision sans modifier le code existant.

### 4. **Pattern Observer**

Nous avons mis en place le des Observer dans les classes `Labyrinthe`, `LabyrintheObserver` et `JeuControleur` pour
synchroniser automatiquement l'interface graphique avec le modèle. Dès qu'une modification est faite dans le labyrinthe,
tous les observateurs sont notifiés et mettent à jour leur affichage. Cela évite les vérifications constantes de l'état
et améliore les performances.

### 5. **Utilisation de HashMap**

Pour optimiser les temps d'accès aux données, nous avons utilisé des `HashMap` dans les classes `JSONRepository`,
`VisionFactory` et `InventaireJoueur`. Cette structure nous permet un accès en rapide pour retrouver un joueur par son
pseudo ou vérifier si un cosmétique est équipé, sans avoir à parcourir des listes.

### 6. **Enums**

Nous avons utilisé des énumérations dans les classes `Vision`, `ModeJeu`, `TypeLabyrinthe` et `TypeCosmetique` pour
représenter les constantes du projet. Les enums apportent une sécurité au niveau du typage et évitent les erreurs de
saisie. Cela facilite aussi la maintenance du code.

### 7. **Séparation de la logique de calcul**

Pour le calcul du score des joueurs, nous avons créé la classe `CalculateurScore` qui centralise toute la logique. Cette
séparation permet de modifier les règles de calcul du score sans toucher au code du modèle ou des contrôleurs. Cela
facilite aussi les tests unitaires de cette fonctionnalité.

### 8. **Persistance des données**

Nous avons utilisé Gson pour permettre la persistance des données de notre application, ceci nous permet de
sérialisation/désérialisation la progression des joueurs ainsi que leurs inventaires cosmétiques dans des fichiers json.

## Bilan

### Difficultés rencontrées

1. **Génération de labyrinthes** : Nous avons du testé beaucoup de chose pour d'abord avoir une génération qui permet d'
   être sûr d'avoir un chemin et en suite d'avoir une génération qui resemble plus à de longs couloirs.

2. **Gestion des différentes visions** : L'implémentation des multiples modes de vision (locale, limitée, aveugle, etc.)
   a demandé un refactor important pour éviter la duplication de code.

3. **Persistance des données** : La sérialisation/désérialisation des inventaires et des progressions des joueurs avec
   Gson.

4. **Synchronisation Vue/Modèle** : Maintenir la cohérence entre l'état du modèle (labyrinthe) et l'affichage graphique,
   particulièrement lors des déplacements et des changements d'état.

### Améliorations

1. **Principe de responsabilité unique** : Certaines classes comme `JeuControleur` gèrent encore trop de
   responsabilités (initialisation, gestion des événements, coordination des rendus).

2. **Duplication de code** : Des méthodes similaires existent dans plusieurs classes et pourraient être factorisées dans
   une classe utilitaire commune.

3. **Tests unitaires** : La couverture des tests pourrait être améliorée.

4. **Cohérence de langue** : Nous utilisons du français et de l'anglais pour le nom nos classes, méthodes et attribut.