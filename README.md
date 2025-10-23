# Le jeu des Labyrinthes

> Un jeu de labyrinthe 2D captivant développé en JavaFX. Relevez les défis du mode progression ou créez vos propres
> labyrinthes à la volée grâce au mode libre entièrement personnalisable.




![Java Version](https://img.shields.io/badge/Java-24%2B-blue?logo=java)
![Build Tool](https://img.shields.io/badge/Build-Maven-red?logo=apache-maven)

## Table des matières

- [À propos](#-à-propos)
- [Technologies utilisées](#️-technologies-utilisées)
- [Prérequis](#-prérequis)
- [Installation et Lancement](#-installation-et-lancement)
- [Utilisation](#-utilisation)
- [Captures d'écran](#-captures-décran)
- [Auteurs](#-auteurs)

## À propos

Ce projet est une **Situation d'Apprentissage et d'Évaluation (SAE)** réalisée dans le cadre de notre deuxième année de
**BUT Informatique à l'IUT de Lille**.

L'objectif était de concevoir et développer un jeu en **Java** avec l'interface graphique **JavaFX** et de la gérer
avec **Maven**. Le résultat est un jeu de labyrinthe fonctionnel proposant deux expériences de jeu distinctes.

### Fonctionnalités principales

* **Mode Progression :**
    * Enchaînez une série de niveaux avec une difficulté croissante.

* **Mode Libre :**
    * Générez des labyrinthes à la demande.
    * Personnalisez entièrement votre partie :
        * **Taille :** Choisissez la largeur et la hauteur de la grille.
        * **Densité :** Ajustez le pourcentage de murs pour des labyrinthes plus ou moins complexes.
        * **Mode de vision :** "Brouillard de guerre", "Vue complète".

## Auteurs

Ce projet a été réalisé par une équipe de 4 étudiants :

* **VANHOUTTE Amaury** - `amaury.vanhoutte.etu@univ-lille.fr`
* **FERNANDES Bastien** - `bastien.fernandes.etu@univ-lille.fr`
* **DEWAELE Enzo** - `enzo.dewaele.etu@univ-lille.fr`
* **HAMITI Edi** - `edi.hamiti.etu@univ-lille.fr`

## Technologies utilisées

* **Langage :** Java 24
* **Build Tool :** Maven
* **Frameworks :** JavaFX
* **Tests :** JUnit 5
* **Autres :** Git, Gitlab, Figma

## Prérequis

Pour compiler et exécuter ce projet localement, vous aurez besoin des outils suivants, quel que soit votre système
d'exploitation :

* **Java Development Kit (JDK)** : Version `24+` ou supérieure.
    * Nous recommandons [**Oracle**](https://www.oracle.com/java/technologies/javase/jdk24-archive-downloads.html). Le
      site propose des
      installateurs simples (`.msi` pour Windows, `.pkg` pour macOS, `.deb`/.`rpm` pour Linux).
    * *Note : Vous n'avez **pas** besoin d'installer JavaFX séparément. Maven s'occupera de télécharger les bonnes
      bibliothèques pour votre OS lors de la compilation.*

* **Apache Maven** : Version `3.9.*` ou supérieure.
    * Maven est l'outil qui va compiler le code et gérer toutes les dépendances (y compris JavaFX).
    * **Installation :**
        * **Méthode 1 (Manuelle) :** Téléchargez l'archive binaire (`.zip`) depuis
          le [site officiel de Maven](https://maven.apache.org/download.cgi) et suivez
          le [guide d'installation officiel](https://maven.apache.org/install.html) pour configurer vos variables
          d'environnement (`PATH`).
        * **Méthode 2 (Recommandée, via un gestionnaire de paquets) :**
            * **Sur Windows** (avec [Chocolatey](https://chocolatey.org/)) : `choco install maven`
            * **Sur macOS** (avec [Homebrew](https://brew.sh/)) : `brew install maven`
            * **Sur Linux (Ubuntu/Debian)** : `sudo apt install maven`
            * **Sur Linux (Fedora/RHEL)** : `sudo dnf install maven`

* **(Optionnel) Un IDE**
    * Ce projet est configuré pour être importé facilement. [**IntelliJ IDEA**](https://www.jetbrains.com/idea/) (
      versions Community ou Ultimate) est parfait.
    * Le site de téléchargement propose des installateurs pour Windows, macOS et Linux. Il vous suffira ensuite d'ouvrir
      le fichier `pom.xml` pour importer le projet, et l'IDE détectera et configurera tout automatiquement.

## Installation et Lancement

### 1. Cloner le dépôt (via GitLab)

Ouvrez votre terminal ou Git Bash et exécutez la commande suivante :

```bash
git clone https://gitlab.univ-lille.fr/sae302/2025/I5_SAE3.3.git

# Naviguez dans le dossier nouvellement créé
cd I5_SAE3.3
```

### 2. Lancement par ligne de commande

```bash
# C'est la commande qui lance l'application JavaFX via Maven
mvn javafx:run
# Maven va télécharger les dépendances, compiler le code, puis lancer l'application automatiquement.