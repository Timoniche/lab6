package lab5;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;

public class MyFactory extends AbstractCandidateFactory<double[]> {
    public static double MIN_X_DEFINITION = -5.0;
    public static double MAX_X_DEFINITION = 5.0;
    private final int dimension;

    public MyFactory(int dimension) {
        this.dimension = dimension;
    }

    public double[] generateRandomCandidate(Random random) {
        double[] solution = new double[dimension];

        for (int i = 0; i < dimension; i++) {
            solution[i] = generateDoubleBetween(MIN_X_DEFINITION, random, MAX_X_DEFINITION);
        }

        return solution;
    }

    public static double generateDoubleBetween(double rangeMin, Random random, double rangeMax) {
        return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
    }
}

