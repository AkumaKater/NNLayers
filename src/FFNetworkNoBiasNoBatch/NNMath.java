package FFNetworkNoBiasNoBatch;
import java.util.Random;

public class NNMath {

    public static double[][] RandomDoubleArrayMatrix(int rows, int columns) {

        double[][] array = new double[rows][columns]; // Erstellen des zweidimensionalen Arrays
        // Vorinitialisierung des Arrays mit Zufallszahlen
        for (int i = 0; i < rows; i++) {
            array[i] = RandomDoubleArray(columns);
        }
        return array;
    }

    public static double[] RandomDoubleArray(int length) {
        double[] array = new double[length]; // Erstellen des zweidimensionalen Arrays
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            array[i] = random.nextDouble() - 0.5; // Generieren einer Zufallszahl zwischen -0,5 und 0,5
        }
        return array;
    }
    
}