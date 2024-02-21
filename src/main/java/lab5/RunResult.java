package lab5;

public class RunResult {
    private final double fit;
    private final long timeMs;

    public RunResult(double fit, long timeMs) {
        this.fit = fit;
        this.timeMs = timeMs;
    }

    public double getFit() {
        return fit;
    }

    public long getTimeMs() {
        return timeMs;
    }
}
