package modele;

import java.time.Duration;
import java.time.LocalTime;


/**
 * Classe représentant un chronomètre pour mesurer la durée d'une partie.
 * Permet de démarrer, arrêter et récupérer la durée écoulée.
 */
public class GameTimer {
    private LocalTime start;
    private LocalTime end;


    /**
     * Démarre le chronomètre en enregistrant l'heure actuelle.
     */
    public void startTimer() {
        this.start = LocalTime.now();
    }


    /**
     * Arrête le chronomètre en enregistrant l'heure actuelle.
     */
    public void endTimer() {
        this.end = LocalTime.now();
    }


    /**
     * Retourne la durée écoulée entre le démarrage et l'arrêt du chronomètre.
     *
     * @return la durée écoulée
     */
    public Duration getDuration() {
        return Duration.between(start, end);
    }


    /**
     * Vérifie si le chronomètre est actuellement en cours d'exécution.
     *
     * @return true si le chronomètre a été démarré et n'est pas encore arrêté, false sinon
     */
    public boolean isRunning() {
        return this.start != null && this.end == null;
    }
}
