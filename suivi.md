# Suivi du Travail effectué

## Semaine 40

Réalisation des fiches descriptives des CU pour le rendu analyse :

- Fernandes Bastien a réalisé celle de Lancer mode progression
- Dewaele Enzo a réalisé celle d'afficher la progression
- Hamiti Edi a réalisé celle de charger un profil
- Vanhoutte Amaury a réalisé celle de déplacer le joueur

Nous avons aussi réalisé ensemble une première version de diagramme de classes.

## Semaine 41

Nous avons mis en commun nos idées afin d'améliorer notre diagramme de classes grâce aux conseils du professeur Antoine
NONGAILLARD. Nous avons aussi créé le diagramme de cas d'utilisation

Et nous avons aussi créé un projet partagé sur le site figma pour réaliser la maquette du projet, suite à ça nous avons
fait une base de notre maquette.

## Semaine 42

Nous avons réalisé le diagramme de classes final, et aussi fini la maquette qui étaient tous deux demandés dans le rendu
qui était en fin de semaine.

## Semaine 43

Durant cette semaine, nous avons réalisé la plupart de notre jeu **"Le jeu des Labyrinthes"**.

Liste des travaux de la semaine :

- Mis à jour le pom.xml pour nous permettre de lancer notre application. (Edi)
- Mise en place des modèles. (Amaury, Bastien, Edi, Enzo)
- Création de test unitaire pour nos modèles. (Amaury, Bastien, Edi, Enzo)
- Implémentation de notre algorithme de génération de labyrinthe ainsi que le calcule du plus court chemin (Dijkstra). (
  Amaury)
- Création d'un système de sauvegarde. (Enzo)
- Création de notre première vue HomePage, contenu plus style. (Amaury, Bastien, Edi, Enzo)
- Refactor de notre partie JavaFX en fxml. (Enzo, Edi)
- Création de la vue Jeu et de son contrôleur, qui nous permet de voir pour la première fois notre labyrinthe dans
  JavaFX. (Amaury, Bastien, Edi, Enzo)
- Implémentation des déplacements du joueur grâce à des boutons et les touches du clavier. (Enzo)
- Séparation des vues et contrôleurs, car nous avions mélangé les deux au début. (Amaury, Bastien, Edi, Enzo)
- Page de choix de l'étape et defi. (Bastien)
- Ajout progressif de formulaires et de bouton qui permet l'initialisation du jeu avec un labyrinthe et un joueur. (Edi)
- Transformation de notre modèle Jeu en singleton, pour nous permettre de stocker et récupéré toutes les données utiles
  au jeu. (Enzo)
- Création de la vue locale ainsi que le minimap. (Enzo)
- Amélioration de notre algorithme de génération. (Bastien, Amaury)
- Amélioration de l'expérience utilisateur. (Amaury, Bastien, Edi, Enzo)

## Semaine 44

- Refactorisation du code pour qu'il accepte différents types de générateurs de labyrinthes. (Edi)
- Modification du calcul des points pour qu'il prenne en note le temps pris à finir un labyrinthe. (Amaury)

## Semaine 45

- Ajout de l'étape 4 et 5 (sans leurs vues). (Edi)
- Fix de bugs avec la génération de labyrinthe suite au refactor précedent. (Edi)
- Petits ajustements. (Amaury, Bastien, Edi, Enzo)

## Semaine 46

Durant cette semaine, nous avons principalement entamé de refactorisation du code afin qu'il adhère plus aux
principes SOLID. Nous avons aussi commencé à développer la boutique de cosmétique

- Optimisations des méthodes de chargement des textures. (Enzo, Edi)
- Fix de bugs en rapport avec la sauvegarde. (Edi)
- Ajout de la vue de l'étape 5. (Amaury)
- Refactorisation du code pour qu'il adhère plus aux principes SOLID. (Edi, Enzo, Amaury, Bastien)
  - Extraction du calcul du plus court chemin dans une classe dédiée.
  - Ajout d'un système de vision avec VisionFactory avec une enum et les implémentations spécifiques pour chaque type de
    vision.
  - Extraction de la logique de fin de partie dans une classe dédiée
- Début du développement de la boutique de cosmétique. (Enzo)
- Ajustements mineurs. (Amaury, Bastien, Edi, Enzo)
- Début du développement sur la vue limitée. (Amaury)
- Ajout d'un leaderboard. (Bastien)
- Suppression de patterns Singleton utilisés dans certaines classes. (Edi)

## Semaine 47

- Mise à jour de la documentation. (Amaury, Bastien, Edi, Enzo)
- Mise à jour de la génération de labyrinthe parfaits pour qu'il prenne en compte la distance minimale entre l'entrée et la sortie. (Amaury)
- Mise à jour de la gestion des défis avec un nouveau format JSON. (Enzo)
- Mise à jour des calculs de points pour prendre en compte le nombre de déplacements. (Amaury)
- Suppression de patterns Singleton utilisés dans certaines classes. (Edi, Bastien)
- Fix de bugs divers. (Amaury, Bastien, Edi, Enzo)
- Nettoyage du code afin de faciliter la maintenance. (Bastien)
- Ajouts de différentes textures pour la boutique. (Enzo, Edi)
- Migration vers un système de routage centralisé avec la classe `Router` (utilisation d'une pile centralisée pour le routage entre les différentes vues). (Edi)
- Suppression d'utilisations de `Properties` JavaFX dans le modele (passage vers un pattern Observeur/Observable)
- Ajout d'un mode de jeu "Chasse à la Clé" avec une sortie vérouillée. (Amaury)
- Ajout d'une limite de mouvements pour le mode progression. (Bastien, Enzo)
- Ajout d'une étape 7 : "Étape Aveugle" où le joueur doit trouver le labyrinthe sans le voir, seulement avec le son. (Edi)