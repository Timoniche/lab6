package lab5;

import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lab5.MyFactory.MAX_X_DEFINITION;
import static lab5.MyFactory.MIN_X_DEFINITION;
import static lab5.MyFactory.generateDoubleBetween;

public class MyMutation implements EvolutionaryOperator<double[]> {
    public static double UNIFORM_MUTATE_COEF = 0.05;
    public static double GAUSSIAN_MUTATE_COEF = 0.1;
    public static double GAUSSIAN_DEVIATION_COEF = 0.5;

    public List<double[]> apply(List<double[]> population, Random random) {
        double prob = random.nextDouble();
        if (prob <= 0.5) {
            return uniformExplorationMutation(population, random);
        }

        return gaussianExploitationMutation(population, random);
    }

    private List<double[]> uniformExplorationMutation(
            List<double[]> population,
            Random random
    ) {
        List<double[]> mutatedPopulation = new ArrayList<>();

        for (double[] individual : population) {
            double[] newFeatures = individual.clone();
            for (int i = 0; i < individual.length; i++) {
                if (random.nextDouble() <= UNIFORM_MUTATE_COEF * (1. / individual.length)) {
                    newFeatures[i] = generateDoubleBetween(MIN_X_DEFINITION, random, MAX_X_DEFINITION);
                }
            }

            mutatedPopulation.add(newFeatures);
        }

        return mutatedPopulation;
    }

    private List<double[]> gaussianExploitationMutation(
            List<double[]> population,
            Random random
    ) {
        List<double[]> mutatedPopulation = new ArrayList<>();

        // https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)
        double desiredStandardDeviation = GAUSSIAN_DEVIATION_COEF * (MAX_X_DEFINITION - MIN_X_DEFINITION) / 6;
        double desiredMean = 0.;

        for (double[] individual : population) {
            double[] newFeatures = individual.clone();

            for (int i = 0; i < individual.length; i++) {
                if (random.nextDouble() <= GAUSSIAN_MUTATE_COEF * (1. / individual.length)) {
                    newFeatures[i] = newFeatures[i] + generateGaussian(desiredStandardDeviation, random, desiredMean);
                }
            }

            mutatedPopulation.add(newFeatures);
        }

        return mutatedPopulation;
    }

    private double generateGaussian(double standardDeviation, Random random, double mean) {
        return random.nextGaussian() * standardDeviation + mean;
    }
}
