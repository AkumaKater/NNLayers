package JanNN;

import java.util.Arrays;
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

    public static double[] KreuzProdukt(double[][] Matrix, double[] inputs){

        if(Matrix[0].length != inputs.length){System.out.println("Fehler " + Matrix.length+"X"+Matrix[0].length +" "+ inputs.length);}

        int numOutputNodes = Matrix.length;
        int numInputNodes = Matrix[0].length;
        double[] weightedInputs = new double[numOutputNodes];
        for(int nodeOut = 0; nodeOut < numOutputNodes; nodeOut++){
            double weightedInput = 0;
            for(int nodeIn = 0; nodeIn < numInputNodes; nodeIn++){
                weightedInput += inputs[nodeIn] * Matrix[nodeOut][nodeIn];
            }
            //weightedInputs[nodeOut] = Activations.Sigmoid(weightedInput);
            weightedInputs[nodeOut] = weightedInput;
        }
        return weightedInputs;
    }

    public static double[][] DotProdukt(double[][] Matrix1, double[][] Matrix2){
        if(Matrix1[0].length!=Matrix2.length){System.out.println("Missmacht in Matrix Dimensions to DotProdukt."+ "\nFehler " + Matrix1.length+"X"+Matrix1[0].length +" "+ Matrix2.length+"X"+Matrix2[0].length);};
        int Zeilen = Matrix1.length;
        int Spalten = Matrix2[0].length;
        int SZ = Matrix2.length;
        double[][] result = new double[Zeilen][Spalten];
        for(int zeile = 0; zeile < Zeilen; zeile++){
            double Spalte[] = new double[Spalten];
            for(int spalte = 0; spalte < Spalten; spalte++){
                double localResult = 0;
                for(int sz = 0; sz < SZ; sz++){
                    localResult += Matrix1[zeile][sz] * Matrix2[sz][spalte];
                }
                Spalte[spalte] = localResult;
            }
            result[zeile] = Spalte;
        }
        return result;
    }

    public static double[][] DotProdukt(double[][] Matrix1, double[] Vektor){
        return DotProdukt(Matrix1, VektorToMatrix(Vektor));
    }

    public static double[] VektorMultiplikation(double[] Vektor1, double[] Vektor2){
        double[] result = Vektor1.clone();
        for(int i = 0; i < Vektor1.length; i++){
            result[i] *= Vektor2[i];
        }
        return result;
    }

    public static double[] WertMinusVektor(double Wert, double[] Vektor){
        double[] result = Vektor.clone();
        for(int i = 0; i < Vektor.length; i++){
            result[i] = Wert - Vektor[i];
        }
        return result;
    }

    public static void UpdateWeights(double[][] Weights, double Learnrate, double[] Vektor1, double[] Vektor2){
        int fromNodes = Weights.length;
        int toNodes = Weights[0].length;
        for(int toNode = 0; toNode < toNodes; toNode++){
            for(int fromNode = 0; fromNode < fromNodes; fromNode++){
                Weights[fromNode][toNode] += Learnrate * Vektor1[toNode] * Vektor2[fromNode];
            }
        }
    }

    public static double[][] VektorToMatrix(double[] Vektor){
        double[][] result = new double[Vektor.length][1];
        for(int i = 0; i < Vektor.length; i++){
            result[i][0] = Vektor[i];
        }
        return result;
    }
    public static double[] MatrixToVektor(double[][] Matrix){
        double[] result = new double[Matrix.length];
        for(int i = 0; i < Matrix.length; i++){
            result[i] = Matrix[i][0];
        }
        return result;
    }
}