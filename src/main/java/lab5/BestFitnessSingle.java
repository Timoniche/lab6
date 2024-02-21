package lab5;

public class BestFitnessSingle implements BestFitness {
    private double bestRun = 0.0;

    public void update(double fitness) {
        if (fitness > bestRun) {
            bestRun = fitness;
        }
    }

    public double getBestRun() {
        return bestRun;
    }
}
