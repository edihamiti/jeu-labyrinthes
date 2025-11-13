package modele;

import java.time.Duration;
import java.time.LocalTime;

public class GameTimer {
    private LocalTime start;
    private LocalTime end;

    public void startTimer() {
        this.start = LocalTime.now();
    }

    public void endTimer() {
        this.end = LocalTime.now();
    }

    public Duration getDuration() {
        return Duration.between(start, end);
    }
}
