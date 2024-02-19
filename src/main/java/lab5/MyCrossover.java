package lab5;

import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCrossover extends AbstractCrossover<double[]> {
    public static double CROSSOVER_ALPHA_COEF = 0.3;

    protected MyCrossover() {
        super(1);
    }

    protected List<double[]> mate(double[] p1, double[] p2, int i, Random random) {
        ArrayList<double[]> children = new ArrayList<>();

        double alpha = CROSSOVER_ALPHA_COEF;
        double[] child1 = arithmeticCrossover(p1, p2, alpha);
        double[] child2 = arithmeticCrossover(p2, p1, alpha);
        children.add(child1);
        children.add(child2);

        return children;
    }

    private double[] arithmeticCrossover(double[] p, double[] q, double alpha) {
        if (p.length != q.length) {
            throw new IllegalArgumentException("Genomes p, q must be the same length");
        }
        if (alpha < 0 || alpha > 1) {
            throw new IllegalArgumentException("Alpha must be in [0, 1]");
        }
        double[] child = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            child[i] = alpha * p[i] + (1 - alpha) * q[i];
        }
        return child;
    }
}
