package boutique.repository;

import boutique.modele.Cosmetique;
import boutique.modele.TypeCosmetique;

import java.util.List;
import java.util.Optional;

/**
 * Interface définissant le contrat pour accéder aux cosmétiques disponibles.
 * Permet de récupérer les cosmétiques par différents critères.
 */
public interface IDepotCosmetique {

    /**
     * Récupère tous les cosmétiques disponibles.
     *
     * @return la liste de tous les cosmétiques
     */
    List<Cosmetique> obtenirTous();

    /**
     * Récupère tous les cosmétiques d'un type spécifique.
     *
     * @param type le type de cosmétique recherché
     * @return la liste des cosmétiques du type donné
     */
    List<Cosmetique> obtenirParType(TypeCosmetique type);

    /**
     * Recherche un cosmétique par son identifiant.
     *
     * @param id l'identifiant du cosmétique
     * @return un Optional contenant le cosmétique s'il existe, vide sinon
     */
    Optional<Cosmetique> obtenirParId(String id);

}
