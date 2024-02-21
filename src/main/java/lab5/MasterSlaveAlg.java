package lab5;

import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.GenerationCount;

import java.util.ArrayList;
import java.util.List;
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

    private static class RunResult {
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

    public static RunResult run(
            boolean singleThreaded,
            int complexity
    ) {
        int dimension = 100;
        int populationSize = 150;
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

        double bestFitVal = bestFitness.getBestRun();

        return new RunResult(bestFitVal, durationMs);
    }

    public static void main(String[] args) {
        System.out.println("Algo | Complexity | Fitness | Time");
        System.out.println();
        for (int singleThreaded = 0; singleThreaded <= 1; singleThreaded++) {
            boolean isSingleThreaded = singleThreaded == 0;
            for (int complexity = 1; complexity <= 5; complexity++) {
                List<Double> fits = new ArrayList<>();
                List<Long> timeMss = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    RunResult result = run(isSingleThreaded, complexity);
                    fits.add(result.getFit());
                    timeMss.add(result.getTimeMs());
                }
                String algo = isSingleThreaded ? "Single-thread" : "Master-slave";
                double fit = mean(fits);
                int timeMs = meanLongs(timeMss);
                System.out.print(algo + " | " + complexity + " | " + String.format("%.2f", fit) + " | " + timeMs);
                System.out.println();
            }
        }
    }

    public static double mean(List<Double> fits) {
        double sum = 0;
        for (Double fit : fits) {
            sum += fit;
        }
        return sum / fits.size();
    }

    public static int meanLongs(List<Long> timeMss) {
        double sum = 0;
        for (Long timeMs : timeMss) {
            sum += timeMs;
        }
        return (int) (sum / timeMss.size());
    }
}
