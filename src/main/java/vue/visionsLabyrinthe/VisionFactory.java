package vue.visionsLabyrinthe;

import java.util.HashMap;
import java.util.Map;
import modele.Vision;

public class VisionFactory {
    private static final Map<Vision, VisionLabyrinthe> strategies = new HashMap<>();

    static {
        strategies.put(Vision.VUE_LIBRE, new VisionLibre());
        strategies.put(Vision.VUE_LOCAL, new VisionLocale());
        strategies.put(Vision.VUE_LIMITEE, new VisionLimitee());
    }

    public static VisionLabyrinthe getStrategy(Vision vision) {
        return strategies.get(vision);
    }
}
