package modele.boutique;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Représente l'inventaire d'un joueur contenant son score, ses cosmétiques possédés et équipés.
 * Cette classe gère la collection de cosmétiques du joueur et leur équipement.
 */
public class InventaireJoueur {

    private int score;
    private final Set<String> cosmetiquesPossedes;
    private final Map<TypeCosmetique, String> cosmetiquesEquipes;

    /**
     * Construit un nouvel inventaire pour un joueur avec un score initial.
     *
     * @param scoreInitial le score de départ du joueur
     */
    public InventaireJoueur(int scoreInitial) {
        this.score = scoreInitial;
        this.cosmetiquesPossedes = new HashSet<>();
        this.cosmetiquesEquipes = new HashMap<>();
    }

    /**
     * @return le score actuel du joueur
     */
    public int getScore() {
        return score;
    }

    /**
     * Définit le score du joueur.
     *
     * @param score le nouveau score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Ajoute un cosmétique à l'inventaire du joueur.
     *
     * @param idCosmetique l'identifiant du cosmétique à ajouter
     */
    public void ajouterCosmetique(String idCosmetique) {
        cosmetiquesPossedes.add(idCosmetique);
    }

    /**
     * Vérifie si le joueur possède un cosmétique spécifique.
     *
     * @param idCosmetique l'identifiant du cosmétique à vérifier
     * @return true si le joueur possède ce cosmétique, false sinon
     */
    public boolean possedeCosmetique(String idCosmetique) {
        return cosmetiquesPossedes.contains(idCosmetique);
    }

    /**
     * @return une copie de l'ensemble des identifiants des cosmétiques possédés
     */
    public Set<String> getCosmetiquesPossedes() {
        return new HashSet<>(cosmetiquesPossedes);
    }

    /**
     * Équipe un cosmétique pour un type donné.
     *
     * @param type         le type de cosmétique à équiper
     * @param idCosmetique l'identifiant du cosmétique à équiper
     */
    public void equiperCosmetique(TypeCosmetique type, String idCosmetique) {
        cosmetiquesEquipes.put(type, idCosmetique);
    }

    /**
     * Récupère l'identifiant du cosmétique équipé pour un type donné.
     *
     * @param type le type de cosmétique
     * @return l'identifiant du cosmétique équipé, ou null si aucun n'est équipé
     */
    public String getCosmetiqueEquipe(TypeCosmetique type) {
        return cosmetiquesEquipes.get(type);
    }

    /**
     * @return une copie de la map des cosmétiques équipés par type
     */
    public Map<TypeCosmetique, String> getCosmetiquesEquipes() {
        return new HashMap<>(cosmetiquesEquipes);
    }

}
