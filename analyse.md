# Le Jeu des Labyrinthes

## Équipe et organisation du travail

Équipe I5:
- Fernandes Bastien : A contribué à la fiche descriptive de lancer mode progression
- Dewaele Enzo : A contribué à la fiche descriptive d'afficher la progression
- Hamiti Edi : A contribué à la fiche descriptive de charger un profil
- Vanhoutte Amaury : A contribué à la fiche descriptive de déplacer le joueur

## Cas d’utilisation

Inclure le diagramme de cas d’utilisation, par exemple sous forme de capture d’écran.

Inclure des fiches descriptives et des prototypes d’interface pour ces fonctionnalités:

## Fiche descriptive

### Charger un profil
Système : Labyrinthes 

CU : Charger un profil

Acteur Principal : Joueuse

Acteur Secondaire : Système

Pré-conditions :
* Avoir un profil déjà sauvegarder
* La joueuse à déjà lancé le mode progression

Garantie en cas de succès :
* Un profil est chargé

Garantie minimale :
* Si le scénario n’aboutit pas, l’état du système reste inchangé

Scénario nominal : 
1. La joueuse sélectionne “Charger un profile”
2. Le système affiche la liste des profiles à charger
3. La joueuse sélectionne le profile à charger
4. Le système charge le profile
   
Scénario alternatif :
A: Dans l'étape 4, il y a une erreur durant le chargement du profile
* 4(A) : Le système affiche une notification et reviens à l'étape 2 du scénario nominal



### Afficher la progression
Système : Labyrinthes

CU : Afficher la progression

Acteur Principal : Système

Acteur Secondaire : Utilisateur

Pré-condition : 
 * Lancer le mode progression

Garantie en cas de succès : 
 * Afficher la progression

Garantie minimale :
 * Rien ne se passe

Scénario nominal : 
1. L’utilisateur lance le mode progression
2. Système affiche la progression


### Lancer mode progression

Système : Labyrinthes

CU : Lancer le Mode Progression

Acteur Principal : Utilisateur

Acteur Secondaire : Système

Pré-condition :
* L’utilisateur choisi le mode progression

Garantie en cas de succès :
* Le labyrinthe s’affiche et l’utilisateur peut jouer

Garantie minimale :
* Rien ne se passe

Scénario nominal : 
1. L’utilisateur choisi une nouvelle partie
2. Le système affiche les étapes et les défis 
3. L’utilisateur choisi une étape et un défi 
4. Le système affiche le labyrinthe

Scénario alternatif : 
1. L’utilisateur choisi de charger une partie (retour étape 2 scénario nominal)




### Déplacer joueur

Système : Labyrinthes
                                                                                                                                                                                 
CU : Déplacer le joueur

Acteur Principal : Joueur

Acteur Secondaire : Système

Pré-condition : 

* être en jeu

Garantie en cas de succès : 

* Le joueur se déplace dans le labyrinthe
                                                                                
Garantie Minimal : 

* Rien ne se passe, le joueur ne se déplace pas

Scénario nominal : 
1. le joueur utilise les flèches du clavier pour se déplacer
2. le système vérifie le futur déplacement du joueur et le déplace le joueur sur la case souhaité

Scénario alternatif : 
1. le système ne valide pas le futur déplacement du joueur (retour étape 1 scénario nominal)

### Diagramme de classes
<!-- 
Inclure un diagramme de classes qui permet d’implémenter toutes les fonctionnalités pour le Jalon 1. Le diagramme sera construit suivant la méthode vue on analyse. L’image du diagramme doit être de résolution suffisante permettant de zoomer et lire le texte qui y figure.

Chaque prototype d’interface est constitué d’une suite d’écrans, ou d’une arborescence d’écrans si plusieurs chemins d’interaction sont possibles. Les prototypes peuvent être en faible fidélité. Ils peuvent être dessinés à la main ou générés en utilisant un logiciel.
Des images des prototypes seront inclus dans le rapport (pas de lien externe). Les images doivent être lisibles et avec une résolution suffisante pour qu’on puisse zoomer et lire le texte qui s’y trouve. -->
