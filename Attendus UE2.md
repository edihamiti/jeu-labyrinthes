# 1. Investissement dans le projet

Groupe I5 :

* **Edi HAMITI** - `153 commits` - `37.9%`
* **Enzo DEWAELE** - `111 commits` - `27.5%`
* **Amaury VANHOUTTE** - `70 commits` - `17.3%`
* **Bastien FERNANDES** - `70 commits` - `17.3%`

*pourcentage d'investissement calculé par le nombre de commits.


# 3 Algorithmes de génération

## Labyrinthe Aléatoire

On commence par créer une grille en tableau 2D, car cela permet de représenter le labyrinthe de façon simple, avec un accès direct aux cellules. Chaque cellule est d’abord un mur.


```
Créer grille[largeur][hauteur]

Pour x de 0 à largeur - 1
  Pour y de 0 à hauteur - 1
    grille[x][y] = MUR
```


On place ensuite une entrée dans une position définie. On choisit une coordonnée fixe pour simplifier, et c’est à partir de cette case que la création du chemin principal va commencer.


```
entreeX = 0
entreeY = 1
grille[entreeX][entreeY] = ENTREE
```


Pour garantir qu’il existe un chemin entre l’entrée et la sortie, on utilise une exploration en profondeur (DFS). Cette méthode est adaptée car elle avance le plus loin possible avant de revenir en arrière. Pour cela, on utilise une pile (LIFO), ce qui permet de revenir automatiquement aux cellules précédentes lorsqu’on ne peut plus avancer.
Chaque cellule visitée est marquée comme chemin, donc à la fin on a forcément un chemin continu depuis l’entrée jusqu’à la dernière cellule visitée, qui deviendra la sortie.


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


Des chemins secondaires peuvent être créés pour rendre le labyrinthe moins linéaire. On choisit certaines cellules du chemin principal et on creuse à partir d’elles tant qu’il existe des murs voisins disponibles.
Ces chemins ne remplacent pas le chemin principal, ils l’enrichissent simplement.


```
Pour chaque cellule de la grille
  si la cellule est un chemin et x mod 10 = 0
    CreerCheminSecondaire(x, y)
```

La procédure suivante crée un chemin secondaire tant qu’il est possible d’avancer. Elle reste simple : tant qu’il existe des cellules voisines encore murées, on avance dans une direction choisie au hasard.

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


Pour obtenir le pourcentage de murs demandé par l’utilisateur, on calcule combien de cases internes devraient être des chemins. Ensuite, tant que le total actuel de chemins est insuffisant, on choisit une cellule au hasard et si c’est un mur, on la transforme en chemin.
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

On utilise une grille en tableau 2D pour représenter le labyrinthe, car cela permet d’accéder à chaque case directement et facilement en fonction de ses coordonnées. On remplit d’abord toute la grille avec des murs.


```
Créer grille[largeur][hauteur]

Pour x de 0 à largeur - 1
  Pour y de 0 à hauteur - 1
    grille[x][y] = MUR
```


On place ensuite une entrée dans la première colonne, mais à une position verticale choisie aléatoirement. Cela permet d’avoir des labyrinthe variés à chaque génération.


```
entreeX = 0
entreeY = nombre aléatoire entre 1 et hauteur - 2
grille[entreeX][entreeY] = ENTREE
```


Le premier chemin commence dans la cellule immédiatement à droite de l’entrée. Cette cellule est transformée en chemin, ce qui permet au générateur de progresser vers le centre du labyrinthe.


```
startX = entreeX + 1
startY = entreeY
grille[startX][startY] = CHEMIN
```



Pour créer un labyrinthe parfait (un seul chemin entre n’importe quelles cases, aucune boucle), on utilise une exploration en profondeur (DFS).
On utilise une pile (structure LIFO), car elle permet d’explorer une branche jusqu’au bout avant de revenir en arrière, ce qui donne un labyrinthe sans croisements inutiles.

On utilise également un tableau 2D de booleens `visite[][]` pour mémoriser quelles cellules ont déjà été visitées.

Contrairement à un DFS classique, on se déplace de deux cellules à la fois pour forcer des couloirs séparés par des murs. Entre chaque déplacement de deux cases, on casse la cellule intermédiaire.


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



Après avoir généré toutes les cases, il faut placer la sortie. L’objectif est que la sortie soit à une distance minimum donnée de l’entrée.
Pour cela, on calcule d’abord la distance de chaque case au point de départ en utilisant un parcours en largeur (BFS).

On utilise une file (FIFO) car BFS explore couche par couche et permet de calculer les distances minimum en nombre de pas depuis l’entrée.
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

Sinon, si aucune ne remplit la condition, on prend la cellule dont la distance est la plus proche possible de la distance minimale (meilleur compromis).

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

Pour la generation de labyrinthe parfait, il est utilisé aussi dans 2 classes qui sont les mêmes que pour la génération de labyrinthe aléatoire.
