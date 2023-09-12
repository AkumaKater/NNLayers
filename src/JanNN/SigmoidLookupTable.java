package JanNN;

import java.util.HashMap;
import java.util.Map;

public class SigmoidLookupTable {
    private static final int TABLE_SIZE = 100;
    private static final double MIN_INPUT = -6.0; // Minimum input value for the table
    private static final double MAX_INPUT = 6.0; // Maximum input value for the table
    private static final double STEP_SIZE = (MAX_INPUT - MIN_INPUT) / TABLE_SIZE;

    private static Map<Integer, Double> sigmoidTable = new HashMap<>();

    // Fill the lookup table with precomputed sigmoid values
    static {
        for (int i = 0; i <= TABLE_SIZE; i++) {
            double x = MIN_INPUT + i * STEP_SIZE;
            sigmoidTable.put(i, sigmoid(x));
        }
    }

    // Precomputed sigmoid function
    private static double sigmoid(double x) {
        return 1.0 / (1 + Math.exp(-x));
    }

    // Lookup the precomputed sigmoid value
    public static double lookupSigmoid(double x) {
        if (x <= MIN_INPUT) {
            return sigmoidTable.get(0); // Return the value for MIN_INPUT
        } else if (x >= MAX_INPUT) {
            return sigmoidTable.get(TABLE_SIZE); // Return the value for MAX_INPUT
        } else {
            // Interpolate between two nearest values in the table
            int index = (int) Math.floor((x - MIN_INPUT) / STEP_SIZE);
            double lowerX = MIN_INPUT + index * STEP_SIZE;
            double upperX = MIN_INPUT + (index + 1) * STEP_SIZE;
            double lowerValue = sigmoidTable.get(index);
            double upperValue = sigmoidTable.get(index + 1);
            double t = (x - lowerX) / (upperX - lowerX);
            return lowerValue + t * (upperValue - lowerValue);
        }
    }

    public static void main(String[] args) {
        double input = -1; // Example input value
        double result = lookupSigmoid(input);
        System.out.println("Sigmoid(" + input + ") = " + result);
        sigmoidTable.forEach((Value, Var) -> {
            // System.out.println(Value + ": " + Var);
        });

        double index = -10.0;
        for (int i = 0; i < 100; i++) {
            System.out.println(index + ": " + lookupSigmoid(index));
            index += 0.5;
        }

    }
}
