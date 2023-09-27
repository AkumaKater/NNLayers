package JanNN;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Random;

import MNISTReader.MnistMatrix;

public class NNUtil {

    public static double[][] generateRandomWeights(int rows, int columns) {

        double[][] array = new double[rows][columns]; // Erstellen des zweidimensionalen Arrays

        Random random = new Random();

        // Vorinitialisierung des Arrays mit Zufallszahlen
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i][j] = random.nextDouble() - 0.5; // Generieren einer Zufallszahl zwischen -0,5 und 0,5
            }
        }

        return array;
    }

    public static double[] generateRandomBiasas(int length) {

        double[] array = new double[length]; // Erstellen des zweidimensionalen Arrays

        Random random = new Random();

        // Vorinitialisierung des Arrays mit Zufallszahlen
        for (int i = 0; i < length; i++) {
            array[i] = random.nextDouble() - 0.5; // Generieren einer Zufallszahl zwischen -0,5 und 0,5
        }

        return array;
    }

    public static double[] ArraySubtraction(double[] arr1, double[] arr2) {
        double[] result = new double[arr1.length];
        for (int i = 0; i < arr1.length; i++) {
            result[i] = arr1[i] - arr2[i];
        }
        return result;
    }

    public static double[] KreuzProdukt(double[][] Matrix, double[] inputs) {

        if (Matrix[0].length != inputs.length) {
            System.out.println("Fehler " + Matrix.length + "X" + Matrix[0].length + " " + inputs.length);
        }

        int numOutputNodes = Matrix.length;
        int numInputNodes = Matrix[0].length;
        double[] weightedInputs = new double[numOutputNodes];
        for (int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++) {
            double weightedInput = 0;
            for (int nodeIn = 0; nodeIn < numInputNodes; nodeIn++) {
                weightedInput += inputs[nodeIn] * Matrix[nodeOut][nodeIn];
            }
            // weightedInputs[nodeOut] = Activations.Sigmoid(weightedInput);
            weightedInputs[nodeOut] = weightedInput;
        }
        return weightedInputs;
    }

    public static double[][] DotProdukt(double[][] Matrix1, double[][] Matrix2) {
        if (Matrix1[0].length != Matrix2.length) {
            throw new DotProduktException("Missmacht in Matrix Dimensions to DotProdukt." + "\nFehler " + Matrix1.length
                    + "X" + Matrix1[0].length + " " + Matrix2.length + "X" + Matrix2[0].length);
        }
        int Zeilen = Matrix1.length;
        int Spalten = Matrix2[0].length;
        int SZ = Matrix2.length;
        double[][] result = new double[Zeilen][Spalten];
        for (int zeile = 0; zeile < Zeilen; zeile++) {
            double Spalte[] = new double[Spalten];
            for (int spalte = 0; spalte < Spalten; spalte++) {
                double localResult = 0;
                for (int sz = 0; sz < SZ; sz++) {
                    localResult += Matrix1[zeile][sz] * Matrix2[sz][spalte];
                }
                Spalte[spalte] = localResult;
            }
            result[zeile] = Spalte;
        }
        return result;
    }

    public static double[][] DotProdukt(double[][] Matrix1, double[] Vektor) {
        return DotProdukt(Matrix1, VektorToMatrix(Vektor));
    }

    public static double[] VektorMultiplikation(double[] Vektor1, double[] Vektor2) {
        double[] result = Vektor1.clone();
        for (int i = 0; i < Vektor1.length; i++) {
            result[i] *= Vektor2[i];
        }
        return result;
    }

    public static double[] WertMinusVektor(double Wert, double[] Vektor) {
        double[] result = Vektor.clone();
        for (int i = 0; i < Vektor.length; i++) {
            result[i] = Wert - Vektor[i];
        }
        return result;
    }

    public static void UpdateWeights(Matrix Weights, double Learnrate, Vektor partialDerivative,
            Vektor previousWeightedOutputs) {
        int toNodes = Weights.length;
        int fromNodes = Weights.width;
        for (int fromNode = 0; fromNode < fromNodes; fromNode++) {
            for (int toNode = 0; toNode < toNodes; toNode++) {
                //Weights.setValue(toNode, fromNode,
                        //(Learnrate * partialDerivative[toNode] * previousWeightedOutputs[fromNode]));
            }
        }
    }

    public static double[][] VektorToMatrix(double[] Vektor) {
        double[][] result = new double[Vektor.length][1];
        for (int i = 0; i < Vektor.length; i++) {
            result[i][0] = Vektor[i];
        }
        return result;
    }

    public static double[] MatrixToVektor(double[][] Matrix) {
        double[] result = new double[Matrix.length];
        for (int i = 0; i < Matrix.length; i++) {
            result[i] = Matrix[i][0];
        }
        return result;
    }

    public static int getHighestIndex(double[] numbers) {
        int maxNumberIndex = 0;

        // Schleife durch das Array laufen und die größte Zahl finden
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > numbers[maxNumberIndex]) {
                maxNumberIndex = i;
            }
        }
        return maxNumberIndex;
    }

    public static void printHighestLabel(double[] numbers) {
        System.out.println(HighestLabelToString(numbers));
    }

    public static String HighestLabelToString(double[] numbers) {
        int maxNumberIndex = 0;
        String Stringresult = "";

        // Schleife durch das Array laufen und die größte Zahl finden
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > numbers[maxNumberIndex]) {
                maxNumberIndex = i;
            }
        }
        double[] result = new double[2];
        result[0] = (double) maxNumberIndex;
        result[1] = numbers[maxNumberIndex];
        DecimalFormat df = new DecimalFormat("0.0");
        Stringresult += ("Das Netzwerk denkt dass mit der Wahrscheinlichkeit von: " + df.format(result[1]*100) + "% Es sich um eine " + maxNumberIndex + " handelt.");
        return Stringresult;
    }

    public static int CorrectLabel(double[] numbers, int label){
        int maxNumberIndex = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > numbers[maxNumberIndex]) {
                maxNumberIndex = i;
            }
        }
        return maxNumberIndex;
    }

    public static String ArrayToString(double[] arr) {
        return ArrayToString(arr, true);
    }

    public static void ArrayPrintString(double[] arr) {
        ArrayPrintString(arr, true);
    }

    public static void ArrayPrintString(double[] arr, boolean formatter) {
        System.out.print("[");
        DecimalFormat df = new DecimalFormat("0.0000");
        for (int i = 0; i < arr.length - 2; i++) {
            if (formatter)
                System.out.print(df.format(arr[i]));
            else
                System.out.print(arr[i]);
            System.out.print(", ");
        }
        if (formatter) {
            System.out.print(df.format(arr[arr.length - 1]));
        } else {
            System.out.print(arr[arr.length - 1]);
        }
        System.out.println("]");
    }

    public static String ArrayToString(double[] arr, boolean formatter) {
        String result = "|";
        DecimalFormat df = new DecimalFormat("0.0");
        for (int i = 0; i < arr.length - 1; i++) {
            if (formatter)
                result += (df.format(arr[i]*100))+"%";
            else
                System.out.print(arr[i]);
            result += ("|");
        }
        if (formatter) {
            result += (df.format(arr[arr.length - 1]*100))+"%";
        } else {
            result += (arr[arr.length - 1]);
        }
        result += ("|");
        return result;
    }

    

    public static String getAcuracy(NeuralNetwork nn, MnistMatrix[] mnistMatrix) {
        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int Hindex = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if (Hindex == label) {
                correct++;
            }
        }
        acuracy = (double) correct / (double) iterations * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(acuracy) + "%";
    }
    public static String getAcuracy(FFNetwork.NeuralNetwork nn, MnistMatrix[] mnistMatrix) {
        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            int Hindex = NNUtil.getHighestIndex(nn.Querry(mnistMatrix[i].getInputs()));
            int label = mnistMatrix[i].getLabel();
            if (Hindex == label) {
                correct++;
            }
        }
        acuracy = (double) correct / (double) iterations * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(acuracy) + "%";
    }

    public static String getAcuracy(NeuralNetwork nn, MnistMatrix[] mnistMatrix, LinkedList<Integer> wrongList) {
        int iterations = mnistMatrix.length;
        int correct = 0;
        double acuracy = 0.0;
        for (int i = 0; i < iterations; i++) {
            double[] querry = nn.Querry(mnistMatrix[i].getInputs());
            int Hindex = NNUtil.getHighestIndex(querry);
            int label = mnistMatrix[i].getLabel();
            if (Hindex == label) {
                correct++;
            }else{
                wrongList.add(i);
            }
        }
        acuracy = (double) correct / (double) iterations * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(acuracy) + "%";
    }
}

class DotProduktException extends RuntimeException {
    public DotProduktException(String message) {
        super(message);
    }
}