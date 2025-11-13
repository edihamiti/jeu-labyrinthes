package vue;

import javafx.scene.media.AudioClip;

public class SoundManager {
    public static void playSound(String soundName) {
        try {
            AudioClip audio = new AudioClip(
                    SoundManager.class.getResource("/sounds/" + soundName).toExternalForm()
            );
            audio.play();
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture du son: " + soundName);
        }
    }
}
