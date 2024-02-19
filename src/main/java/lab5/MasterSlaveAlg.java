package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.Random;

public class MasterSlaveAlg {

    private static class BestFitness {
        private double bestRun = 0.0;

        private void update(double fitness) {
            if (fitness > bestRun) {
                bestRun = fitness;
            }
        }

        public double getBestRun() {
            return bestRun;
        }
    }

    public static void run(
            boolean singleThreaded,
            int complexity
    ) {
        int dimension = 50;
        int populationSize = 100;
        int generations = 100;

        Random random = new Random();

        CandidateFactory<double[]> factory = new MyFactory(dimension);

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<>();
        operators.add(new MyCrossover());
        operators.add(new MyMutation());
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection();

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity);

        AbstractEvolutionEngine<double[]> algorithm = new SteadyStateEvolutionEngine<>(
                factory, pipeline, evaluator, selection, populationSize, false, random);

        algorithm.setSingleThreaded(singleThreaded);

        BestFitness bestFitness = new BestFitness();
        algorithm.addEvolutionObserver(new EvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                bestFitness.update(bestFit);
//                System.out.println("Generation " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);

        long startTime = System.currentTimeMillis();
        algorithm.evolve(populationSize, 1, terminate);
        long endTime = System.currentTimeMillis();
        long durationMs = (endTime - startTime);

        String algo = singleThreaded ? "Single-thread" : "Master-slave";
        double bestFitVal = bestFitness.getBestRun();
        String bestFit = String.format("%.2f", bestFitVal);
        System.out.println(algo + " | " + complexity + " | " + bestFit + " | " + durationMs);
    }

    public static void main(String[] args) {
        System.out.println("Algo | Complexity | Fitness | Time");
        boolean singleThreaded = true;
        System.out.println();
        for (int complexity = 1; complexity <= 5; complexity++) {
            run(singleThreaded, complexity);
        }

        singleThreaded = false;
        for (int complexity = 1; complexity <= 5; complexity++) {
            run(singleThreaded, complexity);
        }
    }
}
