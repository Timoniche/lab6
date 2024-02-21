package lab5;

import java.util.concurrent.atomic.AtomicReference;

public class BestFitnessConcurrent implements BestFitness {
    private final AtomicReference<Double> bestRun = new AtomicReference<>(0.0);

    public void update(double newFitness) {
        Double curFitness;
        do {
            curFitness = bestRun.get();
        } while (curFitness < newFitness && !bestRun.compareAndSet(curFitness, newFitness));
    }

    public double getBestRun() {
        return bestRun.get();
    }
}
