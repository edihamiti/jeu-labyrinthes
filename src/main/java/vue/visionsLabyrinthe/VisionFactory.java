package vue.visionsLabyrinthe;

import java.util.HashMap;
import java.util.Map;
import modele.Vision;

public class VisionFactory {
    private static final Map<Vision, VisionLabyrinthe> strategies = new HashMap<>();

    static {
        strategies.put(Vision.VUE_LIBRE, new VisionLibre());
        strategies.put(Vision.VUE_LOCALE, new VisionLocale());
        strategies.put(Vision.VUE_LIMITEE, new VisionLimitee());
        strategies.put(Vision.VUE_CARTE, new VisionCarte());
        strategies.put(Vision.VUE_AVEUGLE, new VisionAveugle());
        strategies.put(Vision.VUE_CLE, new VisionCle());
    }

    public static VisionLabyrinthe getStrategy(Vision vision, int porteeVision) {
        VisionLabyrinthe strategy = strategies.get(vision);
        if (strategy == null) {
            System.out.println("Pas réussi à trouver la vision pour " + vision);
            return strategies.get(Vision.VUE_LIBRE);
        }
        System.out.println("On a trouvé la stratégie!!! : " + strategy.getClass().getSimpleName());
        return strategy;
    }
}
