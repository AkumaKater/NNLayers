package JanNN;

import java.util.Random;

public class NNUtil {
    
    public static double[][] generateRandomWeights(int rows, int columns) {

        double[][] array = new double[rows][columns]; // Erstellen des zweidimensionalen Arrays

        Random random = new Random();

        // Vorinitialisierung des Arrays mit Zufallszahlen
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i][j] = random.nextDouble()-0.5; // Generieren einer Zufallszahl zwischen -0,5 und 0,5
            }
        }

        return array;
    }

    public static double[] ArraySubtraction(double[] arr1, double[] arr2){
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] - arr2[i];
        }
        return result;
    }
}