package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.islands.IslandEvolution;
import org.uncommons.watchmaker.framework.islands.IslandEvolutionObserver;
import org.uncommons.watchmaker.framework.islands.Migration;
import org.uncommons.watchmaker.framework.islands.RingMigration;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static lab5.MasterSlaveAlg.mean;
import static lab5.MasterSlaveAlg.meanLongs;

public class IslandsAlg {

    public static RunResult run(int complexity) {
        int dimension = 50;
        int populationSize = 100;
        int iterations = 100;
        int epochLength = 50;
        int generations = iterations / epochLength;

        Random random = new Random(); // random

        CandidateFactory<double[]> factory = new MyFactory(dimension); // generation of solutions

        ArrayList<EvolutionaryOperator<double[]>> operators = new ArrayList<EvolutionaryOperator<double[]>>();
        operators.add(new MyCrossover()); // Crossover
        operators.add(new MyMutation()); // Mutation
        EvolutionPipeline<double[]> pipeline = new EvolutionPipeline<double[]>(operators);

        SelectionStrategy<Object> selection = new RouletteWheelSelection(); // Selection operator

        FitnessEvaluator<double[]> evaluator = new MultiFitnessFunction(dimension, complexity); // Fitness function

        int islandCount = 10;
        Migration migration = new RingMigration();
        IslandEvolution<double[]> island_model = new IslandEvolution<>(
                islandCount, migration, factory, pipeline, evaluator, selection, random);

        BestFitness bestFitness = new BestFitnessConcurrent();
        island_model.addEvolutionObserver(new IslandEvolutionObserver() {
            public void populationUpdate(PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                bestFitness.update(bestFit);
//                System.out.println("Epoch " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tEpoch best solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
            }

            public void islandPopulationUpdate(int i, PopulationData populationData) {
                double bestFit = populationData.getBestCandidateFitness();
                bestFitness.update(bestFit);
//                System.out.println("Island " + i);
//                System.out.println("\tGeneration " + populationData.getGenerationNumber() + ": " + bestFit);
//                System.out.println("\tBest solution = " + Arrays.toString((double[])populationData.getBestCandidate()));
            }
        });

        TerminationCondition terminate = new GenerationCount(generations);
        int islandPopulationSize = populationSize / islandCount;

        long startTime = System.currentTimeMillis();
        island_model.evolve(islandPopulationSize, 1, epochLength, 2, terminate);
        long endTime = System.currentTimeMillis();
        long durationMs = (endTime - startTime);

        double bestFitVal = bestFitness.getBestRun();

        return new RunResult(bestFitVal, durationMs);
    }

    public static void main(String[] args) {
        System.out.println("Algo | Complexity | Fitness | Time");

        String algo = "Islands";
        for (int complexity = 1; complexity <= 5; complexity++) {
            List<Double> fits = new ArrayList<>();
            List<Long> timeMss = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                RunResult runResult = run(complexity);
                fits.add(runResult.getFit());
                timeMss.add(runResult.getTimeMs());
            }
            double fit = mean(fits);
            int timeMs = meanLongs(timeMss);

            System.out.print(algo + " | " + complexity + " | " + String.format("%.2f", fit) + " | " + timeMs);
            System.out.println();
        }
    }
}
