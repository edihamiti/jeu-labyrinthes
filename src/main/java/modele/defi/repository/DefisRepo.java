package modele.defi.repository;

import modele.defi.Defi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Classe représentant un dépôt de défis.
 *
 * Cette classe permet de stocker et gérer une collection de défis
 * ({@link Defi}) pour le jeu. Elle fournit des méthodes pour :
 * - ajouter un défi au dépôt
 * - récupérer la liste des défis
 * - obtenir une map associant chaque défi à son état de progression (initialisé à false)
 *
 * Exemple d'utilisation :
 * DefisRepo repo = new DefisRepo();
 * repo.ajouterDefi(monDefi);
 * List<Defi> tousLesDefis = repo.getDefisRepo();
 * HashMap<Defi, Boolean> progression = repo.getMapsDefis();
 */
public class DefisRepo {

    private final List<Defi> defisRepo;


    /**
     * Constructeur.
     * Initialise un dépôt vide de défis.
     */
    public DefisRepo() {
        this.defisRepo = new ArrayList<>();
    }



    /**
     * Ajoute un défi au dépôt.
     *
     * @param defi défi à ajouter
     */
    public void ajouterDefi(Defi defi) {
        defisRepo.add(defi);
    }


    /**
     * Retourne une map associant chaque défi à son état de progression.
     * Par défaut, chaque défi est initialisé à false (non complété).
     *
     * @return HashMap où la clé est le défi et la valeur est un boolean indiquant
     *         s'il a été complété
     */
    public HashMap<Defi, Boolean> getMapsDefis() {
        HashMap<Defi, Boolean> progression = new HashMap<>();
        defisRepo.forEach(defi -> progression.put(defi, false));
        return progression;
    }


    /**
     * Retourne la liste complète des défis du dépôt.
     *
     * @return liste de tous les défis
     */
    public List<Defi> getDefisRepo() {
        return defisRepo;
    }
}
